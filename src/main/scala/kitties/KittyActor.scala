package kitties

import akka.actor.{Actor, ActorRef}
import scalafx.scene.paint.Color

import scala.concurrent.duration._

case object Clicked
case object NextFrame

class KittyActor(val kittyIndex: Int, val backgroundColor: Color, val xPosition: Int, val kittiesPanelActor: ActorRef)
  extends Actor {

  private var kittyScore = 0
  private var frameIndex = 0

  override def receive: Receive = {
    case Clicked => handleClick()
    case NextFrame => handleFrameChange()
    case _ =>
  }

  def handleClick(): Unit = {
    println("Kitty" + kittyIndex + 1 + ": zostalem nacisniety:((")
    if(kittyScore + 50 < 1000) kittyScore = kittyScore + 50
    kittiesPanelActor ! UpdateLabel(50)
  }

  def handleFrameChange(): Unit = {
    frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    import context.dispatcher
    println(kittyScore + " in kitty" + (kittyIndex + 1))
    context.system.scheduler.scheduleOnce((1000 - kittyScore).millis)(self ! NextFrame)
    kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
  }
}
