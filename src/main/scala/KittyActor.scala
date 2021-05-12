import akka.actor.Actor
import scalafx.scene.paint.Color

case object Clicked
case object TimePassed

class KittyActor(backgroundColor: Color) extends Actor {
  private var score = 0
  private var frame_index = 0

  override def receive: Receive = {
    case Clicked => handleClick
    case TimePassed => handleFrameChange
    case _ =>
  }

  def handleClick = {
    score = score + 1
  }

  def handleFrameChange = {
    frame_index = (frame_index + 1) % Package.ANIMATION_LENGTH
  }
}
