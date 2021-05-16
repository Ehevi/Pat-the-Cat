package Package

import akka.actor.{Actor, ActorRef}
import scalafx.scene.paint.Color

case object Clicked
case object TimePassed

object KittyActor {
  sealed trait StateChange
  final case class CurrentAndNextState(replyTo: ActorRef) extends StateChange
  final case class ChangeTo(replyTo: ActorRef, state: State) extends StateChange

  sealed trait State
  final case object Sleep extends State
  final case object Run extends State
  final case object Scratch extends State
  final case object WakeUp extends State
  final case object Yawn extends State
}

class KittyActor(backgroundColor: Color) extends Actor {
  private var score = 0 // ?
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
