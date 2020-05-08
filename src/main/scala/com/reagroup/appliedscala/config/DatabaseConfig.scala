package com.reagroup.appliedscala.config

import cats.data.ValidatedNel
import cats.implicits._

case class DatabaseConfig(
  host: String,
  username: String,
  password: SensitiveValue[String],
  databaseName: String
)

object DatabaseConfig {
  def apply(env: Environment): ValidatedNel[ConfigError, DatabaseConfig] = {
    val host = env.required("DATABASE_HOST")
    val username = env.required("DATABASE_USERNAME")
    val password = env.required("DATABASE_PASSWORD").map(SensitiveValue.apply)
    val databaseName = env.required("DATABASE_NAME")
    (host, username, password, databaseName).mapN(DatabaseConfig.apply)
  }
}
