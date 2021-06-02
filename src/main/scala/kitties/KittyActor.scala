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

  private var kittySpeedRate = 0
  //private var frameIndex = 0
  //private var loops = 1
  private var cancellable : Cancellable = _

  override def receive: Receive = {
    active(hasStarted = false, 0, 0)
  }

  def active(hasStarted: Boolean, loops: Int, frameIndex: Int): Receive = {
    case Start =>
      become(active(hasStarted = true, 1, 0))
    case Stop =>
      become(active(hasStarted = false, 0, 0))
      handleStop()
    case Clicked => if (hasStarted) handleClick(frameIndex)
    case NextFrame => handleFrameChange(hasStarted, loops, frameIndex)
    case _ =>
  }

  def handleClick(frameIndex: Int): Unit = {
    val score = FRAME_POINTS(frameIndex)
    println("Kitty no. " + (kittyIndex + 1) + " received Click message")
    if((kittySpeedRate + score < 1000) && (kittySpeedRate + score > 0)) kittySpeedRate = kittySpeedRate + score
    kittiesPanelActor ! UpdateLabel(score)
  }

  def handleFrameChange(hasStarted: Boolean, loops: Int, frameIndex: Int): Unit = {
    if (!hasStarted) {
      while (cancellable != null && !cancellable.isCancelled) {
        cancellable.cancel()
      }
    } else {
      val newIndex = updateFrameIndex(loops, frameIndex)
      import context.dispatcher
      println("Kitty no. " + (kittyIndex + 1) + " speed rate: " + kittySpeedRate)
      cancellable = context.system.scheduler.scheduleOnce((1000 - kittySpeedRate*5).millis)(self ! NextFrame)
      kittiesPanelActor ! ChangeFrame(kittyIndex, newIndex)
    }
  }

  def handleStop(): Unit = {
    println("Kitty no. " + (kittyIndex + 1) + " received Stop message")
    while (cancellable != null && !cancellable.isCancelled) {
      cancellable.cancel()
    }
    become(active(hasStarted = false, 0, 0))
    //frameIndex = 0
    kittySpeedRate = 0
  }

  def updateFrameIndex(loops: Int, frameIndex: Int): Int = {
    if(loops < 3 && STATE_INDEXES.contains(frameIndex)) {
      val random = new Random()
      val value = random.nextInt() % 3
      if(value == 1) {
        // frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
        become(active(hasStarted = true, 1, (frameIndex+1) % ANIMATION_LENGTH))
        (frameIndex + 1) % ANIMATION_LENGTH
      }
      else {
        // frameIndex = frameIndex - 1
        become(active(hasStarted = true, loops+1, (frameIndex-1) % ANIMATION_LENGTH))
        (frameIndex - 1) % ANIMATION_LENGTH
      }
    }
    else {
      // frameIndex = (frameIndex + 1) % ANIMATION_LENGTH
      if(!STATE_INDEXES.contains(frameIndex + 1)) become(active(hasStarted = true, 1, (frameIndex + 1) % ANIMATION_LENGTH ))
      (frameIndex + 1) % ANIMATION_LENGTH
    }
  }
}
