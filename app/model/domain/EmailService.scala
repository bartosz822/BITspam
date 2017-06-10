package model.domain

import scala.concurrent.Future

trait EmailService {

  def getMailbox(accountId: String): Future[List[EmailMetadata]]

}
