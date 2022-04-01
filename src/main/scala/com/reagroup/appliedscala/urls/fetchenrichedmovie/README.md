## GET movies/id/?enriched=true

Let's add an endpoint to fetch a movie, which is enriched with some metascore info from the OMDB API.

### 1. Intro to OMDB

Go to [http://www.omdbapi.com/](http://www.omdbapi.com/), scroll down to _By ID or Title_.

We are going to search for a movie using the `?t` query parameter. If we go to [http://www.omdbapi.com/?t=Titanic&apikey=7f9b5b06](http://www.omdbapi.com/?t=Titanic&apikey=7f9b5b06), we will see the following response:

```
{
  Title: "Titanic",
  Year: "1997",
  Rated: "PG-13",
  Released: "19 Dec 1997",
  Metascore: "75"
  ...
  ...
  ...
}
```

We want to extract the `Metascore` and return it along with our `Movie`.

### 2. `Metascore` - decoder (exercise)

We can now implement a `Decoder` instance to convert a `Json` response from OMDB to a `Metascore`.

_**Complete exercise**_

_**Run unit test: `MetascoreSpec`**_

### 3. `Http4sMetascoreRepository` (exercise)

This has mostly been implemented for you. We use Http4s' `Uri` type to encode the `movieName` so spaces become `%20`, for instance, and then we make a request using an Http4s HTTP Client. 

Hint: We want to start by converting the `String` in the response body into a `Metascore`. For the purpose of this exercise, let's convert any failures from Circe into a `None`.

_**Complete exercise**_

### 4. `FetchEnrichedMovieService` (exercise)

Moving on to the `Service`, we can see it has access to _two_ functions. The first one is to fetch a `Movie` and the second is to fetch a `Metascore`. More concretely, the first is the Postgresql database call and the second one is the OMDB API call.

For the purpose of this exercise, if we get no `Metascore`, we want to error.

_**Complete exercise**_

_**Run unit test: `FetchEnrichedMovieServiceSpec`**_

### 5. `FetchEnrichedMovieController` and `Encoder[EnrichedMovie]` (exercise)

Again, this is not much different than what we've seen. If we have `Some(enrichedMovie)` we want to return `Ok(...)`. If we have `None`, we want to return `NotFound()`. If we have a `Left`, we want to call the `ErrorHandler`.

If you try to convert an `EnrichedMovie` to `Json` using `.asJson`, you will get a compilation error. This is because we have not created the `Encoder` instance for `EnrichedMovie`.

Work on the `Controller` and also the `Encoder`. You will have to create your own custom encoder this time because we want to return a flat `Json`, even though `EnrichedMovie` is a nested structure.

_**Complete exercise**_

_**Run unit test: `FetchEnrichedMovieControllerSpec`**_

### 6. Review the wiring in `AppRuntime` and update `AppRoutes` (exercise)

In `AppRuntime`, `FetchEnrichedMovieController` has been constructed using `FetchEnrichedMovieService`, and passed into `AppRoutes`.

`AppRoutes` has an unused handler called `fetchEnrichedMovieHandler`. Update the `GET /movies/{id}` route to check for the existence of a `?enriched=true` query parameter and call `fetchEnrichedMovieHandler` if the query parameter exists.

You can pattern match on a query parameter as such:

```
case GET -> Root / "movies" / LongVar(id) :? OptionalEnrichedMatcher(maybeEnriched) => ??? 
```  

_**Run unit test: `AppRoutesSpec`**_
