package com.reagroup.appliedscala.urls.repositories

import cats.effect.IO
import com.reagroup.appliedscala.urls.fetchenrichedmovie.Metascore
import io.circe.parser.decode
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client

class Http4sMetascoreRepository(httpClient: Client[IO], apiKey: String) {

  /** For the purpose of this exercise, we return a `None` if we are unable
    * to decode a `Metascore` out of the response from OMDB.
    */
  def apply(movieName: String): IO[Option[Metascore]] = {
    val omdbURI: Uri = uri"http://www.omdbapi.com/"
      .withQueryParam("apikey", apiKey)
      .withQueryParam("t", movieName)
    val ioStr: IO[String] = httpClient.expect[String](omdbURI)
    ???
  }

}
