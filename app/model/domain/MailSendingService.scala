package model.domain


import akka.Done

import scala.concurrent.Future

trait MailSendingService {

  def send(recipient: String, subject: String, content: String): Future[Done]

}
