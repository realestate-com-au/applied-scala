package com.reagroup.appliedscala.urls.fetchenrichedmovie

import cats.effect.IO
import com.reagroup.appliedscala.models._
import com.reagroup.appliedscala.urls.ErrorHandler
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class FetchEnrichedMovieController(fetchEnrichedMovie: MovieId => IO[Option[EnrichedMovie]]) extends Http4sDsl[IO] {

  /**
    * 1. Convert `movieId` to a value of `MovieId` type
    * 2. Call `fetchEnrichedMovie` and don't forget to `attempt` so you can handle errors!
    * 3. Pattern match on the results and convert each possible case into an HTTP response
    *
    * Hint: Refer to `FetchMovieController` if you're stuck.
    */
  def fetch(movieId: Long): IO[Response[IO]] = {
    val id: MovieId = MovieId(movieId)

    fetchEnrichedMovie(id).attempt.flatMap {
      case Left(error) => ErrorHandler(error)
      case Right(Some(enrichedMovie)) => Ok(enrichedMovie)
      case Right(None) => NotFound()
    }

  }

}