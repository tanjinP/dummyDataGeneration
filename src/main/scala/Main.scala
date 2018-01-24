import java.sql.{Date, Timestamp}

import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.typesafe.config.ConfigFactory

object Main {

  private val HALF_MILLION = 500000
  private val CHUNK_AMOUNT = 10000

  def main(args: Array[String]): Unit = {

    // obtaining details of database from application.conf
    val config = ConfigFactory.load
    val url = config.getString("DB_URL")
    val driver = "org.postgresql.Driver"
    val user = config.getString("DB_USER")
    val pass = config.getString("DB_PASSWORD")

    // connecting to database to insert dummy data into (currently postgres of AWS RDS)
    val db = Database.forURL(url = url, driver = driver, user = user, password = pass)

    // generating lots of case class reprentation of table data via for comp (see def)
    val activities = generateActivityLogs
    println("successfully generated dummy case classes")

    // chunking the seq so insertion into postgres can be done without bulk exception being thrown (limit is ~37K items)
    val chunkedActivities = chunk(activities).toList
    val activityLogsTable = TableQuery[ActivityLogs]
    val sqlCommand = for {
      index <- chunkedActivities.indices
    } yield activityLogsTable ++= chunkedActivities(index)

    // executing SQL command to insert into database in batch inserts
    Await.result(db.run(DBIO.seq(
      sqlCommand:_*
    )), Duration.Inf)

  }

  // def has potential to be generic with reflexion regarding generation of fields - won't dig into
  private def generateActivityLogs: IndexedSeq[ActivityLog] = {
    for {
      id <- 1 to HALF_MILLION
      string = s"here's a random string with the id number $id"
      created = new Timestamp((math.random * 100000000 * id).toLong)
      updated = new Timestamp((math.random * 600000000 * id).toLong)
      userId = math.ceil(id.toDouble/5000).toInt
      date = new Date(id/100000)
    } yield ActivityLog(id, created, updated, string, userId, string, string, id, date, created, id, date)
  }


  private def chunk[A](seq: Seq[A]): Iterator[Seq[A]] = {
    seq.grouped(CHUNK_AMOUNT)
  }
}