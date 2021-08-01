name := """todo"""
organization := "io.github.uazw"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.github.uazw.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.github.uazw.binders._"
