package com.reagroup.appliedscala.urls.savemovie

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

sealed trait MovieValidationError

case object MovieNameTooShort extends MovieValidationError

case object MovieSynopsisTooShort extends MovieValidationError

object MovieValidationError {

  /**
    * This function turns an `MovieValidationError` to a `String`.
    * This will be used in our `Encoder`.
    *
    * `MovieNameTooShort` -> "MOVIE_NAME_TOO_SHORT"
    * `MovieSynopsisTooShort` -> "MOVIE_SYNOPSIS_TOO_SHORT"
    */
  def show(error: MovieValidationError): String =
    error match {
      case MovieNameTooShort => "MOVIE_NAME_TOO_SHORT"
      case MovieSynopsisTooShort => "MOVIE_SYNOPSIS_TOO_SHORT"
    }

  /**
   * Here is the Encoder instance.
   *
   * We want the resulting Json to look like this:
   *
   * {
   * "error": "MOVIE_NAME_TOO_SHORT"
   * }
   */
  implicit val encoder: Encoder[MovieValidationError] =
    Encoder { err =>
      Json.obj("error" -> MovieValidationError.show(err).asJson)
    }

}
