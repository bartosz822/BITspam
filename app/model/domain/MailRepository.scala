package model.domain

import akka.Done
import model.infractructure.mongo.EmailUpdate

import scala.concurrent.Future

trait MailRepository {

  def getMails(): Future[List[Email]]

  def getMail(emailId: EmailId): Future[Option[Email]]

  def updateMail(email: EmailUpdate): Future[Done]

}

