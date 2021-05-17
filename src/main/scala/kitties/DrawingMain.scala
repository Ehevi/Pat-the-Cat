package kitties

import akka.actor.{ActorRef, ActorSystem, Props}
import scalafx.application.JFXApp
import scalafx.scene.Scene

import scala.concurrent.duration.DurationInt

object DrawingMain extends JFXApp {
  private val kittiesPanel = new KittiesPanel

  private val actorSystem = ActorSystem("ActorSystem")
  private val kittiesPanelActor = actorSystem.actorOf(Props(new KittiesPanelActor(kittiesPanel)), name = "KittiesPanelActor")
  private val mainFrameActor = actorSystem.actorOf(Props(new MainFrameActor(kittiesPanel)), name = "MainFrameActor")

  private val kittyActors = new Array[ActorRef](KITTIES_NUMBER)

  prepareKittyActors()
  stage = new JFXApp.PrimaryStage {
    title = "Kitties Game"
    scene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT) {
      content = List(kittiesPanel)
    }
  }
  startKitties()
  mainFrameActor ! FullScreen

  def prepareKittyActors(): Unit = {
    for (i <- 0 until KITTIES_NUMBER) {
      val backgroundColor = COLORS(i)
      val x_position = i * (SPACE_BETWEEN_KITTIES + KITTY_WIDTH) + INITIAL_KITTIES_X
      kittyActors(i) = actorSystem.actorOf(Props(new KittyActor(i, backgroundColor, x_position, kittiesPanelActor)), name = "Kitty" + i)
      kittiesPanel.addInitialKitty()
    }
  }

  def startKitties(): Unit = {
    import actorSystem.dispatcher
    kittyActors.foreach(kitty => actorSystem.scheduler.scheduleOnce(1.second)(kitty ! NextFrame))
  }

  def getStage: JFXApp.PrimaryStage = stage
}
