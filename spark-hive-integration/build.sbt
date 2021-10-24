name := "spark-hive-integration"
organization := "com.ranga.spark.hive"
description   := "Spark Hive Integration Project"
version := "1.0.0-SNAPSHOT"

developers := List(
    Developer(
        id    = "rangareddy",
        name  = "Ranga Reddy",
        email = "rangareddy.avula@gmail.com",
        url   = url("https://github.com/rangareddy")
    )
)

publishMavenStyle := true
autoScalaLibrary := false
scalaVersion := "2.11.12"
val scalaBinaryVersion = "2.11"
val javaVersion = "1.8"
val scalaTestVersion = "3.0.8"
val junitTestVersion = "4.13.1"
val sparkVersion = "2.4.0.7.1.6.0-297"
val sparkScope = "provided"
val testScope = "test"

resolvers ++= Seq(
    "scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
    "central" at "https://repo1.maven.org/maven2",
    "cloudera-repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)

// Spark + Other Dependencies
lazy val appDependencies = Seq(
    "org.apache.spark" % s"spark-core_${scalaBinaryVersion}" % sparkVersion % sparkScope,
    "org.apache.spark" % s"spark-sql_${scalaBinaryVersion}" % sparkVersion % sparkScope,
    "org.apache.spark" % s"spark-hive_${scalaBinaryVersion}" % sparkVersion % sparkScope
)

// Test Dependencies
lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion  % testScope,
    "junit" % "junit" % junitTestVersion % testScope
)

libraryDependencies ++= appDependencies ++ testDependencies

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
    artifact.name + "-" + module.revision + "." + artifact.extension
}

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))