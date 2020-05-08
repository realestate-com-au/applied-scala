package com.reagroup.appliedscala

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.ExitCode
import com.reagroup.appliedscala.config.Config
import java.util.concurrent.Executors
import org.http4s.client.blaze._
import org.http4s.client._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val server = startServer()

    println("Starting server")

    server
  }

  private def runServerWith(config: Config): IO[ExitCode] = {
    BlazeClientBuilder[IO](global).resource.use { httpClient =>
      val app = new AppRuntime(config, httpClient, contextShift, timer).routes
      new AppServer(9200, app).start()
    }
  }

  def startServer(): IO[ExitCode] = {
    for {
      config <- Config.fromEnvironment()
      process <- runServerWith(config)
    } yield process
  }

}
