package kitties

import akka.actor.{Actor, ActorRef}
import scalafx.scene.paint.Color

import scala.concurrent.duration._

case object Clicked
case object NextFrame
case object Start

class KittyActor(val kittyIndex: Int, val backgroundColor: Color, val xPosition: Int, val kittiesPanelActor: ActorRef)
  extends Actor {

  private var kittyScore = 0
  private var frameIndex = 0
  private var hasStarted = false

  override def receive: Receive = {
    case Clicked => handleClick()
    case NextFrame => handleFrameChange()
    case Start => handleStart()
    case _ =>
  }

  def handleClick(): Unit = {
    val score = FRAME_POINTS(frameIndex)
    println("Kitty" + kittyIndex + 1 + ": zostalem nacisniety:((")
    if(hasStarted) {
      if((kittyScore + score < 1000) && (kittyScore + score > 0)) kittyScore = kittyScore + score
      kittiesPanelActor ! UpdateLabel(score)
    }
  }

  def handleFrameChange(): Unit = {
    frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    import context.dispatcher
    println(kittyScore + " in kitty" + (kittyIndex + 1))
    context.system.scheduler.scheduleOnce((1000 - kittyScore*5).millis)(self ! NextFrame)
    kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
  }

  def handleStart(): Unit = {
    frameIndex = 0
    kittyScore = 0
    hasStarted = true
  }
}
