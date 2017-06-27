package model.domain.mailbox

import model.domain.{Email, EmailId}

import scala.concurrent.Future

/**
  * Created by bartek on 27.06.17.
  */
trait MailRepository {

  def getMails: Future[List[Email]]

  def getMail(id: EmailId): Future[Email]

  def addMail(newMail: Email): Unit

  def updateMail(id: EmailId, content: String, label: String): Unit

}
