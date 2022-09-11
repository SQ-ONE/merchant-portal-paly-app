ThisBuild / organization := "com.squareoneinsights.merchantportal"
ThisBuild / version := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
ThisBuild / scalaVersion := "2.13.8"

// Workaround for scala-java8-compat issue affecting Lagom dev-mode
// https://github.com/lagom/lagom/issues/3344
ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % Test

lazy val `marchant-portal-app` = (project in file("."))
  .aggregate(`marchant-portal-app-api`, `marchant-portal-app-impl`, `marchant-portal-app-stream-api`, `marchant-portal-app-stream-impl`)

lazy val `marchant-portal-app-api` = (project in file("marchant-portal-app-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `marchant-portal-app-impl` = (project in file("marchant-portal-app-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`marchant-portal-app-api`)

lazy val `marchant-portal-app-stream-api` = (project in file("marchant-portal-app-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `marchant-portal-app-stream-impl` = (project in file("marchant-portal-app-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`marchant-portal-app-stream-api`, `marchant-portal-app-api`)
