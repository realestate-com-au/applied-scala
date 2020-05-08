package com.reagroup.appliedscala.urls.savereview

import cats.data.NonEmptyList
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.models._
import org.specs2.mutable.Specification

class SaveReviewServiceSpec extends Specification {
  val movie = Movie("Empire Strikes Back", "Guy unwittingly is kissed by sister", Vector.empty)

  "saveReview" should {

    "return errors for invalid review" in {

      val saveReview = (movieId: MovieId, review: ValidatedReview) => IO.pure(ReviewId(12345))
      val fetchMovie = (movieId: MovieId) => IO.pure(Option(movie))


      val service = new SaveReviewService(saveReview, fetchMovie)

      val reviewToSave = NewReviewRequest("", "")

      val result = service.save(MovieId(12345), reviewToSave)

      result.unsafeRunSync() must_=== NonEmptyList.of(ReviewAuthorTooShort, ReviewCommentTooShort).invalid

    }

    "return errors for non-existent movie" in {

      val saveReview = (movieId: MovieId, review: ValidatedReview) => IO.pure(ReviewId(12345))
      val fetchMovie = (movieId: MovieId) => IO.pure(None)


      val service = new SaveReviewService(saveReview, fetchMovie)

      val reviewToSave = NewReviewRequest("", "")

      val result = service.save(MovieId(12345), reviewToSave)

      result.unsafeRunSync() must_=== NonEmptyList.of(MovieDoesNotExist, ReviewAuthorTooShort, ReviewCommentTooShort).invalid

    }

    "return saved reviewId" in {

      val saveReview = (movieId: MovieId, review: ValidatedReview) => IO.pure(ReviewId(12345))
      val fetchMovie = (movieId: MovieId) => IO.pure(Option(movie))


      val service = new SaveReviewService(saveReview, fetchMovie)

      val reviewToSave = NewReviewRequest("bob", "good movie")

      val result = service.save(MovieId(12345), reviewToSave)

      result.unsafeRunSync() must_=== ReviewId(12345).valid

    }

  }

}
