package kitties

import scalafx.Includes._
import akka.actor.{ActorRef, ActorSystem, Props}
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.BorderPane

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
      root = new BorderPane() {
        top = new BorderPane() {
          prefHeight = 50
          center = new Label("Score:")
        }
        center = kittiesPanel
        bottom = new BorderPane() {
          prefHeight = 100
          center = new Button("Start Again")
        }
      }
    }
  }
  startKitties()

  def prepareKittyActors(): Unit = {
    for (i <- 0 until KITTIES_NUMBER) {
      val backgroundColor = COLORS(i)
      val x_position = i * (SPACE_BETWEEN_KITTIES + KITTY_WIDTH) + INITIAL_KITTIES_X
      kittyActors(i) = actorSystem.actorOf(Props(new KittyActor(i, backgroundColor, x_position, kittiesPanelActor)), name = "Kitty" + i)
      kittiesPanel.addInitialKitty(backgroundColor)
    }
  }

  def startKitties(): Unit = {
    import actorSystem.dispatcher
    kittyActors.foreach(kitty => actorSystem.scheduler.scheduleOnce(1000.millis)(kitty ! NextFrame))
  }

  def getStage: JFXApp.PrimaryStage = stage
}
