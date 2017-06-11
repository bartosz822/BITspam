name := """bit-spam"""
organization := "pl.edu.knbit"
version := "0.1"
scalaVersion := "2.11.11"

val playDependencies = {

  val scalaTestVersion = "2.0.0"

  Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestVersion % Test,
    ws
  )
}

val levelDBDependencies = {

  Seq(
    "org.iq80.leveldb" % "leveldb" % "0.7",
    "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
  )
}


val akkaDependencies = {

  val akkaVersion = "2.5.2"

  Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion
  )

}

val sparkDependencies = {

  val sparkMlVersion = "2.1.1"
  val sparkVersion = "2.1.1"

  Seq(
    "org.apache.spark" %% "spark-mllib" % sparkMlVersion,
    "org.apache.spark" %% "spark-core" % sparkVersion
  )


}


val jacksonDependencies = {

  val jacksonVersion = "2.6.5"

  Set(
    "com.fasterxml.jackson.module" % "jackson-module" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-core"  % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-annotations"  % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8"  % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"  % jacksonVersion
  )

}


libraryDependencies += filters
libraryDependencies ++= playDependencies
libraryDependencies ++= akkaDependencies
libraryDependencies ++= levelDBDependencies
libraryDependencies ++= sparkDependencies

dependencyOverrides ++= jacksonDependencies


lazy val root = (project in file(".")).enablePlugins(PlayScala)