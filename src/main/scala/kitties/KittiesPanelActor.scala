package kitties

import akka.actor.Actor
import kitties.KITTY_X_MATCHER.{firstKittyMatched, secondKittyMatched, thirdKittyMatched, fourthKittyMatched, fifthKittyMatched}
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.scene.input.MouseEvent

case class ChangeFrame(index: Int, frameIndex: Int)
case class UpdateLabel(addScore: Int)

class KittiesPanelActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case ChangeFrame(index, frameIndex) => changeFrame(index, frameIndex)
    case UpdateLabel(addScore) => updateLabel(addScore)
    case _ =>
  }

  def changeFrame(index: Int, frameIndex: Int): Unit = {
    println("KittiesPanelActor changing Kitty no. " + (index + 1) + " frame to " + frameIndex )
    Platform.runLater {
      kittiesPanel.changeKittyFrame(index, frameIndex)
    }
  }

  def matchKitty(x: Double): Int = {
    x match {
      case x if firstKittyMatched(x) => 1
      case x if secondKittyMatched(x) => 2
      case x if thirdKittyMatched(x) => 3
      case x if fourthKittyMatched(x) => 4
      case x if fifthKittyMatched(x) => 5
      case _ => 0
    }
  }

  kittiesPanel.onMouseClicked = (me: MouseEvent) => {
    val matchedKitty = matchKitty(me.x)
    matchedKitty match {
      case 0 =>
      case 1 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(0)) ! Clicked
      case 2 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(1)) ! Clicked
      case 3 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(2)) ! Clicked
      case 4 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(3)) ! Clicked
      case 5 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(4)) ! Clicked
    }
    if(matchedKitty != 0)
      println("Clicked Kitty no. " + matchedKitty + ", x: " + me.x + " y: " + me.y)
  }

  def updateLabel(addScore: Int): Unit = {
    println("KittiesPanelActor adding score " + addScore)
    DrawingMain.updateScore(addScore)
  }
}
