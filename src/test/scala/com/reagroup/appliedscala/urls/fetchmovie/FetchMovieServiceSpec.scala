package com.reagroup.appliedscala.urls.fetchmovie

import cats.data.NonEmptyList
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.models._
import org.specs2.mutable.Specification

class FetchMovieServiceSpec extends Specification {

  "fetchMovie" should {

    "return movie" in {

      val expectedMovie = Movie("badman", "nananana", Vector.empty[Review])

      val repo = (movieId: MovieId) => IO.pure(Some(expectedMovie))

      val service = new FetchMovieService(repo)

      val actual = service.fetch(MovieId(123))

      actual.unsafeRunSync() must beSome(expectedMovie)

    }

  }

}
