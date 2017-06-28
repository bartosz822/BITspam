package model.domain

import scala.concurrent.Future

trait EmailService {

  def getMailbox(accountId: String): Future[List[EmailMetadata]]

  def getMailContent(accountId: String, folderId: String, messageId: String): Future[String]

}
