addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4")

resolvers += "Flyway" at "https://davidmweber.github.io/flyway-sbt.repo"

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.17")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.10")
