package kitties

import akka.actor.{Actor, ActorRef}
import scalafx.scene.paint.Color

import scala.concurrent.duration._

case object Clicked
case object NextFrame

class KittyActor(val kittyIndex: Int, val backgroundColor: Color, val xPosition: Int, val kittiesPanelActor: ActorRef)
  extends Actor {

  private var score = 0
  private var frameIndex = 0

  override def receive: Receive = {
    case Clicked => handleClick()
    case NextFrame => handleFrameChange()
    case _ =>
  }

  def handleClick(): Unit = {
    println("Kotek" + kittyIndex + ": zostalem nacisniety:((")
    if(score + 50 < 1000) score = score + 50
  }

  def handleFrameChange(): Unit = {
    frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    import context.dispatcher
    println(score + " in kitty" + kittyIndex)
    context.system.scheduler.scheduleOnce((1000 - score).millis)(self ! NextFrame)
    kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
  }
}
