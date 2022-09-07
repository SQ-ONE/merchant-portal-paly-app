name := """marchant-portal-app"""
organization := "com.squareoneinsights.merchantportal"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

scalaVersion in ThisBuild := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.typelevel" %% "cats-core" % "1.4.0",
  "com.oracle.ojdbc" % "ojdbc8" % "19.3.0.0",
  "org.pac4j" %% "lagom-pac4j" % "2.0.0",
  "org.pac4j" % "pac4j-http" % "3.6.1",
  "org.pac4j" % "pac4j-jwt" % "3.6.1",
  "com.pauldijou" %% "jwt-play-json" % "0.12.1",
  "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1",
  "joda-time" % "joda-time" % "2.9.9",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "net.logstash.logback" % "logstash-logback-encoder" % "5.2",
  "com.typesafe.play" %% "play-slick" % "4.0.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  guice,
  "org.apache.kafka" % "kafka-clients" % "0.10.0.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.squareoneinsights.merchantportal.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.squareoneinsights.merchantportal.binders._"
