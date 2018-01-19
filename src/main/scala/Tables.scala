import slick.driver.PostgresDriver.api._

import java.sql.Timestamp


case class Company(id: Int, name: String)
case class Companies(tag: Tag) extends Table[Company](tag, "company") {
  def id = column[Int]("c_id")
  def name = column[String]("name")
  def * = (id, name) <> (Company.tupled, Company.unapply)
}

case class Property(id: Int, companyId: Int, name: String)
case class Properties(tag: Tag) extends Table[Property](tag, "property") {
  def id = column[Int]("p_id")
  def companyId = column[Int]("c_id")
  def name = column[String]("name")
  def * = (id, companyId, name) <> (Property.tupled, Property.unapply)
}

case class Floor(id: Int, propertyId: Int, name: String)
case class Floors(tag: Tag) extends Table[Floor](tag, "floor") {
  def id = column[Int]("f_id")
  def propertyId = column[Int]("p_id")
  def name = column[String]("name")
  def * = (id, propertyId, name) <> (Floor.tupled, Floor.unapply)
}

case class Space(id: Int, floorId: Int, name: String)
case class Spaces(tag: Tag) extends Table[Space](tag, "space") {
  def id = column[Int]("s_id")
  def floorId = column[Int]("f_id")
  def name = column[String]("name")
  def * = (id, floorId, name) <> (Space.tupled, Space.unapply)
}

case class Deal(id: Int, spaceId: Int, name: String, start: Timestamp, end: Timestamp, amount: Double)
case class Deals(tag: Tag) extends Table[Deal](tag, "deal") {
  def id = column[Int]("d_id")
  def spaceId = column[Int]("s_id")
  def name = column[String]("name")
  def start = column[Timestamp]("start")
  def end = column[Timestamp]("end")
  def amount = column[Double]("amount")
  def * = (id, spaceId, name, start, end, amount) <> (Deal.tupled, Deal.unapply)
}