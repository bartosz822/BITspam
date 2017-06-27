package controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import model.domain.mailbox.{MailboxActor, MailboxScheduler}
import model.infractructure.mongo.MongoMailRepository
import model.domain.mailbox.{MailboxActor, MailboxScheduler, MockMailRepository}
import model.service.RESTZohoService
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.twirl.api.Html

import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

@Singleton
class HomeController @Inject()(val reactiveMongoApi: ReactiveMongoApi, ws: WSClient, system: ActorSystem)
  extends Controller with MongoController with ReactiveMongoComponents {

  implicit val actorSystem = system
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val zohoAuthToken = system.settings.config.getString("zoho.authToken")

  private val emailService = new RESTZohoService(zohoAuthToken, ws)
  private val emailRepository = new MockMailRepository
  private val mailboxActor = system.actorOf(MailboxActor.props(emailService, emailRepository))
  private val _ = new MailboxScheduler(mailboxActor, system)

  private val readJournal = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  // TODO: here we have a reference to the mail repository which you can query for a list of emails or a specific email
  private val mailRepository = new MongoMailRepository(db, readJournal)


  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def mails = Action.async { implicit request =>
    val mails = emailRepository.getMails
    val contents = mails.map(_.filter(_.label == "1.0").take(5).map(_.content).map(Html(_)))

    contents.map(l =>Ok(views.html.mail(l)))
  }

//  TODO Respond with one specific mail
  def getMail(id: String) = play.mvc.Results.TODO
//  TODO Update mail with specific id
  def updateMail(id: String) = play.mvc.Results.TODO

}
