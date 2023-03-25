ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

//lazy val root = (project in file("."))
//  .settings(
//    name := "zio_project"
//  )

lazy val zioVersion = "2.0.7"
val zioSqlVersion = "0.1.1"
val zioConfigVersion = "3.0.7"
val zioHttpVersion = "0.0.4"

val testcontainersVersion = "1.16.2"
val testcontainersScalaVersion = "0.39.12"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion,
  "dev.zio" %% "zio-http" % zioHttpVersion,
  "dev.zio" %% "zio-json" % "0.3.0-RC8",
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-sql-postgres" % zioSqlVersion,
  "dev.zio" %% "zio-direct" % "1.0.0-RC6",

  // config
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,

// test dependencies
  "dev.zio" %% "zio-test" % zioVersion % Test,
  "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
  "dev.zio" %% "zio-test-junit" % zioVersion % Test,
  "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % Test,
  "org.testcontainers" % "testcontainers" % testcontainersVersion % Test,
  "org.testcontainers" % "database-commons" % testcontainersVersion % Test,
  "org.testcontainers" % "postgresql" % testcontainersVersion % Test,
  "org.testcontainers" % "jdbc" % testcontainersVersion % Test

  //  "dev.zio" %% "zio-schema" % "0.4.8"
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
