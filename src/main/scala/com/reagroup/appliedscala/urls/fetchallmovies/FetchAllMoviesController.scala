package com.reagroup.appliedscala.urls.fetchallmovies

import cats.effect.IO
import com.reagroup.appliedscala.models.Movie
import com.reagroup.appliedscala.urls.ErrorHandler
import io.circe.Json
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class FetchAllMoviesController(fetchMovies: IO[Vector[Movie]]) extends Http4sDsl[IO] {

  /**
    * 1. Execute the `fetchMovies` function to retrieve all our movies.
    * 2. Call `attempt` "at the end of the world" (right before we serve responses to the client)
    * because every `IO` can fail and we want to handle errors gracefully.
    * 3. Pattern match on the results and convert each possible case into an HTTP response.
    * 4. We have an `ErrorHandler` that handles all the errors in our program.
    */
  def fetchAll: IO[Response[IO]] = for {
    errorOrMovies <- fetchMovies.attempt
    resp <- errorOrMovies match {
      case Right(movies) => Ok(movies.map(movieToJson))
      case Left(e) => ErrorHandler(e)
    }
  } yield resp

  /**
    * The reason we aren't using an `Encoder` instance for this conversion here is because
    * we want you to write your own `Encoder` instance for the `GET movie/id` endpoint.
    * Don't want to giveaway the answer :)
    */
  def movieToJson(movie: Movie): Json =
    Json.obj("name" -> movie.name.asJson)

}
