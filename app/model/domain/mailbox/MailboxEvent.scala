package model.domain.mailbox

import java.util.Date

import model.domain.{EmailId, EmailMetadata}

sealed trait MailboxEvent {
  val id: EmailId
  val time: Date
}

final case class EmailAdded(id: EmailId, emailMetadata: EmailMetadata, content: String, label: String, time: Date) extends MailboxEvent

