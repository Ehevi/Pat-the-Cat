import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object DrawingMain extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Program"
    scene = new Scene(800, 600) {
      val button = new Button("Click Me!")
      button.layoutX = 100
      button.layoutY = 100
      val rect: Rectangle = Rectangle(400, 200, 100, 150)
      rect.fill = Color.Pink
      content = List(button, rect)
    }
  }
}
