import slick.driver.PostgresDriver.api._

case class ActivityLog(
                      id: Int,
                      status: String,
                      userId: Int,
                      industryName: String,
                      tenantLocation: String,
                      successProbability: Int,
                      accountId: Int
                      )
case class ActivityLogs(tag: Tag) extends Table[ActivityLog](tag, "activity_logs") {
  def id = column[Int]("id")
  def status = column[String]("status")
  def userId = column[Int]("user_id")
  def industryName = column[String]("industry_name")
  def tenantLocation = column[String]("tenant_location")
  def successProbability = column[Int]("success_probability")
  def accountId = column[Int]("account_id")
  def * = (id, status, userId, industryName, tenantLocation, successProbability, accountId) <>
    (ActivityLog.tupled, ActivityLog.unapply)
}

case class Proposal(
                   id: Int,
                   activityLogId: Int,
                   lastEntered: Boolean,
                   editLocked: Boolean,
                   proposalType: String
                   )
case class Proposals(tag: Tag) extends Table[Proposal](tag, "proposals") {
  def id = column[Int]("id")
  def activityLog = column[Int]("activity_log_id")
  def lastEntered = column[Boolean]("last_entered")
  def editLocked = column[Boolean]("edit_locked")
  def proposalType = column[String]("proposal_type")
  def * = (id, activityLog, lastEntered, editLocked, proposalType) <> (Proposal.tupled, Proposal.unapply)
}