package model

import java.util.Date

package object domain {

  final case class EmailId(folderId: String, messageId: String) {
    val value = folderId + "-" + messageId

  }

  object EmailId{
    def apply(id: String): EmailId = {
      val ids = id.split("-")
      EmailId(ids(0), ids(1))
    }
  }

  final case class EmailMetadata(messageId: String, folderId: String, sender: String, sentTime: Date, receivedTime: Date)

  final case class Email(id: EmailId, sender: String, sentTime: Date, receivedTime: Date, content: String, label: String)

}
