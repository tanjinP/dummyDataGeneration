import com.typesafe.config.ConfigFactory
import slick.driver.PostgresDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {

  private val RECORDS_TO_INSERT = 500000
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

    // generating lots of case class representing table data via for comp (see def)
    val activities = generateActivityLogs
    // chunking the seq so insertion into postgres can be done without bulk exception being thrown (limit is ~37K items)
    val chunkedActivities = chunk(activities).toList
    val activityLogsTable = TableQuery[ActivityLogs]
    val activityInsertSql = for {
      index <- chunkedActivities.indices
    } yield activityLogsTable ++= chunkedActivities(index)

    val proposals = generateProposals

    val chunkedProposals = chunk(proposals).toList
    val proposalTable = TableQuery[Proposals]
    val proposalInsertSql = for {
      index <- chunkedProposals.indices
    } yield proposalTable ++= chunkedProposals(index)

    val sql = activityInsertSql ++ proposalInsertSql
    // executing SQL command to insert into database in batch inserts
    Await.result(db.run(DBIO.seq(
      sql:_*
    )), Duration.Inf)

  }

  // def has potential to be generic with reflexion regarding generation of fields - won't dig into
  private def generateActivityLogs: IndexedSeq[ActivityLog] = {
    for {
      id <- RECORDS_TO_INSERT + 1 to 2 * RECORDS_TO_INSERT
      string = s"here's a random string with the id number $id"
      userId = math.ceil(id.toDouble/5000).toInt
    } yield ActivityLog(id, string, userId, string, string, id, id)
  }

  private def generateProposals: IndexedSeq[Proposal] = {
    for {
      id <- RECORDS_TO_INSERT + 1 to 2 * RECORDS_TO_INSERT
      string = s"string for the proposal $id"
      activityId = math.ceil(id.toDouble/5000).toInt
      bool = id %2 == 0
    } yield Proposal(id, activityId, bool, !bool, string)
  }


  private def chunk[A](seq: Seq[A]): Iterator[Seq[A]] = {
    seq.grouped(CHUNK_AMOUNT)
  }
}