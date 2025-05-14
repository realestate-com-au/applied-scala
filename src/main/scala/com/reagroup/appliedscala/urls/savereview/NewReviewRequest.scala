package com.reagroup.appliedscala.urls.savereview

import io.circe.Decoder
import io.circe.generic.semiauto._

case class NewReviewRequest(author: String, comment: String)

object NewReviewRequest {

  /**
    * Here is the Decoder instance.
    *
    * {
    *   "author": "Bob",
    *   "comment": "I liked this a lot"
    * }
    *
    * We can just use `deriveDecoder` because the keys in the incoming JSON are named exactly the same
    * as the fields in resulting data type.
    */

    given Decoder[NewReviewRequest] = deriveDecoder
}
