lazy val root = (project in file(".")).
  settings(
    name := "Insight Challenge",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % "2.4.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.2" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "io.spray" %%  "spray-json" % "1.3.2",
  "com.github.nscala-time" %% "nscala-time" % "2.12.0"
)
//  "net.liftweb" %% "lift-json" % "2.6"
