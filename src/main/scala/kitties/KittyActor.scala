package kitties

import akka.actor.{Actor, ActorRef, Cancellable}
import scalafx.scene.paint.Color

import scala.concurrent.duration._
import scala.util.Random

case object Clicked
case object NextFrame
case object Start
case object Stop

class KittyActor(val kittyIndex: Int, val backgroundColor: Color, val xPosition: Int, val kittiesPanelActor: ActorRef)
  extends Actor {
  import context._

  private var kittyScore = 0
  private var frameIndex = 0
  private var loops = 1
  private var cancellable : Cancellable = _

  override def receive: Receive = {
    active(false)
  }

  def active(hasStarted: Boolean): Receive = {
    case Start => become(active(true))
    case Stop =>
      become(active(false))
      handleStop()
    case Clicked => if (hasStarted) handleClick()
    case NextFrame => handleFrameChange(hasStarted)
    case _ =>
  }

  def handleClick(): Unit = {
    val score = FRAME_POINTS(frameIndex)
    println("Kitty" + kittyIndex + 1 + ": zostalem nacisniety:((")
    if((kittyScore + score < 1000) && (kittyScore + score > 0)) kittyScore = kittyScore + score
    kittiesPanelActor ! UpdateLabel(score)
  }

  def handleFrameChange(hasStarted: Boolean): Unit = {
    if (!hasStarted) {
      println("Nie powinienem tu byc...")
      while (cancellable != null && !cancellable.isCancelled) {
        cancellable.cancel()
      }
    } else {
      updateFrameIndex()
      import context.dispatcher
      println(kittyScore + " in kitty" + (kittyIndex + 1))
      cancellable = context.system.scheduler.scheduleOnce((1000 - kittyScore*5).millis)(self ! NextFrame)
      kittiesPanelActor ! ChangeFrame(kittyIndex, frameIndex)
    }
  }

  def handleStop(): Unit = {
    println("Dostalem stop. Kotek: " + (kittyIndex + 1))
    while (cancellable != null && !cancellable.isCancelled) {
      cancellable.cancel()
    }
    frameIndex = 0
    kittyScore = 0
  }

  def updateFrameIndex(): Unit = {
    if(loops < 3 && STATE_INDEXES.contains(frameIndex)) {
      val random = new Random()
      val value = random.nextInt() % 2
      if(value == 1) {
        loops = 1
        frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
      }
      else {
        loops += 1
        frameIndex = (frameIndex - 1)
      }
    }
    else {
      if(!STATE_INDEXES.contains(frameIndex + 1)) loops = 1
      frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
    }
  }
}
