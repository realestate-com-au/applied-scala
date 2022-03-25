package com.reagroup.appliedscala

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.{Method, Request, Response, Uri}
import org.typelevel.ci.CIString

object Http4sSpecHelpers {
  def header(response: IO[Response[IO]], headerName: String): Option[String] = {
    response.unsafeRunSync().headers.get(CIString(headerName)).map(_.head.value)
  }

  def body(response: IO[Response[IO]]): String = {
    val bytes = response.unsafeRunSync().body.compile.toVector.unsafeRunSync()
    new String(bytes.toArray, "utf-8")
  }

  def request(path: String, method: Method): Request[IO] = {
      Request[IO](method = method, uri = Uri(path = Uri.Path.unsafeFromString(path)))
  }

  def request(uri: Uri, method: Method): Request[IO] = {
      Request[IO](method = method, uri = uri)
  }
}
