ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-streams-demo"
  )

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "3.2.0"
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "3.2.0" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.12"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.36"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.36"