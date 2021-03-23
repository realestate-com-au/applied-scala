addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

resolvers += "Flyway" at "https://davidmweber.github.io/flyway-sbt.repo"

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.5.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.17")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.10")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
