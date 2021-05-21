package kitties

import akka.actor.Actor

class MainFrameActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case _ =>
  }

}
