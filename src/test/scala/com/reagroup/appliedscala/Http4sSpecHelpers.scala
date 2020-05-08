package com.reagroup.appliedscala

import cats.effect.IO
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Method, Query, Request, Response, Uri}

object Http4sSpecHelpers {
  def header(response: IO[Response[IO]], headerName: String): Option[String] = {
    response.unsafeRunSync().headers.get(CaseInsensitiveString(headerName)).map(_.value)
  }

  def body(response: IO[Response[IO]]): String = {
    val bytes = response.unsafeRunSync().body.compile.toVector.unsafeRunSync()
    new String(bytes.toArray, "utf-8")
  }

  def request(path: String, method: Method): Request[IO] = {
      Request[IO](method = method, uri = Uri(path = path))
  }

  def request(uri: Uri, method: Method): Request[IO] = {
      Request[IO](method = method, uri = uri)
  }
}
