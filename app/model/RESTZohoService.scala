package model

import java.util.Date
import javax.inject.Inject

import model.domain.{EmailMetadata, EmailService}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsValue, Reads}
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RESTZohoService @Inject()(authToken: String, client: WSClient) extends EmailService {

  import RESTZohoService._

  private val address = "mail.zoho.com" // TODO: move to .conf file


  override def getMailbox(accountId: String): Future[List[EmailMetadata]] =
    client
      .url(getMailboxRequest(address, accountId))
      .handle { json => (json \ "data").as[List[JsValue]].flatMap(_.asOpt[EmailMetadata]) }


  override def getMailContent(accountId: String, folderId: String, messageId: String): Future[String] =
    client
      .url(getMailContentRequest(address, accountId, folderId, messageId))
      .handle { json => (json \ "data" \ "content").as[String] }


  private implicit class GetRequestHandler(request: WSRequest) {
    def handle[A](handler: JsValue => A) =
      request
        .withHeaders("Authorization" -> authToken)
        .get()
        .map(response => (response.status, response.json))
        .map {
          case (200, json) => handler(json)
          case other => throw new IllegalStateException(s"Unexpected response $other while handling request $request")
        }
  }

  private implicit val emailReads: Reads[EmailMetadata] = (
    (JsPath \ "messageId").read[String] and
      (JsPath \ "folderId").read[String] and
      (JsPath \ "sender").read[String] and
      (JsPath \ "sentDateInGMT").read[String].map(s => new Date(s.toLong)) and
      (JsPath \ "receivedTime").read[String].map(s => new Date(s.toLong))
    ) (EmailMetadata.apply _)

}

object RESTZohoService {

  private def getMailboxRequest(address: String, accountId: String) =
    s"http://$address/api/accounts/$accountId/messages/view"

  private def getMailContentRequest(address: String, accountId: String, folderId: String, messageId: String) =
    s"http://$address/api/accounts/$accountId/folders/$folderId/messages/$messageId/content"

}