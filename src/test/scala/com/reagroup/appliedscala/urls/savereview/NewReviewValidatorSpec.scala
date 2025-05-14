package com.reagroup.appliedscala.urls.savereview

import cats.data.*
import cats.implicits.*
import com.reagroup.appliedscala.urls.savereview.ReviewValidationError.*
import org.specs2.mutable.Specification

class NewReviewValidatorSpec extends Specification {

  "validate" should {
    "return all errors if new review has no name and no synopsis" in {
      val review = NewReviewRequest("", "")

      val result = NewReviewValidator.validate(review)

      result must_=== NonEmptyList.of(ReviewAuthorTooShort, ReviewCommentTooShort).invalid
    }

    "return NewMovie" in {
      val review = NewReviewRequest("bob", "cool movie")

      val result = NewReviewValidator.validate(review)

      result must_=== ValidatedReview("bob", "cool movie").valid
    }
  }

}
