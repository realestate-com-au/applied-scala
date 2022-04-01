package com.reagroup.appliedscala.urls.fetchenrichedmovie

import org.specs2.mutable.Specification
import io.circe.syntax._
import io.circe.Json

final class MetascoreSpec extends Specification {

  "A Metascore decoder" should {

    "convert valid Json to Metascore" in {
      val json = Json.obj("name" -> "Some movie".asJson,  "Metascore" -> 75.asJson)
      val errOrMetascore = json.as[Metascore]

      errOrMetascore must_=== Right(Metascore(75))
    }

    "convert invalid Json to error" in {
      val json = Json.obj("foo" -> "bar".asJson)
      val errOrMetascore = json.as[Metascore]
      errOrMetascore must beLeft
    }
  }  

}

