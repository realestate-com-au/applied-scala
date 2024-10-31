package com.reagroup.appliedscala.urls.savereview

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import com.reagroup.appliedscala.models.*
import com.reagroup.appliedscala.urls.savereview.ReviewValidationError.*
import io.circe.literal.*
import org.http4s.*
import org.specs2.mutable.Specification

class SaveReviewControllerSpec extends Specification {

  "when saving a valid review" should {

    val json =
      json"""
        {
          "author": "Bob",
          "comment": "This is a good movie"
        }
      """

    val request = Request[IO](method = Method.POST).withEntity(json.noSpaces)

    val controller = new SaveReviewController((_: MovieId, _: NewReviewRequest) => IO.pure(ReviewId(1).valid))

    val actual = controller.save(100, request).unsafeRunSync()

    "return status code Created" in {

      actual.status must beEqualTo(Status.Created)

    }

    "return reviewId in response body" in {

      val expectedJson =
        json"""
          { "id": 1 }
        """
      actual.as[String].unsafeRunSync() must beEqualTo(expectedJson.noSpaces)

    }

  }

  "when saving an invalid review" should {

    val invalidJson =
      json"""
        {
          "author": "",
          "comment": ""
        }
      """

    val request = Request[IO](method = Method.POST).withEntity(invalidJson.noSpaces)

    val saveNewReview = (_: MovieId, _: NewReviewRequest) => IO.pure(NonEmptyList.of(ReviewAuthorTooShort, ReviewCommentTooShort).invalid)

    val controller = new SaveReviewController(saveNewReview)

    val actual = controller.save(100, request).unsafeRunSync()

    "return status code BadRequest" in {

      actual.status must beEqualTo(Status.BadRequest)

    }

    "return errors in response body" in {

      val expectedJson =
        json"""
          [ { "error": "REVIEW_AUTHOR_TOO_SHORT" }, { "error": "REVIEW_COMMENT_TOO_SHORT"} ]
        """
      actual.as[String].unsafeRunSync() must beEqualTo(expectedJson.noSpaces)

    }

  }

}
