package kitties

import akka.actor.Actor
import scalafx.application.Platform

case class ChangeFrame(index: Int, frameIndex: Int)

class KittiesPanelActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case ChangeFrame(index, frameIndex) => changeFrame(index, frameIndex)
    case _ =>
  }

  def changeFrame(index: Int, frameIndex: Int): Unit = {
    Platform.runLater {
      kittiesPanel.changeKittyFrame(index, frameIndex)
    }
  }

}
