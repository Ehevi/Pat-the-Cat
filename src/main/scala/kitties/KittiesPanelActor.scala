package kitties

import akka.actor.{Actor, ActorRef}
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.scene.input.MouseEvent

case class ChangeFrame(index: Int, frameIndex: Int)
case class UpdateLabel(addScore: Int)

sealed abstract class KittieXRange
case class Kittie1() extends KittieXRange
case class Kittie2() extends KittieXRange
case class Kittie3() extends KittieXRange
case class Kittie4() extends KittieXRange
case class Kittie5() extends KittieXRange
case class SpaceBetween() extends KittieXRange

class KittiesPanelActor(kittiesPanel: KittiesPanel) extends Actor {
  override def receive: Receive = {
    case ChangeFrame(index, frameIndex) => changeFrame(index, frameIndex)
    case UpdateLabel(addScore) => updateLabel(addScore)
    case _ =>
  }

  def changeFrame(index: Int, frameIndex: Int): Unit = {
    Platform.runLater {
      kittiesPanel.changeKittyFrame(index, frameIndex)
    }
  }

  def matchKittyX(x: Double): Int = {
    x match {
      case x if INITIAL_KITTIES_X < x && x < (KITTY_WIDTH + INITIAL_KITTIES_X) => 1
      case x if (SPACE_BETWEEN_KITTIES + INITIAL_KITTIES_X + KITTY_WIDTH) < x &&
        x < (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES + KITTY_WIDTH * 2) => 2
      case x if x < (SPACE_BETWEEN_KITTIES * 2 + INITIAL_KITTIES_X + KITTY_WIDTH * 3) &&
        x > (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 2 + KITTY_WIDTH * 2) => 3
      case x if x < (SPACE_BETWEEN_KITTIES * 3 + INITIAL_KITTIES_X + KITTY_WIDTH * 4) &&
        x > (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 3 + KITTY_WIDTH * 3) => 4
      case x if x < (WINDOW_WIDTH - SPACE_BETWEEN_KITTIES) &&
        x > (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 4 + KITTY_WIDTH * 4) => 5
      case _ => 0
    }
  }

  kittiesPanel.onMouseClicked = (me: MouseEvent) => {
    val matchedKitty = matchKittyX(me.x)
    matchedKitty match {
      case 0 =>
      case 1 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(0)) ! Clicked
      case 2 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(1)) ! Clicked
      case 3 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(2)) ! Clicked
      case 4 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(3)) ! Clicked
      case 5 => context.system.actorSelection("user/" + KITTY_ACTOR_NAMES(4)) ! Clicked
    }
    println("Kliknales kota " + matchedKitty + ", x: " + me.x + " y: " + me.y)
  }

  def updateLabel(addScore: Int): Unit = {
    DrawingMain.updateScore(addScore)
  }
}
