package model.domain.classification

import javax.inject.Inject

import akka.actor.{Actor, Props}
import model.domain.classification.ClassifierActor.Classify

class ClassifierActor @Inject()(classifier: ClassifierService) extends Actor {

  override def receive: Receive = {
    case Classify(emailContent) => sender() ! classifier.classify(emailContent)
  }



}

object ClassifierActor {

  /* props */
  def props(classifier: ClassifierService): Props = Props(new ClassifierActor(classifier))

  /* commands */
  final case class Classify(emailContent: String)

}