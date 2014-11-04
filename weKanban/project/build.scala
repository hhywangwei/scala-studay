import sbt._
import Keys._

object H2TaskManager {
  var process: Option[Process] = None
  lazy val H2 = config("h2") extend(Compile)

  val startH2 = TaskKey[Unit]("startH2", "Start H2 database")
  val startH2Task = startH2 in H2 <<= (fullClasspath in Compile) map{
    cp =>
    startDatabase {
      cp.map(_.data)
        .map(_.getAbsolutePath())
        .filter(_.contains("h2database"))
    }
  }

  def startDatabase(paths:Seq[String]) = {
    process match {
      case None =>
        val cp = paths.mkString(System.getProperty("path.separator"))
        val command = "java -cp "  + cp + " org.h2.tools.Server"
        println("Start Database with database:" + command)
        process = Some(Process(command).run())
        println("Database started!")
      case Some(_) =>
        println("H2 Database already start")
    }
  }

  val stopH2 = TaskKey[Unit] ("stopH2", "Stop H2 database")
  var stopH2Task = stopH2 in H2 := {
    process match {
      case None =>println("Database already stopped")
      case Some(_) =>
        println("Stop database ......")
        process.foreach(_.destroy())
        process = None
        println("Database stopped......")
    }
  }
}

object MainBuild extends Build{
  import H2TaskManager._

  lazy val scalazVersion = "6.0.4"
  lazy val h2Version = "1.4.182"
  lazy val squerylVersion = "0.9.5-6"

  lazy val wekanban = Project("wekanban", file(".")).settings(startH2Task,stopH2Task)
}
