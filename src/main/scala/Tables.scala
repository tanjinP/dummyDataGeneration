import slick.driver.PostgresDriver.api._

import java.sql.{Date, Timestamp}

case class ActivityLog(
                      id: Int,
                      createdAt: Timestamp,
                      updatedAt: Timestamp,
                      status: String,
                      userId: Int,
                      industryName: String,
                      tenantLocation: String,
                      successProbability: Int,
                      leaseExpiration: Date,
                      lastModifiedTime: Timestamp,
                      accountId: Int,
                      moveInDate: Date
                      )

case class ActivityLogs(tag: Tag) extends Table[ActivityLog](tag, "activity_logs") {
  def id = column[Int]("id")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def status = column[String]("status")
  def userId = column[Int]("user_id")
  def industryName = column[String]("industry_name")
  def tenantLocation = column[String]("tenant_location")
  def successProbability = column[Int]("success_probability")
  def leaseExpiration = column[Date]("lease_expiration_date")
  def lastModifiedTime = column[Timestamp]("last_modified_time")
  def accountId = column[Int]("account_id")
  def moveInDate = column[Date]("move_in_date")
  def * = (id, createdAt, updatedAt, status, userId, industryName, tenantLocation, successProbability,
    leaseExpiration, lastModifiedTime, accountId, moveInDate) <> (ActivityLog.tupled, ActivityLog.unapply)
}