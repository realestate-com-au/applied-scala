package com.reagroup.appliedscala.models

import io.circe.Encoder
import io.circe.Json

case class MovieId(value: Long)

object MovieId {

  /**
    * Here is the Encoder instance.
    *
    * We want the resulting Json to look like this:
    *
    * {
    *   "id": 1
    * }
    *
    * We don't want to use `deriveEncoder` here, otherwise the resulting JSON key will be tied to the name
    * of the field in `MovieId`
    */

  implicit val encoder: Encoder[MovieId] = Encoder.forProduct1("id")(_.value)
}
