enablePlugins(UniversalPlugin)

import java.util.jar.Attributes.Name.CLASS_PATH
import NativePackagerHelper._

val NewRelicConfig = config("newrelic")

ivyConfigurations += NewRelicConfig

libraryDependencies += "com.newrelic.agent.java" % "newrelic-agent" % "4.11.0" % "newrelic"

maintainer := "Applied Scala course team"

topLevelDirectory in Universal := None

packageOptions += Package.ManifestAttributes(
  (CLASS_PATH, (managedClasspath in Compile).value.map(_.data.getName).mkString(" "))
)

mappings in Universal ++= {
  val Vector(newrelicJar) = update.value.select(configurationFilter("newrelic"))

  (((packageBin in Compile).value, "app.jar") +:
  (newrelicJar, "newrelic.jar") +:
  contentOf("support/prod")) ++
  fromClasspath((managedClasspath in Compile).value, ".")
}
