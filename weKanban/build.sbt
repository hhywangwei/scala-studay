jetty()

libraryDependencies ++= Seq(
    "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided",
    "org.scalaz" %% "scalaz-core" % "6.0.4",
    "org.scalaz" %% "scalaz-http" % "6.0.4",
    "com.h2database" % "h2" % "1.2.137",
    "org.squeryl" % "squeryl_2.10" % "0.9.5-6"
 )