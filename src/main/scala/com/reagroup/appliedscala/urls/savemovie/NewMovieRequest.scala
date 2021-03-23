package com.reagroup.appliedscala.urls.savemovie

import io.circe.Decoder
import io.circe.generic.semiauto._

case class NewMovieRequest(name: String, synopsis: String)

object NewMovieRequest {

  /** Here is the Decoder instance.
    *
    * {
    *   "name": "Titanic",
    *   "synopsis": "A movie about ships"
    * }
    *
    * We can just use `deriveDecoder` because the keys in the incoming JSON are named exactly the same
    * as the fields in resulting data type.
    */

  implicit val decoder: Decoder[NewMovieRequest] = deriveDecoder

}
