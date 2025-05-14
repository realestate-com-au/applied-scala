package com.reagroup.appliedscala.urls.savemovie

import cats.data.NonEmptyList
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import com.reagroup.appliedscala.models.*
import com.reagroup.appliedscala.urls.savemovie.MovieValidationError.*
import io.circe.literal.*
import org.http4s.*
import org.specs2.mutable.Specification

class SaveMovieControllerSpec extends Specification {

  "when saving a valid movie" should {

    val json =
      json"""
        {
          "name": "Jurassic Park",
          "synopsis": "Why does pterodactyl start with a p?"
        }
      """

    val request = Request[IO](method = Method.POST).withEntity(json.noSpaces)

    val controller = new SaveMovieController((_: NewMovieRequest) => IO.pure(MovieId(1).valid))

    val actual = controller.save(request).unsafeRunSync()

    "return status code Created" in {

      actual.status must beEqualTo(Status.Created)

    }

    "return movieId in response body" in {

      val expectedJson =
        json"""
          { "id": 1 }
        """
      actual.as[String].unsafeRunSync() must beEqualTo(expectedJson.noSpaces)

    }

  }

  "when saving an invalid movie" should {

    val invalidJson =
      json"""
        {
          "name": "",
          "synopsis": ""
        }
      """

    val request = Request[IO](method = Method.POST).withEntity(invalidJson.noSpaces)

    val saveNewMovie = (_: NewMovieRequest) => IO.pure(NonEmptyList.of(MovieNameTooShort, MovieSynopsisTooShort).invalid)

    val controller = new SaveMovieController(saveNewMovie)

    val actual = controller.save(request).unsafeRunSync()

    "return status code BadRequest" in {

      actual.status must beEqualTo(Status.BadRequest)

    }

    "return errors in response body" in {

      val expectedJson =
        json"""
          [ { "error": "MOVIE_NAME_TOO_SHORT" }, { "error": "MOVIE_SYNOPSIS_TOO_SHORT"} ]
        """
      actual.as[String].unsafeRunSync() must beEqualTo(expectedJson.noSpaces)

    }

  }

}
