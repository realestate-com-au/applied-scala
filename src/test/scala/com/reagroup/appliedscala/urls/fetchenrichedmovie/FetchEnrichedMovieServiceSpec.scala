package com.reagroup.appliedscala.urls.fetchenrichedmovie

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.reagroup.appliedscala.models.*
import com.reagroup.appliedscala.models.AppError.EnrichmentFailure
import org.specs2.mutable.Specification

class FetchEnrichedMovieServiceSpec extends Specification {

  "fetchEnrichedMovie" should {

    "return movie enriched with star rating" in {

      val expectedMovie = Movie("badman", "nananana", Vector.empty[Review])
      val expectedMetascore = Metascore(100)

      val repo = (movieId: MovieId) => IO.pure(Some(expectedMovie))

      val starRatingsRepo = (movieName: String) => IO.pure(Some(expectedMetascore))

      val service = new FetchEnrichedMovieService(repo, starRatingsRepo)

      val actual = service.fetch(MovieId(123))

      actual.unsafeRunSync() must beSome(EnrichedMovie(expectedMovie, expectedMetascore))

    }

    "return error if star rating does not exist" in {

      val movie = Movie("badman", "nananana", Vector.empty[Review])

      val repo = (movieId: MovieId) => IO.pure(Some(movie))

      val starRatingsRepo = (movieName: String) => IO.pure(None)

      val service = new FetchEnrichedMovieService(repo, starRatingsRepo)

      val actual = service.fetch(MovieId(123))

      actual.unsafeRunSync() must throwA[EnrichmentFailure]

    }

  }

}
