## GET movies

_There is no exercise for this endpoint._

We have already implemented an endpoint for you! Let's have a look at it in `AppRoutes`.

### 1. `AppRoutes`

We can see that `AppRoutes` already has a `FetchAllMoviesController` in scope. If the incoming request is `GET movies`, we call the `FetchAllMoviesController`.

Let's look at the `Controller` and `Service` implementations now. Open up the `fetchallmovies` package in the project. This package contains the `Service` and `Controller` for fetching all movies. 

### 2. `FetchAllMoviesService`

The `Service` has a `fetchAllMovies` passed in as dependency. The `Service` is typically where your business logic is, but fetching all movies is extremely simple. All we need to do here is call `fetchAllMovies`.

More concretely, `fetchAllMovies` is the function defined in `PostgresRepository` for fetching all the movies from the database. Let's have a quick look at `PostgresRepository`, which contains all the logic needed to interact with our database.

### 3. `PostgresRepository`

We have implemented all the SQL needed to work with Postgres in this file. We are using a library called `Doobie`. If we look at this file, we have one function for each of our endpoints already implemented. Keep in mind that the return type of each function is an `IO`.

The function that this endpoint uses is `fetchAllMovies`.

### 4. `FetchAllMoviesController`

The `Controller` takes the function from the `Service` in as a dependency. Every `IO` has potential for failure by definition. At this point, we call `fetchAll` and then we attempt it here, before serving any result to the client. We typically want to defer error handling to the end of our program so we do not have to handle it all over the place.

Now we can pattern match on the result. If we have `Right(movies)`, we construct an 200 `Ok(...)` response with the `movies` converted to `Json`. Otherwise, we pass the error to our `ErrorHandler`, which we will explain later. We should really be using an `Encoder[Movie]` here and do `movies.asJson` but we aren't because we want you to write the `Encoder` instance for `Movie` later on. We don't want to give away the answer!

### 5. `ErrorHandler` detour

We have written this error handler to convert errors into a nice `Json` response. Every time you get a `Left` in your `Controller`, call this function.

Because `AppError` is an ADT, if we add a new `AppError`, we get a compilation error in `encodeAppError` because of non-exhaustiveness.

### 6. Hook it all up in `AppRuntime` and `AppRoutes`

In `AppRuntime`, we instantiate our `PostgresqlRepository`, our `FetchAllMoviesService` and `FetchAllMoviesController`. Then we pass the `Controller` into `AppRoutes`.

If we have a look at `AppRoutes`, we can see that when we call `GET movies`, we run `fetchAllMovies`, which calls the `apply` method in the `FetchAllMoviesController`.

### 7. Start up the app and test

Run `./auto/start-local`

`curl http://localhost:9200/movies`