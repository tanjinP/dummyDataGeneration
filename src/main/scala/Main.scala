import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory

object Main {

  def main(args: Array[String]): Unit = {
    val companies = TableQuery[Companies]

    val url = ConfigFactory.load().getString("DB_URL")
    val driver = "org.postgresql.Driver"
    val user = ConfigFactory.load().getString("DB_USER")
    val pass = ConfigFactory.load().getString("DB_PASSWORD")

    val db = Database.forURL(url = url, driver = driver, user = user, password = pass)


    try{
      Await.result(db.run(DBIO.seq(
        companies.result.map(println)
      )), Duration.Inf)
    } finally db.close
  }
}