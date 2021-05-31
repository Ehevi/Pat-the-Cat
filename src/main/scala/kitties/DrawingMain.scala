package kitties

import scalafx.Includes._
import akka.actor.{ActorRef, ActorSystem, Props}
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.BorderPane
import scalafx.application.Platform
import scalafx.scene.text.Font

import scala.concurrent.duration.DurationInt
import scala.sys.exit

object DrawingMain extends JFXApp {
  private val kittiesPanel = new KittiesPanel

  private val actorSystem = ActorSystem("ActorSystem")
  private val kittiesPanelActor = actorSystem.actorOf(Props(new KittiesPanelActor(kittiesPanel)), name = "KittiesPanelActor")

  private val kittyActors = new Array[ActorRef](KITTIES_NUMBER)
  private var score = 0
  private var time: Double = 60
  var timeText: String = "%.3f".format(time)
  private var lastTimer: Double = 0
  private val label = new Label("Score: " + score)
  private val timerLabel = new Label("Time: " + ((time*1000).round / 1000.toDouble)) {
  }
  timerLabel.setFont(Font.font("consolas"))
  private var hasStarted = false

  val timer: AnimationTimer = AnimationTimer ( t => {
    val delta = (t.toDouble - lastTimer) / 1e9
    if (delta < 1) time -= delta
    timeText = "%.3f".format(time)
    if (time > 0) timerLabel.text = "Time: " + timeText
    else {
      time = 0
      timeText = "%.3f".format(time)
      timerLabel.text = "Time: " + timeText
      stopGame()
    }
    lastTimer = t
  })

  val startButton = new Button("Start!")
  startButton.onAction = { _ =>
    if (hasStarted) {
      stopGame()
    } else {
      time = 60
      lastTimer = 0
      timeText = "%.3f".format(time)
      timerLabel.text = "Time: " + timeText
      timer.start()
      val negativeScore = 0 - this.score
      updateScore(negativeScore)
      hasStarted = true
      for (i <- 0 until KITTIES_NUMBER) {
        kittiesPanelActor ! ChangeFrame(i, 0)
      }
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
          left = label
          right = timerLabel
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

  override def stopApp(): Unit = {
    actorSystem.terminate()
    exit(0)
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

  def stopGame(): Unit = {
    kittyActors.foreach(kitty => kitty ! Stop)
    hasStarted = false
    timer.stop()
    startButton.text = "Start!"
  }
}