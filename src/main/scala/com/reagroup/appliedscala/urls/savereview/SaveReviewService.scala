package com.reagroup.appliedscala.urls.savereview

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.models.{Movie, MovieId}

class SaveReviewService(saveReview: (MovieId, ValidatedReview) => IO[ReviewId],
                        fetchMovie: MovieId => IO[Option[Movie]]) {

  def save(movieId: MovieId, review: NewReviewRequest): IO[ValidatedNel[ReviewValidationError, ReviewId]] = {
    for {
      maybeFetchedMovie <- fetchMovie(movieId)
      maybeValidatedMovie = validateMovie(maybeFetchedMovie)
      maybeValidatedReview = validateReview(review)
      combinedValidatedReview = maybeValidatedMovie.productR(maybeValidatedReview)
      reviewId <- combinedValidatedReview.traverse(validatedReview => saveReview(movieId, validatedReview))
//      reviewId = combinedValidatedReview.map(validatedReview => saveReview(movieId, validatedReview)) match {
//        case Valid(ioReview) => ioReview.map(reviewId => Valid(reviewId))
//        case Invalid(error) => IO(Invalid(error))
//      }
    } yield reviewId
  }

  private def validateMovie(maybeMovie: Option[Movie]): ValidatedNel[ReviewValidationError, Movie] = {
    maybeMovie match {
      case Some(movie) => movie.valid
      case None => MovieDoesNotExist.invalidNel
    }
  }

  private def validateReview(review: NewReviewRequest): ValidatedNel[ReviewValidationError, ValidatedReview] = {
    NewReviewValidator.validate(review)
  }

}
