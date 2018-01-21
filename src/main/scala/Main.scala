import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.config.ConfigFactory

object Main {

  def main(args: Array[String]): Unit = {
    val companyTable = TableQuery[Companies]
    val propertyTable = TableQuery[Properties]
    val floorTable = TableQuery[Floors]
    val spaceTable = TableQuery[Spaces]
    val dealTable = TableQuery[Deals]

    val config = ConfigFactory.load

    val url = config.getString("DB_URL")
    val driver = "org.postgresql.Driver"
    val user = config.getString("DB_USER")
    val pass = config.getString("DB_PASSWORD")

    val db = Database.forURL(url = url, driver = driver, user = user, password = pass)

    val companyCount = 3000
    val propertyCount = 100000
    val floorCount = 1000000
    val spaceCount = 3000000
    val dealCount = 5000000

    val allSpaces = for {
      s <- 2000001 to spaceCount
      f = math.ceil(s.toDouble/floorCount).toInt
    } yield Space(s, f, s"space_${f}_$s")

    // to get around bulk upload limit, creating an array of insert commands which is then put into DBIO.seq
    // insert commands are done via the .grouped (
    // limit is 32678 (I think - just did 25K for nice even round number
    val grouped = allSpaces.grouped(25000).toList
    val sqlCommand = for {
      index <- grouped.indices
    } yield spaceTable ++= grouped(index)

    Await.result(db.run(DBIO.seq(
      sqlCommand:_*
    )), Duration.Inf)

  }
}