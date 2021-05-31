name := "untitled"

version := "0.1"

scalaVersion := "2.13.5"

lazy val javaFXModules = {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _                            =>
      throw new Exception("Unknown platform!")
  }
  // Create dependencies for JavaFX modules
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map( m=> "org.openjfx" % s"javafx-$m" % "15.0.1" classifier osName)
}

libraryDependencies ++= javaFXModules

libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.5.32"

libraryDependencies +=
  "org.scalafx" %% "scalafx" % "15.0.1-R21"

libraryDependencies +=
  "com.typesafe.akka" %% "akka-testkit" % "2.6.14" % Test

libraryDependencies +=
  "org.scalatest" %% "scalatest" % "3.2.9" % Test