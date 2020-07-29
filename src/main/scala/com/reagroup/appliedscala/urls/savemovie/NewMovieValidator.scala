package com.reagroup.appliedscala.urls.savemovie

import cats.data.Validated._
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._

object NewMovieValidator {

  /**
    * How do we combine two validations together? Refer to `ValidationExercises` for a refresher!
    *
    * Hint: `Validated` has an Applicative instance.
    */
  def validate(newMovie: NewMovieRequest): ValidatedNel[MovieValidationError, ValidatedMovie] = {
//    val thing = validateMovieName(newMovie.name).product(validateMovieSynopsis(newMovie.synopsis))
//    thing.map(result => ValidatedMovie(result._1, result._2))
//
    (validateMovieName(newMovie.name), validateMovieSynopsis(newMovie.synopsis)).mapN(ValidatedMovie)
  }

  /**
    * If `name` is empty, return an `InvalidNel` containing `MovieNameTooShort`,
    * else return a `Valid` containing the `name`.
    *
    * Hint: You can use `.isEmpty` or `.nonEmpty` on `String`
    */
  private def validateMovieName(name: String): ValidatedNel[MovieValidationError, String] =
  // CondNel way
  //  Validated.condNel(name.nonEmpty, name, MovieNameTooShort)

  // If / Else way!
    if(name.isEmpty) {
      MovieNameTooShort.invalidNel
    } else {
      name.validNel
    }

  /**
    * If `synopsis` is empty, return an `InvalidNel` containing `MovieSynopsisTooShort`,
    * else return a `Valid` containing the `synopsis`.
    *
    * Hint: You can use `.isEmpty` or `.nonEmpty` on `String`
    */
  private def validateMovieSynopsis(synopsis: String): ValidatedNel[MovieValidationError, String] =
    if(synopsis.isEmpty) {
      MovieSynopsisTooShort.invalidNel
    } else {
      synopsis.validNel
    }


}
