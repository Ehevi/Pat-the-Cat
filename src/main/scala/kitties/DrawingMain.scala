package kitties

import kitties.ActorManager.{getKittiesPanel, prepareKittyActors, startKitties, stopAll, terminateActorSystem}
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.BorderPane
import scalafx.application.Platform
import scalafx.scene.text.Font
import scala.sys.exit

object DrawingMain extends JFXApp {
  private var score = 0
  private var time: Double = 60
  private var timeText: String = "%.3f".format(time)
  private var lastTimer: Double = 0
  private var hasStarted = false

  private val label = new Label("Score: " + score)

  private val timerLabel = new Label("Time: " + ((time*1000).round / 1000.toDouble)) {
    font = Font.font("consolas")
  }

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
      startKitties()
      startButton.text = "Stop!"
    }
  }

  prepareKittyActors()

  stage = new JFXApp.PrimaryStage {
    title = "Pat the Cat"
    scene = new Scene(WINDOW_WIDTH, WINDOW_HEIGHT) {
      root = new BorderPane() {
        top = new BorderPane() {
          prefHeight = 50
          left = label
          right = timerLabel
        }
        center = getKittiesPanel
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
    terminateActorSystem()
    exit(0)
  }

  def getStage: JFXApp.PrimaryStage = stage

  def updateScore(addScore: Int): Unit = {
    this.score = this.score + addScore
    Platform.runLater {
      label.setText("Score: " + score)
    }
  }

  def stopGame(): Unit = {
    stopAll()
    hasStarted = false
    timer.stop()
    startButton.text = "Start!"
  }
}