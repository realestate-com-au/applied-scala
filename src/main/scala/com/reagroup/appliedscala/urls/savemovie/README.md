## POST movies

This endpoint is different than the `GET` ones because there is a request body that we receive from the client that we need to decode and turn into a Movie.

### 1. `MovieValidationError` (2 min)

We have built an ADT that represents all possible validation errors for a `NewMovieRequest`.

If the `name` is empty, return a `MovieNameTooShort` and if the `synopsis` is empty, return a `MovieSynopsisTooShort`.

### 2. `NewMovieRequest` vs `ValidatedMovie` (3 min)

We have two different models here. A `NewMovieRequest` represents a request that has been successfully decoded containing a `name` and `synopsis` to save into the database. 

However, at the point of decoding, we do not know whether the `name` and `synopsis` obey our business rules.

We need to validate this model and if it is valid, we create a different type `ValidatedMovie` that represents this. The two types contain the same information, but by making them two distinct types, we can enforce additional type safety and better readability.

### 3. `NewMovieValidator` (exercise) (5 min)

Build a `validate` function that takes a `NewMovieRequest` and returns either an `Invalid[NonEmptyList[MovieValidationError]]` or a `Valid[ValidatedMovie]`.

Remember what you learned from `ValidationExercises`.

_**Complete exercise**_

_**Run unit test: `NewMovieValidatorSpec`**_

### 4. `SaveMovieService` (exercise) (5 min)

We can see `SaveMovieService` has a `saveMovie` function taken in as a dependency. It is of type `ValidatedMovie => IO[MovieId]`. 

The `save` function accepts a `NewMovieRequest` and returns a `IO[ValidatedNel[MovieValidationError, MovieId]]`. We want to validate the request, if it is valid, we save the movie and return the `MovieId`, otherwise we return all the errors. 

_**Complete exercise**_

_**Run unit test: `SaveMovieServiceSpec`**_

### 5. `SaveMovieController` (exercise) (10 min)

The `Controller` is a little different this time. We have the entire request as an argument to the function. We want to decode the request into a `NewMovieRequest` and then pass that into the `saveNewMovie` function in the class constructor.

After that, we want to `attempt` as usual and handle each possibility.

You will need to complete the following in order to get the `Controller` to compile:

- `Decoder[NewMovieRequest]` 
- `Encoder[MovieValidationError]`
- `Encoder[MovieId]`

### 6. `NewMovieRequest` decoder (exercise) (5 min)

We need to describe how to convert from a `Json` into `NewMovieRequest`.

Hint: The incoming JSON body is the same shape as our `NewMovieRequest` case class.

```
{
  "name": "Titanic",
  "synopsis": "A movie about ships"
}
```

_**Complete exercise**_

### 7. `MovieValidationError.show` and `MovieValidationError` encoder (exercise) (10 min)

Implement the `show` function first. This is a way to go from our type into a `String`. 

_**Complete exercise**_

_**Run unit test: `MovieValidationErrorSpec`**_

Next, create an `Encoder` instance to convert our type into `Json`.

_**Complete exercise**_

### 8. `MovieId` encoder (exercise) (5 min)

We also need a way to convert a `MovieId` into `Json`.

_**Complete exercise**_

### 9. Finish off `SaveMovieController`! (5 min)

_**Complete exercise**_

_**Run unit test: `SaveMovieControllerSpec`**_

### 10. Wire it all up in `AppRuntime` (5 min)

Now let's wire the `Service` and `Controller` up in `AppRuntime`.

Pass the newly instantiated `saveMovieController` into `AppRoutes`.

### 11. Update `AppRoutes` (5 min)

Change AppRoutes to accept a `SaveMovieController` and then call `saveMovie` given the `req`uest!

Note that `req@POST...` means that `req` is an alias for the value on the right hand side.

Start the app using `./auto/start-local` and test it out!

### Test queries:

```
curl -H "Accept: application/json"  -X POST -d "{\"name\": \"\", \"synopsis\": \"\"}" http://localhost:9200/movies

curl -H "Accept: application/json"  -X POST -d "{\"name\": \"Space Jam\", \"synopsis\": \"A movie about basketball\"}" http://localhost:9200/movies

curl http://localhost:9200/movies/2
```
