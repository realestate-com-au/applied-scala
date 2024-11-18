package com.reagroup.appliedscala.urls.fetchenrichedmovie

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor}
import io.circe.generic.semiauto._

case class Metascore(value: Int)

object Metascore {

  /**
    * Add a Decoder instance here to decode a JSON containing "Metascore" into a `Metascore`, e.g.
    *
    * Convert:
    *
    * {
    *   ..
    *   ..
    *   "Metascore": "75",
    *   ..
    *   ..
    * }
    *
    * into:
    *
    * `Metascore(75)`
    */
    implicit val decoderMetascore: Decoder[Metascore] = Decoder { metascore => ??? }
}