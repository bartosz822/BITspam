package model

import java.util.Date

package object domain {

  final case class EmailId(folderId: String, messageId: String)

  final case class EmailMetadata(messageId: String, folderId: String, sender: String, sentTime: Date, receivedTime: Date)

  final case class Email(id: EmailId)

}
