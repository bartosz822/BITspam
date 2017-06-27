package model.domain

import scala.concurrent.Future

trait MailRepository {

  def getMails(): Future[List[Email]]

  def getMail(emailId: EmailId): Future[Option[Email]]

}

