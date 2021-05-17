package kitties

import akka.actor.Actor

case object FullScreen

class MainFrameActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case _ =>
  }

}
