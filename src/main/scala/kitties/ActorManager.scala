package kitties

import akka.actor.{ActorRef, ActorSystem, Props}
import scala.concurrent.duration.DurationInt

object ActorManager {
  private val kittiesPanel = new KittiesPanel
  private val actorSystem = ActorSystem("ActorSystem")
  private val kittiesPanelActor = actorSystem.actorOf(Props(new KittiesPanelActor(kittiesPanel)), name = "KittiesPanelActor")

  private val kittyActors = new Array[ActorRef](KITTIES_NUMBER)

  def prepareKittyActors(): Unit = {
    for (i <- 0 until KITTIES_NUMBER) {
      val backgroundColor = COLORS(i)
      val x_position = i * (SPACE_BETWEEN_KITTIES + KITTY_WIDTH) + INITIAL_KITTIES_X
      kittyActors(i) = actorSystem.actorOf(Props(new KittyActor(i, backgroundColor, x_position, kittiesPanelActor)),
        name = KITTY_ACTOR_NAMES(i))
      kittiesPanel.addInitialKitty(backgroundColor)
    }
  }

  def startKitties(): Unit = {
    for (i <- 0 until KITTIES_NUMBER) {
      kittiesPanelActor ! ChangeFrame(i, 0)
    }
    import actorSystem.dispatcher
    kittyActors.foreach(kitty => actorSystem.scheduler.scheduleOnce(1000.millis)(kitty ! NextFrame))
    kittyActors.foreach(kitty => kitty ! Start)
  }

  def stopAll(): Unit = {
    kittyActors.foreach(kitty => kitty ! Stop)
  }

  def terminateActorSystem(): Unit = {
    actorSystem.terminate()
  }

  def getKittiesPanel: KittiesPanel = kittiesPanel
}
