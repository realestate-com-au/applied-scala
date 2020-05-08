package com.reagroup.appliedscala.urls.savereview

import io.circe.Decoder
import io.circe.generic.semiauto._

case class NewReviewRequest(author: String, comment: String)

object NewReviewRequest {

  /**
    * Add a Decoder instance here to decode the following Json:
    *
    * {
    *   "author": "Bob",
    *   "comment": "I liked this a lot"
    * }
    */

    implicit val decoder: Decoder[NewReviewRequest] = deriveDecoder[NewReviewRequest]
}
