package controllers

import javax.inject._

import akka.actor.ActorSystem
import model.domain.mailbox.{MailboxActor, MailboxScheduler}
import model.service.RESTZohoService
import play.api.libs.ws.WSClient
import play.api.mvc._

@Singleton
class HomeController @Inject()(ws: WSClient, system: ActorSystem) extends Controller {

  private val zohoAuthToken = system.settings.config.getString("zoho.authToken")

  private val emailService = new RESTZohoService(zohoAuthToken, ws)
  private val mailboxActor = system.actorOf(MailboxActor.props(emailService))
  private val _ = new MailboxScheduler(mailboxActor, system)

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def mails = Action { implicit request =>
    Ok(views.html.mail())
  }

}
