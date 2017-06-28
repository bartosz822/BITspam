package model.infractructure

import java.util.Date

import model.domain.mailbox.EmailAdded
import model.domain.{Email, EmailId}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

package object mongo {

  final case class EmailDTO(id: EmailId, sender: String, sentTime: Date, receivedTime: Date, content: String, label: String) {

    def toDomain: Email = Email(id, sender, sentTime, receivedTime, content, label)

  }

  object EmailDTO {

    def apply(emailAdded: EmailAdded): EmailDTO = new EmailDTO(
      emailAdded.id,
      emailAdded.emailMetadata.sender,
      emailAdded.emailMetadata.sentTime,
      emailAdded.emailMetadata.receivedTime,
      emailAdded.content,
      emailAdded.label
    )

  }

  final case class EmailUpdate(id: EmailId, content: String, label: String)

  implicit object EmailReaderWriter extends BSONDocumentReader[EmailDTO] with BSONDocumentWriter[EmailDTO] {

    override def read(bson: BSONDocument): EmailDTO = EmailDTO(
      EmailId(bson.getAs[String]("folderId").get, bson.getAs[String]("messageId").get),
      bson.getAs[String]("sender").get,
      bson.getAs[Date]("sentTime").get,
      bson.getAs[Date]("receivedTime").get,
      bson.getAs[String]("content").get,
      bson.getAs[String]("label").get
    )

    override def write(t: EmailDTO): BSONDocument = BSONDocument(
      "messageId" -> t.id.messageId,
      "folderId" -> t.id.folderId,
      "sender" -> t.sender,
      "sentTime" -> t.sentTime,
      "receivedTime" -> t.receivedTime,
      "content" -> t.content,
      "label" -> t.label
    )

  }

}
