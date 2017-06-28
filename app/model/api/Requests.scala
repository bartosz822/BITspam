package model.api

import java.util.Date

import model.domain.{EmailId, EmailMetadata}
import model.infractructure.mongo.EmailUpdate
import play.api.libs.json.{JsPath, Reads}

object Requests {

  final case class MailUpdateRequest(content: String, label: String) {

    def toDTO(id: EmailId): EmailUpdate = EmailUpdate(id, content, label)

  }


}
