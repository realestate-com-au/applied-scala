package com.reagroup.appliedscala.urls.fetchenrichedmovie

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor}
import io.circe.generic.semiauto._

case class Metascore(value: Int)

object Metascore {

  implicit val metascoreDecoder: Decoder[Metascore] = Decoder.instance(cursor => {
    for {
      metascore <- cursor.downField("Metascore").as[Int]
    } yield Metascore(metascore)
  })

// Another decoder implementation
//    override def apply(c: HCursor): Result[Metascore] = {
//      for {
//        metascore <- c.downField("Metascore").as[Int]
//
//      } yield Metascore(metascore)
//    }




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

}
