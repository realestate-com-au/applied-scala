package com.reagroup.appliedscala.urls.fetchmovie

import cats.effect.IO
import com.reagroup.appliedscala.models._
import com.reagroup.appliedscala.urls.ErrorHandler
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class FetchMovieController(fetchMovie: MovieId => IO[Option[Movie]]) extends Http4sDsl[IO] {

  /**
    * 1. Convert `movieId` into a value of type `MovieId`
    * 2. Call `fetchMovie` and we need to call `attempt` on the result so we can handle errors
    * 3. Pattern match on the results and convert each possible case into an HTTP response. 
    * 4. If the movie does not exist, we want to return a 404.
    *
    * Hint: You can use `NotFound()` to construct a 404 and `Ok()` to construct a 200.
    * Delegate the error case to the `ErrorHandler`.
   *
    */
  def fetch(movieId: Long): IO[Response[IO]] = for {
    errorOrMovie <- fetchMovie(MovieId(movieId)).attempt
    resp <- errorOrMovie match {
      case Right(Some(movie)) => Ok(movie)
      case Right(None) => NotFound("Oops not found")
      case Left(e) => ErrorHandler(e)
    }
  } yield resp
}

