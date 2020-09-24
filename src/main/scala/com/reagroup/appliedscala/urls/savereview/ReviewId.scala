package com.reagroup.appliedscala.urls.savereview

import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

case class ReviewId(value: Long)

object ReviewId {

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
    * of the field in `ReviewId`
    */

  implicit val encoder: Encoder[ReviewId] =
    Encoder { id =>
      Json.obj("id" -> id.value.asJson)
    }

}
