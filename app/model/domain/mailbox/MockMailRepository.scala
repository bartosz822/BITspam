package model.domain.mailbox
import model.domain

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by bartek on 27.06.17.
  */
class MockMailRepository extends MailRepository{

  val mails = mutable.MutableList[domain.Email]()

  override def getMails: Future[List[domain.Email]] = Future { mails.toList }

  override def getMail(id: domain.EmailId): Future[domain.Email] = ???

  override def addMail(newMail: domain.Email): Unit = mails += newMail

  override def updateMail(id: domain.EmailId, content: String, label: String): Unit = ???
}
