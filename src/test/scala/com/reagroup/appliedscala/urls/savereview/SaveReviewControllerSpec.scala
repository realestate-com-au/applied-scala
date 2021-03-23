package com.reagroup.appliedscala.urls.savereview

import cats.data.NonEmptyList
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.models._
import io.circe.literal._
import org.http4s._
import org.http4s.testing.Http4sMatchers
import org.http4s.testing.IOMatchers
import org.specs2.mutable.Specification

class SaveReviewControllerSpec
    extends Specification
    with Http4sMatchers[IO]
    with IOMatchers {

  "when saving a valid review" should {

    val json =
      json"""
        {
          "author": "Bob",
          "comment": "This is a good movie"
        }
      """

    val request = Request[IO](method = Method.POST).withEntity(json.noSpaces)

    val controller =
      new SaveReviewController((_: MovieId, _: NewReviewRequest) =>
        IO.pure(ReviewId(1).valid)
      )

    val actual = controller.save(100, request).unsafeRunSync()

    "return status code Created" in {

      actual must haveStatus(Status.Created)

    }

    "return reviewId in response body" in {

      val expectedJson =
        json"""
          { "id": 1 }
        """
      actual must haveBody(expectedJson.noSpaces)

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

    val request =
      Request[IO](method = Method.POST).withEntity(invalidJson.noSpaces)

    val saveNewReview = (_: MovieId, _: NewReviewRequest) =>
      IO.pure(
        NonEmptyList.of(ReviewAuthorTooShort, ReviewCommentTooShort).invalid
      )

    val controller = new SaveReviewController(saveNewReview)

    val actual = controller.save(100, request).unsafeRunSync()

    "return status code BadRequest" in {

      actual must haveStatus(Status.BadRequest)

    }

    "return errors in response body" in {

      val expectedJson =
        json"""
          [ { "error": "REVIEW_AUTHOR_TOO_SHORT" }, { "error": "REVIEW_COMMENT_TOO_SHORT"} ]
        """
      actual must haveBody(expectedJson.noSpaces)

    }

  }

}
