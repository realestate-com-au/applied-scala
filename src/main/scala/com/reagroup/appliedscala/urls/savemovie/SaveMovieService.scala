package com.reagroup.appliedscala.urls.savemovie

import cats.data._
import cats.implicits._
import cats.effect.IO
import com.reagroup.appliedscala.models._

class SaveMovieService(saveMovie: ValidatedMovie => IO[MovieId]) {

  /**
    * Before saving a `NewMovieRequest`, we want to validate the request in order to get a `ValidatedMovie`.
    * Complete `NewMovieValidator`, then use it here before calling `saveMovie`.
    */
  def save(newMovieReq: NewMovieRequest): IO[ValidatedNel[MovieValidationError, MovieId]] = {
    val validatedMovie: ValidatedNel[MovieValidationError, ValidatedMovie] = NewMovieValidator.validate(newMovieReq)
    validatedMovie.traverse(saveMovie)
  }
}
