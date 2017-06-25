package model.domain.classification

import akka.actor.{Actor, Props}
import model.domain.classification.ClassifierActor.Classify

class ClassifierActor extends Actor {

  override def receive: Receive = {
    case Classify(_) => sender() ! "label"
  }

}

object ClassifierActor {

  def props: Props = Props(new ClassifierActor)

  final case class Classify(emailContent: String)

}