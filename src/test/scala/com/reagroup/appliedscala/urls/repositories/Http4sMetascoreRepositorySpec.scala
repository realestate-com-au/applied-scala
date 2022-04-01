package com.reagroup.appliedscala.urls.repositories

import org.specs2.mutable.Specification
import cats.effect.IO
import cats.effect.kernel.Resource
import io.circe.Json
import io.circe.syntax._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import cats.effect.unsafe.implicits.global
import com.reagroup.appliedscala.urls.fetchenrichedmovie.Metascore

final class Http4sMetascoreRepositorySpec extends Specification with Http4sDsl[IO] {
  
  "Http4sMetascoreRepository" should {

    val validJson = Json.obj("name" -> "Encanto".asJson, "Metascore" -> 91.asJson)
    val client = stubClient(validJson)
    val apiKey = "Some api key"
    val repo = new Http4sMetascoreRepository(client, apiKey)

    "return Some if the metascore for the movie exists" in {
      val actual = repo("Encanto")
      actual.unsafeRunSync() must beSome(Metascore(91))

    }

    "return None if the metascore for the movie does not exist" in {
      val invalidJson = Json.obj("name" -> "Shark Tale".asJson)
      val client = stubClient(invalidJson)
      val apiKey = "Some api key"
      val repo = new Http4sMetascoreRepository(client, apiKey)
      val actual = repo("Shark Tale")

      actual.unsafeRunSync() must beNone
    }
  }

  private def stubClient(responseJson: Json): Client[IO] = {
    Client[IO](_ => Resource.make(Ok(responseJson))(_ => IO.unit))
  }

}
