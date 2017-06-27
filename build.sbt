name := """bit-spam"""
organization := "pl.edu.knbit"
version := "0.1"
scalaVersion := "2.11.11"


// TODO: move all those dependencies to a separate `Dependencies` scala object
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

  val akkaVersion = "2.4.16"

  Seq(
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion
  )

}

val reactiveMongoDependencies = {

  val reactiveMongoVersion = "0.11.14"

  Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion
  )

}

libraryDependencies += filters
libraryDependencies ++= playDependencies
libraryDependencies ++= akkaDependencies
libraryDependencies ++= levelDBDependencies
libraryDependencies ++= reactiveMongoDependencies


lazy val root = (project in file(".")).enablePlugins(PlayScala)