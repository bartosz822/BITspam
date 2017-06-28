package model.domain.mailbox

import javax.inject.Inject

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class MailboxScheduler @Inject()(mailboxActor: ActorRef, system: ActorSystem) {

  system.scheduler.schedule(
    Duration.Zero,
    10 seconds,
    mailboxActor,
    MailboxActor.GetMail
  )

}
