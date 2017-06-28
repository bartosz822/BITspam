package controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import model.RESTZohoService
import model.api.Requests.MailUpdateRequest
import model.domain.EmailId
import model.domain.mailbox.{MailboxActor, MailboxScheduler}
import model.infractructure.mongo.MongoMailRepository
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

@Singleton
class HomeController @Inject()(val reactiveMongoApi: ReactiveMongoApi, ws: WSClient, system: ActorSystem)
  extends Controller with MongoController with ReactiveMongoComponents {

  implicit val actorSystem = system
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val zohoAuthToken = system.settings.config.getString("zoho.authToken")

  private val emailService = new RESTZohoService(zohoAuthToken, ws)
  private val mailboxActor = system.actorOf(MailboxActor.props(emailService))
  private val _ = new MailboxScheduler(mailboxActor, system)

  private val readJournal = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  // TODO: here we have a reference to the mail repository which you can query for a list of emails or a specific email
  private val mailRepository = new MongoMailRepository(db, readJournal)


  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def mails = Action.async { implicit request =>
    val mails = mailRepository.getMails()

    mails.map {
      _.filter(_.label == "1.0").take(5)
    }.map(l => Ok(views.html.mails(l)))
  }

  def getMail(id: String) = Action.async { implicit  request =>

    val emailId = EmailId(id)
    val mail = mailRepository.getMail(emailId)

    mail.map {
      _.map { m => Ok(views.html.mail(m)) }
        .getOrElse {
          NotFound
        }
    }
  }

  implicit val emailUpdateReads = Json.reads[MailUpdateRequest]

  def updateMail(id: String) = Action.async(parse.json) { implicit request =>
      mailRepository.updateMail(request.body.as[MailUpdateRequest].toDTO(EmailId(id))).map(_ => Ok)
  }



}
