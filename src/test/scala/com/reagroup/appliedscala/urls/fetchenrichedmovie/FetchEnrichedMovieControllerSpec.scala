package com.reagroup.appliedscala.urls.fetchenrichedmovie

import cats.effect.IO
import com.reagroup.appliedscala.models._
import io.circe.literal._
import org.http4s._
import org.http4s.testing.Http4sMatchers
import org.http4s.testing.IOMatchers
import org.specs2.mutable.Specification

class FetchEnrichedMovieControllerSpec
    extends Specification
    with Http4sMatchers[IO]
    with IOMatchers {

  "when fetching a movie that exists" should {

    val expectedMovie = EnrichedMovie(
      Movie("badman", "the first in the series", Vector.empty),
      Metascore(100)
    )

    val fetchMovie = (_: MovieId) => IO.pure(Some(expectedMovie))

    val controller = new FetchEnrichedMovieController(fetchMovie)

    val actual = controller.fetch(123).unsafeRunSync()

    "return status code OK" in {

      actual must haveStatus(Status.Ok)

    }

    "return enriched movie in response body" in {

      val expectedJson =
        json"""
          {
            "name": "badman",
            "synopsis": "the first in the series",
            "reviews": [],
            "metascore": 100
          }
        """
      actual must haveBody(expectedJson.noSpaces)

    }

  }

  "when fetching a movie that does not exist" should {

    val fetchEnrichedMovie = (_: MovieId) => IO.pure(None)

    val controller = new FetchEnrichedMovieController(fetchEnrichedMovie)

    val actual = controller.fetch(123).unsafeRunSync()

    "return status code NotFound" in {

      actual must haveStatus(Status.NotFound)

    }

  }

  "when encountered an error" should {

    val fetchEnrichedMovie =
      (_: MovieId) => IO.raiseError(new RuntimeException("unknown error"))

    val controller = new FetchEnrichedMovieController(fetchEnrichedMovie)

    val actual = controller.fetch(123).unsafeRunSync()

    "return status code InternalServerError" in {

      actual must haveStatus(Status.InternalServerError)

    }

    "return error message in response body" in {

      val expectedJson =
        json"""
            { "error": "Unexpected error has occurred: unknown error" }
          """
      actual must haveBody(expectedJson.noSpaces)

    }

  }

}
