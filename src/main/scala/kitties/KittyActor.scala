package kitties

import akka.actor.{Actor, ActorRef, Cancellable}
import scalafx.scene.paint.Color

import scala.concurrent.duration._

case object Clicked
case object NextFrame

class KittyActor(kittyIndex: Int, backgroundColor: Color, xPosition: Int, kittiesPanelActor: ActorRef) extends Actor {
  private var score = 0
  private var frameIndex = 0

  override def receive: Receive = {
    case Clicked => handleClick()
    case NextFrame => handleFrameChange
    case _ =>
  }

  def handleClick(): Unit = {
    score = score + 1
  }

  def handleFrameChange: Unit = {
    frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    println("Im kitty " + self.toString()  + " and im changing my frame to the " + frameIndex + " one")
    import context.dispatcher
    context.system.scheduler.scheduleOnce((1000 - score).millis)(self ! NextFrame)
    kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
  }
}
