name := "applied-scala"

version := "1.0"

scalaVersion := "2.12.8"

mainClass := Some("com.reagroup.appliedscala.Main")

val catsVersion = "1.6.0"
val circeVersion = "0.11.1"
val http4sVersion = "0.20.1"
val postgresqlVersion = "42.2.4"
val doobieVersion = "0.5.3"
val specs2Version = "4.5.1"
val newrelicVersion = "5.0.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  // for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic",
  // for string interpolation to JSON model
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "org.typelevel"           %% "cats-core"              % catsVersion,
  "org.http4s"              %% "http4s-blaze-server"    % http4sVersion,
  "org.http4s"              %% "http4s-blaze-client"    % http4sVersion,
  "org.http4s"              %% "http4s-circe"           % http4sVersion,
  "org.http4s"              %% "http4s-dsl"             % http4sVersion,
  "org.postgresql"           % "postgresql"             % postgresqlVersion,
  "org.tpolecat"            %% "doobie-core"            % doobieVersion,
  "org.tpolecat"            %% "doobie-postgres"        % doobieVersion,
  "com.newrelic.agent.java"  % "newrelic-api"           % newrelicVersion,
  "org.specs2"              %% "specs2-core"            % specs2Version % "test",
  "org.specs2"              %% "specs2-matcher-extra"   % specs2Version % "test",
  "org.specs2"              %% "specs2-scalacheck"      % specs2Version % "test",
  "org.http4s"              %% "http4s-testing"         % http4sVersion % "test",
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:higherKinds",
  "-Ypartial-unification"
)

testFrameworks := Seq(TestFrameworks.Specs2)
