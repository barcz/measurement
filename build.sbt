name := "measurements"

version := "0.1"

scalaVersion := "2.13.6"

val AkkaVersion = "2.6.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "co.fs2" %% "fs2-io" % "3.1.2",
  "org.scalameta" %% "munit" % "0.7.29" % Test
)
