package com.reagroup.appliedscala.urls.savereview

import cats.data.Validated._
import cats.data.ValidatedNel
import cats.effect.IO
import com.reagroup.appliedscala.models._
import com.reagroup.appliedscala.urls.ErrorHandler
import io.circe.Json
import io.circe.syntax._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class SaveReviewController(saveNewReview: (MovieId, NewReviewRequest) => IO[ValidatedNel[ReviewValidationError, ReviewId]]) extends Http4sDsl[IO] {

  def save(movieId: Long, req: Request[IO]): IO[Response[IO]] = {
    for {
      newReviewRequest <- req.as[NewReviewRequest]
      eitherValidatedReviewId <- saveNewReview(MovieId(movieId), newReviewRequest).attempt
      response <- eitherValidatedReviewId match {
        case Left(err) => ErrorHandler(err)
        case Right(Valid(reviewId)) => Created(reviewId)
        case Right(Invalid(validationErrors)) => BadRequest(validationErrors)
      }

    } yield response
  }

}
