package model.infractructure.smtp

import java.util.Properties
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeMessage}

import akka.Done
import model.domain.MailService

import scala.concurrent.{ExecutionContext, Future}

final case class MailboxWithPasswordAuth(address: String, password: String, host: String, port: Int) {
  def toSession: Session = {
    val props: Properties = new Properties
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", host)
    props.put("mail.smtp.port", port.toString)

    Session.getInstance(props, new Authenticator() {
      override protected def getPasswordAuthentication: PasswordAuthentication = new PasswordAuthentication(address, password)
    })
  }

}


class SMTPMailService(config: MailboxWithPasswordAuth)(implicit ec: ExecutionContext) extends MailService {

  private val session = config.toSession

  override def send(recipient: String, subject: String, content: String): Future[Done] = Future {

    def buildMessage = {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(config.address))
      message.setRecipients(Message.RecipientType.TO, recipient)
      message.setSubject(subject, "UTF-8")
      message.setContent(content, "text/html; charset=UTF-8")
      message
    }

    Transport.send(buildMessage)

  }.map(_ => Done)

}
