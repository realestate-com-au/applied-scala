package com.reagroup.appliedscala.config

import cats.data.Validated.{Invalid, Valid}
import cats.data.ValidatedNel
import cats.effect.IO
import cats.implicits._

case class Config(
  omdbApiKey: String,
  version: String,
  databaseConfig: DatabaseConfig
)

object Config {
  def apply(environment: Environment): ValidatedNel[ConfigError, Config] = {
    val omdbApiKey = environment.required("OMDB_API_KEY")
    val version = environment.optional("VERSION", "(unknown)")
    val databaseConfig = DatabaseConfig(environment)
    (omdbApiKey, version, databaseConfig).mapN(Config.apply)
  }

  def fromEnvironment(): IO[Config] = {
    val env = Environment(sys.env)
    Config(env) match {
      case Invalid(errors) => IO.raiseError(new IllegalStateException(ConfigError.show(errors.toList)))
      case Valid(c) => IO.pure(c)
    }
  }
}
