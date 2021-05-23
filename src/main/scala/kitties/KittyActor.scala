package kitties

import akka.actor.{Actor, ActorRef, Cancellable}
import scalafx.scene.paint.Color

import scala.concurrent.duration._

case object Clicked
case object NextFrame
case object Start
case object Stop

class KittyActor(val kittyIndex: Int, val backgroundColor: Color, val xPosition: Int, val kittiesPanelActor: ActorRef)
  extends Actor {
  import context._

  private var kittyScore = 0
  private var frameIndex = 0
  private var cancellable : Cancellable = _

  override def receive: Receive = {
    active(false)
  }

  def active(hasStarted: Boolean): Receive = {
    case Start => become(active(true))
    case Stop =>
      handleStop()
      become(active(false))
    case Clicked => if (hasStarted) handleClick()
    case NextFrame => handleFrameChange()
    case _ =>
  }

  def handleClick(): Unit = {
    val score = FRAME_POINTS(frameIndex)
    println("Kitty" + kittyIndex + 1 + ": zostalem nacisniety:((")
    if((kittyScore + score < 1000) && (kittyScore + score > 0)) kittyScore = kittyScore + score
    kittiesPanelActor ! UpdateLabel(score)
  }

  def handleFrameChange(): Unit = {
    frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    import context.dispatcher
    println(kittyScore + " in kitty" + (kittyIndex + 1))
    cancellable = context.system.scheduler.scheduleOnce((1000 - kittyScore*5).millis)(self ! NextFrame)
    kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
  }

  def handleStop(): Unit = {
    cancellable.cancel()
    frameIndex = 0
    kittyScore = 0
  }
}
