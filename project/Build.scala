import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._
object ApplicationBuild extends Build {

  val appName         = "PactBanking"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
   Play2WarPlugin.play2WarSettings: _*)
   .settings(
    // ... Your own settings here
    Play2WarKeys.servletVersion := "3.0"

  )

}
