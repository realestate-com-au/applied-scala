package com.reagroup.appliedscala.urls.savemovie

import cats.data.Validated._
import cats.data.{NonEmptyList, ValidatedNel}
import cats.effect.IO
import com.reagroup.appliedscala.models._
import io.circe.{Encoder, Json}
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class SaveMovieController(saveNewMovie: NewMovieRequest => IO[ValidatedNel[MovieValidationError, MovieId]]) extends Http4sDsl[IO] {

  /**
    * 1. Decode the `req` into a `NewMovieRequest` (refer to the decoding exercises in CirceExercises)
    * 2. Call `saveNewMovie` and don't forget to `attempt` to deal with errors!
    * 3. Pattern match and convert every case into an HTTP response. To Pattern match on `Validated`, use `Invalid` and `Valid`.
    * Hint: Use `Created(...)` to return a 201 response when the movie is successfully saved and `BadRequest(...)` to return a 403 response when there are errors.
    */
  def save(req: Request[IO]): IO[Response[IO]] =
    ???

}
