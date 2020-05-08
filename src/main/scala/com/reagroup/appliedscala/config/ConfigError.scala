package com.reagroup.appliedscala.config

sealed abstract class ConfigError {
  def message: String
}

object ConfigError {
  def show(errors: Iterable[ConfigError]): String = {
    errors.map(_.message).mkString("Configuration errors:\n  ", "\n  ", "")
  }
}

case class MissingEnvironmentVariable(name: String) extends ConfigError {
  def message: String = s"Missing environment variable: $name"
}
