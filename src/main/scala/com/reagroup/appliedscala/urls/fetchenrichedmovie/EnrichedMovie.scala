package com.reagroup.appliedscala.urls.fetchenrichedmovie

import com.reagroup.appliedscala.models.Movie
import io.circe.syntax._
import io.circe.{Encoder, Json}

case class EnrichedMovie(movie: Movie, metascore: Metascore)

object EnrichedMovie {

  implicit val enrichedMovieEncoder: Encoder[EnrichedMovie] = new Encoder[EnrichedMovie] {
    override def apply(enrichedMovie: EnrichedMovie): Json = {
      Json.obj(
        ("name", enrichedMovie.movie.name.asJson),
        ("synopsis", enrichedMovie.movie.synopsis.asJson),
        ("reviews", enrichedMovie.movie.reviews.asJson),
        ("metascore", enrichedMovie.metascore.value.asJson)
      )
    }
  }
  /**
    * Add an Encoder instance here
    *
    * We want the Json to look like:
    *
    * {
    *   "name": "Batman",
    *   "synopsis": "Great movie for the family",
    *   "reviews": []
    *   "metascore": 75
    * }
    *
    * not:
    *
    * {
    *   "movie": {
    *     "name": "Batman",
    *     "synopsis": "Great movie for the family",
    *     "reviews": []
    *   },
    *   "metascore": 75
    * }
    *
    * which is what we would get if we used `deriveEncoder[EnrichedMovie]`
    *
    * Hint: You will need to create a custom encoder (see how we did it for `MovieId`).
    */

}
