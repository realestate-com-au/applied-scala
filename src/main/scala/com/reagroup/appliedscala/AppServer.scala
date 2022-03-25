package com.reagroup.appliedscala

import cats.effect.{ExitCode, IO}
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder

class AppServer(port: Int, service: HttpApp[IO]) {

  def start(): IO[Unit] = {
    BlazeServerBuilder[IO]
      .bindHttp(port, "0.0.0.0")
      .withHttpApp(service)
      .serve
      .compile
      .drain
  }

}
