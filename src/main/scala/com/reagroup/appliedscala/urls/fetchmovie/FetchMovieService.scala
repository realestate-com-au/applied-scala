package com.reagroup.appliedscala.urls.fetchmovie

import cats.effect.IO
import com.reagroup.appliedscala.models._

class FetchMovieService(fetchMovie: MovieId => IO[Option[Movie]]) {

  /**
    * This one is real easy!
    *
    * We have a `MovieId` and we want to yield a `IO[Option[Movie]]` and we have precisely the function that can do this for us in scope.
    */
  def fetch(movieId: MovieId): IO[Option[Movie]] =
    fetchMovie(movieId)

}
