package model.domain

import java.util.Date

import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor
import model.service.TimeProvider

import scala.collection.mutable

class MailboxActor(emailService: EmailService) extends PersistentActor with ActorLogging {

  import MailboxActor._

  private implicit val executionContext = context.dispatcher

  private lazy val accountId = context.system.settings.config.getString("zoho.accountId")

  private val emails = mutable.Map[EmailId, EmailMetadata]()

  override def persistenceId: String = "mailbox-actor"

  override def receiveRecover: Receive = handleEvent

  override def receiveCommand: Receive = {
    case GetMail => getMail()
  }

  def handleEvent: Receive = {
    case EmailAdded(emailId, emailMetadata, _) => emails += (emailId -> emailMetadata)
  }

  private def getMail() =
    emailService
      .getMailbox(accountId)
      .map(_.filter(newEmail => !emails.contains(newEmail.id))
        .foreach(newEmail => persist(EmailAdded(newEmail.id, newEmail, TimeProvider.now))(handleEvent)))

}


object MailboxActor {

  /* props */

  def props(emailService: EmailService): Props = Props(new MailboxActor(emailService))

  /* commands */

  sealed trait MailboxCommand

  object GetMail extends MailboxCommand

  /* events */

  sealed trait MailboxEvent {
    val id: EmailId
    val time: Date
  }

  final case class EmailAdded(id: EmailId, emailMetadata: EmailMetadata, time: Date) extends MailboxEvent

}