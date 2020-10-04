package com.reagroup.appliedscala

import cats.data.Kleisli
import cats.effect.IO
import cats.effect.ContextShift
import cats.effect.Timer
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.config.Config
import com.reagroup.appliedscala.urls.repositories.{Http4sMetascoreRepository, PostgresqlRepository}
import com.reagroup.appliedscala.urls.fetchallmovies.{FetchAllMoviesController, FetchAllMoviesService}
import com.reagroup.appliedscala.urls.fetchenrichedmovie.{FetchEnrichedMovieController, FetchEnrichedMovieService}
import com.reagroup.appliedscala.urls.fetchmovie.{FetchMovieController, FetchMovieService}
import com.reagroup.appliedscala.urls.savemovie.{SaveMovieController, SaveMovieService}
import com.reagroup.appliedscala.urls.savereview.{SaveReviewController, SaveReviewService}
import org.http4s._
import org.http4s.client.Client

class AppRuntime(config: Config, httpClient: Client[IO], contextShift: ContextShift[IO], timer: Timer[IO]) {

  /**
   * This is the repository that talks to Postgresql
   */
  private val pgsqlRepo = PostgresqlRepository(config.databaseConfig, contextShift)

  private val http4sMetascoreRepo = new Http4sMetascoreRepository(httpClient, config.omdbApiKey)

  /**
   * This is where we instantiate our `Service` and `Controller` for each endpoint.
   * You will need to implement a similar block for the `fetchmovies` endpoint when you work on it later.
   * The rest of the endpoints have been completed for you.
   */
  private val fetchAllMoviesController: FetchAllMoviesController = {
    val fetchAllMoviesService: FetchAllMoviesService = new FetchAllMoviesService(pgsqlRepo.fetchAllMovies)
    new FetchAllMoviesController(fetchAllMoviesService.fetchAll)
  }

  /**
   * Construct a `FetchMovieController` by first constructing a `FetchMovieService` using the `pgsqlRepo` from above.
   * You need to use the `new` keyword in order to call the constructor of a class.
   * Refer to the construction of `fetchAllMoviesController` as a hint.
   *
   * After constructing a `FetchMovieController`, pass it into `AppRoutes` at the bottom of this file to get it
   * all wired up correctly.
   */
  private val fetchMovieController: FetchMovieController = {
    ???
  }

  private val fetchEnrichedMovieController: FetchEnrichedMovieController = {
    val fetchEnrichedMovieService = new FetchEnrichedMovieService(pgsqlRepo.fetchMovie, http4sMetascoreRepo.apply)
    new FetchEnrichedMovieController(fetchEnrichedMovieService.fetch)
  }

  private val saveMovieController: SaveMovieController = {
    val saveMovieService = new SaveMovieService(pgsqlRepo.saveMovie)
    new SaveMovieController(saveMovieService.save)
  }

  private val saveReviewController: SaveReviewController = {
    val saveReviewService = new SaveReviewService(pgsqlRepo.saveReview, pgsqlRepo.fetchMovie)
    new SaveReviewController(saveReviewService.save)
  }

  private val appRoutes = new AppRoutes(
    fetchAllMoviesHandler = fetchAllMoviesController.fetchAll,
    fetchMovieHandler = _ => IO(Response[IO](status = Status.NotImplemented)), // Fill this out after constructing `FetchMovieController`
    fetchEnrichedMovieHandler = fetchEnrichedMovieController.fetch,
    saveMovieHandler = saveMovieController.save,
    saveReviewHandler = saveReviewController.save
  )

  /*
   * All routes that make up the application are exposed by AppRuntime here.
   */
  def routes: HttpApp[IO] = HttpApp((req: Request[IO]) => appRoutes.openRoutes(req).getOrElse(Response[IO](status = Status.NotFound)))

}
