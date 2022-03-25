package com.reagroup.appliedscala

import cats.effect.{IO, IOApp}
import com.reagroup.appliedscala.config.Config
import org.http4s.blaze.client.BlazeClientBuilder

object Main extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      _ <- IO(println("Starting server"))
      _ <- startServer()
    } yield ()
  }

  private def runServerWith(config: Config): IO[Unit] = {
    BlazeClientBuilder[IO].resource.use { httpClient =>
      val app = new AppRuntime(config, httpClient).routes
      new AppServer(9200, app).start()
    }
  }

  private def startServer(): IO[Unit] = {
    for {
      config <- Config.fromEnvironment()
      _ <- runServerWith(config)
    } yield ()
  }

}
