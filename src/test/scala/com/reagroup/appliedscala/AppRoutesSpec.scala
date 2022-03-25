package com.reagroup.appliedscala

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.reagroup.appliedscala.Http4sSpecHelpers._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.specs2.mutable.Specification
import org.specs2.specification.core.Fragment

class AppRoutesSpec extends Specification with Http4sDsl[IO] {

  /*
   * We can test our routes independently of our controllers.
   *
   * Here we create the AppRoutes object using functions defined in this test,
   * instead of using the "real" controllers.
   */

  private val testAppRoutes = new AppRoutes(
    fetchAllMoviesHandler = fetchAllMovies,
    fetchMovieHandler = fetchMovie,
    fetchEnrichedMovieHandler = fetchEnrichedMovie,
    saveMovieHandler = saveMovie,
    saveReviewHandler = saveReview
  )

  /*
   * The functions standing in for the controllers can be very simple.
   *
   * Their purpose is to return a distinctive response, so we can
   * confirm that AppRoutes delegates to the right controller for each route.
   *
   * They can also check whether or not the pattern matchers in the route pattern
   * extracted parameters from the URL correctly.
   */

  def fetchAllMovies: IO[Response[IO]] = Ok("great titles")

  def fetchMovie(rawMovieId: Long): IO[Response[IO]] = {
    if (rawMovieId == 123) {
      Ok("got movie 123")
    } else {
      errorResponse(s"Expected movieId 123, but received $rawMovieId")
    }
  }

  def fetchEnrichedMovie(rawMovieId: Long): IO[Response[IO]] = {
    if (rawMovieId == 123) {
      Ok("got enriched movie 123")
    } else {
      errorResponse(s"Expected movieId 123, but received $rawMovieId")
    }
  }

  def saveMovie(req: Request[IO]): IO[Response[IO]] = Ok("movie 456 created")

  def saveReview(rawMovieId: Long, req: Request[IO]): IO[Response[IO]] = {
    if (rawMovieId == 456) {
      Ok("review 7 for movie 456 created")
    } else {
      errorResponse(s"Expected movieId 456, but received $rawMovieId")
    }
  }

  def errorResponse(message: String): IO[Response[IO]] = {
    IO.pure(Response(status = InternalServerError.withReason(message)))
  }

  "AppRoutes" should {
    val endpoints: List[(Request[IO], String)] = List(
      request(path = "/movies", method = Method.GET) -> "great titles",
      request(path = "/movies/123", method = Method.GET) -> "got movie 123",
      request(uri = uri"/movies/123?enriched=true", method = Method.GET) -> "got enriched movie 123",
      request(path = "/movies", method = Method.POST) -> "movie 456 created",
      request(path = "/movies/456/reviews", method = Method.POST) -> "review 7 for movie 456 created"
    )

    Fragment.foreach(endpoints) { endpoint =>
      val (req, expectedResponse) = endpoint

      s"for ${req.method} ${req.uri}" in {

        "return OK" in {
          testAppRoutes.openRoutes(req).getOrElse(Response[IO](status = Status.NotFound)).unsafeRunSync().status must beEqualTo(Status.Ok)
        }

        "return expected response" in {
          body(testAppRoutes.openRoutes(req).getOrElse(Response[IO](Status.NotFound))) must beEqualTo(expectedResponse)
        }
      }
    }
  }

}

