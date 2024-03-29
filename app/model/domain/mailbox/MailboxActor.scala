package model.domain.mailbox

import akka.actor.{ActorLogging, Props}
import akka.pattern.{ask, pipe}
import akka.persistence.PersistentActor
import akka.util.Timeout
import model.domain._
import model.domain.classification.ClassifierActor
import model.service.{IsRelevantClassifier, TimeProvider}

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._

class MailboxActor(emailService: EmailService, mailSendingService: MailSendingService) extends PersistentActor with ActorLogging {

  import MailboxActor._

  private implicit val executionContext = context.dispatcher
  private implicit val timeout = Timeout(5 seconds)

  private lazy val accountId = context.system.settings.config.getString("zoho.accountId")

  private val classifierService = new IsRelevantClassifier
  private lazy val classifierActor = context.actorOf(ClassifierActor.props(classifierService))

  private val emails = mutable.Set[EmailId]()

  override def persistenceId: String = "mailbox-actor"

  override def receiveRecover: Receive = handleEvent

  override def receiveCommand: Receive = {
    case GetMail => getMail()
    case c: ProcessNewMails => processNewMails(c)
    case c: FinishProcessing => finishProcessing(c)
  }

  def handleEvent: Receive = {
    case EmailAdded(emailId, _, _, _, _) => emails += emailId
  }

  private def getMail() =
    emailService
      .getMailbox(accountId)
      .map(_.filter(newEmail => !emails.contains(EmailId(newEmail.folderId, newEmail.messageId))))
      .map(ProcessNewMails)
      .pipeTo(self)

  private def getMailContent(metadata: EmailMetadata): Future[String] =
    emailService
      .getMailContent(accountId, metadata.folderId, metadata.messageId)

  private def processNewMails(command: ProcessNewMails) =
    command.emails.foreach(processNewMail)

  private def processNewMail(metadata: EmailMetadata) = {
    val emailAdded = for {
      content <- getMailContent(metadata)
      label <- (classifierActor ? ClassifierActor.Classify(content)).mapTo[String]
      emailId = EmailId(metadata.folderId, metadata.messageId)
      _ <- sendNotification(emailId)
    } yield EmailAdded(emailId, metadata, content, label, TimeProvider.now)


    emailAdded.map(FinishProcessing).pipeTo(self)
  }

  private def finishProcessing(command: FinishProcessing) = {
    val event = command.emailAdded
    if (!emails.contains(event.id)) persist(event)(handleEvent)
  }

  private def sendNotification(emailId: EmailId) = {
    val link = s"http://localhost:9000/mails/${emailId.value}"

    mailSendingService.send(
      "radzynskib@gmail.com", // TODO: subscription mechanism
      "BIT new message!",
      views.html.email_template(link).toString()
    )
  }

}


object MailboxActor {

  /* props */

  def props(emailService: EmailService, mailSendingService: MailSendingService): Props =
    Props(new MailboxActor(emailService, mailSendingService))

  /* commands */

  sealed trait MailboxCommand

  object GetMail extends MailboxCommand

  private final case class ProcessNewMails(emails: List[EmailMetadata]) extends MailboxCommand

  private final case class FinishProcessing(emailAdded: EmailAdded) extends MailboxCommand

}