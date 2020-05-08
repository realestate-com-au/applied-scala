package com.reagroup.appliedscala

import cats.effect.{ContextShift, ExitCode, IO, Timer}
import cats.implicits._
import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder

class AppServer(port: Int, service: HttpApp[IO]) {

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
  def start()(implicit contextShift: ContextShift[IO], timer: Timer[IO]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(port, "0.0.0.0")
      .withHttpApp(service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
