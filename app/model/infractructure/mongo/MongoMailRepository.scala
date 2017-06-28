package model.infractructure.mongo

import akka.Done
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import model.domain.mailbox.EmailAdded
import model.domain.{Email, EmailId, MailRepository}
import play.api.Logger
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class MongoMailRepository(database: DB, readJournal: LeveldbReadJournal)
                         (implicit ec: ExecutionContext, mat: Materializer) extends MailRepository {

  import MongoMailRepository._

  private val mailCollection = database.collection[BSONCollection]("mails")

  readJournal.allPersistenceIds()
    .runForeach { persistentId =>
      readJournal.eventsByPersistenceId(persistentId)
        .map(_.event)
        .map {
          case e: EmailAdded => addEmail(e)
          case _ => // do not handle unknown events
        }
        .runWith(Sink.ignore)
    }
    .onComplete {
      case Success(_) => Logger.info("Email event stream successfully completed")
      case Failure(ex) => Logger.warn("Email event stream finished with error", ex)
    }


  private def addEmail(event: EmailAdded): Future[Done] =
    mailCollection
      .insert(EmailDTO(event))
      .map(_ => Done)


  override def getMails(): Future[List[Email]] =
    mailCollection
      .find(BSONDocument.empty)
      .cursor[EmailDTO]()
      .collect[List]()
      .map(_.map(_.toDomain))


  override def getMail(emailId: EmailId): Future[Option[Email]] =
    mailCollection
      .find(emailIdSelector(emailId))
      .one[EmailDTO]
      .map(_.map(_.toDomain))


  override def updateMail(email: EmailUpdate): Future[Done] =
    mailCollection
        .update(
          emailIdSelector(email.id),
          BSONDocument(
            "$set" -> BSONDocument(
              "content" -> email.content,
              "label" -> email.label
            )
          )
        ).map(_ => Done)

}


object MongoMailRepository {

  private def emailIdSelector(emailId: EmailId) =
    BSONDocument("messageId" -> emailId.messageId, "folderId" -> emailId.folderId)

}