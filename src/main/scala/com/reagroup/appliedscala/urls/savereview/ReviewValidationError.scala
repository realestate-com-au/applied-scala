package com.reagroup.appliedscala.urls.savereview

import io.circe.Json
import io.circe.syntax._
import io.circe.Encoder

enum ReviewValidationError {
  case ReviewAuthorTooShort
  case ReviewCommentTooShort
  case MovieDoesNotExist
}

object ReviewValidationError {

  /**
    * Write a function that turns an `ReviewValidationError` to a `String`.
    * This will be used in our `Encoder`.
    *
    * `ReviewAuthorTooShort` -> "REVIEW_AUTHOR_TOO_SHORT"
    * `ReviewCommentTooShort` -> "REVIEW_COMMENT_TOO_SHORT"
    * `MovieDoesNotExist` -> "MOVIE_DOES_NOT_EXIST"
    *
    * Hint: Use pattern matching
    */
  def show(error: ReviewValidationError): String = error match {
    case ReviewAuthorTooShort  => "REVIEW_AUTHOR_TOO_SHORT"
    case ReviewCommentTooShort => "REVIEW_COMMENT_TOO_SHORT"
    case MovieDoesNotExist     => "MOVIE_DOES_NOT_EXIST"
  }

  /**
    * Add an Encoder instance here
    *
    * We want the resulting Json to look like this:
    *
    * {
    *   "error": "REVIEW_AUTHOR_TOO_SHORT"
    * }
    *
    * Hint: You don't want to use `deriveEncoder` here
    */

   given Encoder[ReviewValidationError] =
    Encoder { err =>
      Json.obj("error" -> ReviewValidationError.show(err).asJson)
    }

}
