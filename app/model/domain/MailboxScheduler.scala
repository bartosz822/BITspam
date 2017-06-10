package model.domain

import javax.inject.Inject

import akka.actor.{Actor, ActorRef, ActorSystem}

import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

class MailboxScheduler @Inject()(mailboxActor: ActorRef, system: ActorSystem) {

  system.scheduler.schedule(
    Duration.Zero,
    5 minutes,
    mailboxActor,
    MailboxActor.GetMail
  )

}
