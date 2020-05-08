package com.reagroup.appliedscala.urls.fetchallmovies

import cats.effect.IO
import com.reagroup.appliedscala.models._

class FetchAllMoviesService(fetchAllMovies: IO[Vector[Movie]]) {

  def fetchAll: IO[Vector[Movie]] = fetchAllMovies

}
