package model.service


import java.util.Date
import javax.inject.Inject

import model.domain.{EmailId, EmailMetadata, EmailService}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsValue, Reads}
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RESTZohoService @Inject()(authToken: String, client: WSClient) extends EmailService {

  import RESTZohoService._

  private val address = "mail.zoho.com" // TODO: move to .conf file

  def getMailbox(accountId: String): Future[List[EmailMetadata]] = {
    client
      .url(getMailboxRequest(address, accountId))
      .withHeaders("Authorization" -> authToken)
      .get()
      .map(response => (response.status, response.json))
      .map {
        case (200, json) => (json \ "data").as[List[JsValue]].flatMap(_.asOpt[EmailMetadata])
        case other => throw new IllegalStateException(s"Unexpected response while getting a mailbox $other")
      }
  }

  private implicit val emailReads: Reads[EmailMetadata] = (
    (JsPath \ "messageId").read[String].map(EmailId) and
      (JsPath \ "sender").read[String] and
      (JsPath \ "sentDateInGMT").read[String].map(s => new Date(s.toLong)) and
      (JsPath \ "receivedTime").read[String].map(s => new Date(s.toLong))
    ) (EmailMetadata.apply _)

}

object RESTZohoService {

  private def getMailboxRequest(address: String, accountId: String) = s"http://$address/api/accounts/$accountId/messages/view"

}