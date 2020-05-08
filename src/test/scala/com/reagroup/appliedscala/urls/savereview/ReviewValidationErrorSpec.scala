package com.reagroup.appliedscala.urls.savereview

import org.specs2.mutable.Specification

class ReviewValidationErrorSpec extends Specification {

  "show" should {
    "stringify MovieNameTooShort" in {
      val result = ReviewValidationError.show(ReviewAuthorTooShort)

      result must_=== "REVIEW_AUTHOR_TOO_SHORT"
    }

    "return NewMovie" in {
      val result = ReviewValidationError.show(ReviewCommentTooShort)

      result must_=== "REVIEW_COMMENT_TOO_SHORT"
    }
  }

}
