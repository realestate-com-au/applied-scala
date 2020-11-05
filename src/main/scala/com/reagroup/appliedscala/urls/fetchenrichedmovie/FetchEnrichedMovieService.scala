package com.reagroup.appliedscala.urls.fetchenrichedmovie

import cats.effect.IO
import com.reagroup.appliedscala.models._
import cats.implicits._

class FetchEnrichedMovieService(fetchMovie: MovieId => IO[Option[Movie]],
                                fetchMetascore: MovieName => IO[Option[Metascore]]) {

  /**
    * In order to construct an `EnrichedMovie`, we need to first get a `Movie` and a `Metascore`.
    * We can do so using the functions that are passed in as dependencies.
    *
    * For the purpose of this exercise, let's raise an `EnrichmentFailure` if the `Metascore` does not exist.
    *
    * Hint: We know we are going to be chaining multiple effects in `IO` so let's start a for-comprehension.
    * Also pattern match on `Option` if you're stuck!
    **/

  // Fetch implementation without using the private method enrichMovieWithMetascore
//  def fetch(movieId: MovieId): IO[Option[EnrichedMovie]] = {
//    for {
//      optMovie <- fetchMovie(movieId)
//      result <- optMovie match {
//        case None => IO.pure(None)
//        case Some(movie: Movie) => fetchMetascore(movie.name).flatMap(optMetascore => {
//          optMetascore match {
//            case None => IO.raiseError(EnrichmentFailure(movie.name))
//            case Some(metascore: Metascore) => IO.pure(Some(EnrichedMovie(movie, metascore)))
//          }
//        })
//      }
//    } yield result
//  }

  def fetch(movieId: MovieId): IO[Option[EnrichedMovie]] = {
    for {
      optMovie <- fetchMovie(movieId)
      result <- optMovie match {
        case None => IO.apply(None)
        case Some(movie: Movie) => enrichMovieWithMetascore(movie).map(enrichedMovie => Some(enrichedMovie))
      }
    } yield result
  }

  /**
    * Given a `Movie`, we can call `fetchMetascore` using the `name` of the `Movie`.
    * If no `Metascore` is found, raise an `EnrichmentFailure` using `IO.raiseError`.
    **/
  private def enrichMovieWithMetascore(movie: Movie): IO[EnrichedMovie] = {
    for {
      optMetascore <- fetchMetascore(movie.name)
      result <- optMetascore match {
        case Some(metascore: Metascore) => IO.apply(EnrichedMovie(movie, metascore))
        case None => IO.raiseError(EnrichmentFailure(movie.name))
      }
    } yield result
  }

}
