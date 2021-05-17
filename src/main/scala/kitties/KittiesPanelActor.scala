package kitties

import akka.actor.{Actor, ActorRef}

import scala.concurrent.Future

case class ChangeFrame(index: Int, frameIndex: Int)

class KittiesPanelActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case ChangeFrame(index, frameIndex) => changeFrame(index, frameIndex)
    case _ =>
  }

  def changeFrame(index: Int, frameIndex: Int): Unit = {
    import context.dispatcher
    Future {
      kittiesPanel.changeKittyFrame(index, frameIndex)
    }
  }

}
