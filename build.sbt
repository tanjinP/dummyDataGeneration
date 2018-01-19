name := "slick"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.zaxxer" % "HikariCP-java6" % "2.3.3"
)
        