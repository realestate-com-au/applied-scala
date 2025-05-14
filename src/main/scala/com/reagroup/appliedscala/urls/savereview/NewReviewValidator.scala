package com.reagroup.appliedscala.urls.savereview

import cats.data.Validated.*
import cats.data.ValidatedNel
import cats.implicits.*
import com.reagroup.appliedscala.urls.savereview.ReviewValidationError.*

object NewReviewValidator {

  /**
    * How do we combine two validations together? Refer to `ValidationExercises` for a refresher!
    *
    * Hint: `Validated` has an Applicative instance.
    */
  def validate(review: NewReviewRequest): ValidatedNel[ReviewValidationError, ValidatedReview] =
    (validateAuthor(review.author), validateComment(review.comment)).mapN(ValidatedReview.apply)

  /**
    * If `author` is empty, return an `InvalidNel` containing `ReviewAuthorTooShort`,
    * else return a `Valid` containing the `author`.
    *
    * Hint: You can use `.isEmpty` or `.nonEmpty` on `String`
    */
  private def validateAuthor(author: String): ValidatedNel[ReviewValidationError, String] =
    if(author.nonEmpty) {
      author.validNel
    } else {
      ReviewAuthorTooShort.invalidNel
    }

  /**
    * If `comment` is empty, return an `InvalidNel` containing `ReviewCommentTooShort`,
    * else return a `Valid` containing the `comment`.
    *
    * Hint: You can use `.isEmpty` or `.nonEmpty` on `String`
    */
  private def validateComment(comment: String): ValidatedNel[ReviewValidationError, String] =
    if(comment.nonEmpty) {
      comment.validNel
    } else {
      ReviewCommentTooShort.invalidNel
    }

}
