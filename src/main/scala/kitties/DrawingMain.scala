package kitties

import scalafx.Includes._
import akka.actor.{ActorRef, ActorSystem, Props}
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.BorderPane
import scalafx.application.Platform

import scala.concurrent.duration.DurationInt

object DrawingMain extends JFXApp {
  private val kittiesPanel = new KittiesPanel

  private val actorSystem = ActorSystem("ActorSystem")
  private val kittiesPanelActor = actorSystem.actorOf(Props(new KittiesPanelActor(kittiesPanel)), name = "KittiesPanelActor")
  private val mainFrameActor = actorSystem.actorOf(Props(new MainFrameActor(kittiesPanel)), name = "MainFrameActor")

  private val kittyActors = new Array[ActorRef](KITTIES_NUMBER)
  private var score = 0
  private val label = new Label("Score: " + score)
  private var hasStarted = false

  val startButton = new Button("Start!")
  startButton.onAction = { _ =>
    if (hasStarted) {
      kittyActors.foreach(kitty => kitty ! Stop)
      hasStarted = false
      startButton.text = "Start!"
    } else {
      val negativeScore = 0 - this.score
      updateScore(negativeScore)
      hasStarted = true
      startKitties()
      kittyActors.foreach(kitty => kitty ! Start)
      startButton.text = "Stop!"
    }
  }

  prepareKittyActors()
  stage = new JFXApp.PrimaryStage {
    title = "Kitties Game"
    scene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT) {
      root = new BorderPane() {
        top = new BorderPane() {
          prefHeight = 50
          center = label
        }
        center = kittiesPanel
        bottom = new BorderPane() {
          prefHeight = 100
          center = new BorderPane() {
            center = startButton
          }
        }
      }
    }
  }

  def prepareKittyActors(): Unit = {
    for (i <- 0 until KITTIES_NUMBER) {
      val backgroundColor = COLORS(i)
      val x_position = i * (SPACE_BETWEEN_KITTIES + KITTY_WIDTH) + INITIAL_KITTIES_X
      kittyActors(i) = actorSystem.actorOf(Props(new KittyActor(i, backgroundColor, x_position, kittiesPanelActor)),
        name = KITTY_ACTOR_NAMES(i))
      println(kittyActors(i))
      kittiesPanel.addInitialKitty(backgroundColor)
    }
  }

  def startKitties(): Unit = {
    import actorSystem.dispatcher
    kittyActors.foreach(kitty => actorSystem.scheduler.scheduleOnce(1000.millis)(kitty ! NextFrame))
  }

  def getStage: JFXApp.PrimaryStage = stage

  def updateScore(addScore: Int): Unit = {
    this.score = this.score + addScore
    Platform.runLater {
      label.setText("Score: " + score)
    }
  }
}
