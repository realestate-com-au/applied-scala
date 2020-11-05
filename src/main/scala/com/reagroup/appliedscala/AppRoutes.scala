package com.reagroup.appliedscala

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl

/**
 * The `AppRoutes` class defines the routes for our app.
 */
class AppRoutes(fetchAllMoviesHandler: IO[Response[IO]],
                fetchMovieHandler: Long => IO[Response[IO]],
                fetchEnrichedMovieHandler: Long => IO[Response[IO]],
                saveMovieHandler: Request[IO] => IO[Response[IO]],
                saveReviewHandler: (Long, Request[IO]) => IO[Response[IO]]) extends Http4sDsl[IO] {

  /**
   * This Matcher is used to find a query parameter called `enriched` that can be set to `true` or `false`
   * e.g. myurl.com/movies?enriched=true
   * It will be used when working through the `fetchenrichedmovie` package.
   */
  object OptionalEnrichedMatcher extends OptionalQueryParamDecoderMatcher[Boolean]("enriched")

  val openRoutes: HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "movies" => fetchAllMoviesHandler
    case GET -> Root / "movies" / LongVar(id) => fetchMovieHandler(id)
    case req @ POST -> Root / "movies" => saveMovieHandler(req)
    case req @ POST -> Root / "movies" / LongVar(id) / "reviews" => saveReviewHandler(id, req)
  }

}
