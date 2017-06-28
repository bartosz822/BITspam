package model.api

import model.domain.EmailId

object Requests {

  final case class MailUpdateRequest(emailId: EmailId, content: String, label: String)

}
