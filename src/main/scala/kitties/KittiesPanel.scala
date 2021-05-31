package kitties

import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.effect.{Light, Lighting}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color

class KittiesPanel extends HBox {
  spacing = SPACE_BETWEEN_KITTIES
  prefWidth = WINDOW_WIDTH
  prefHeight = KITTY_HEIGHT
  style = "-fx-background-color: #FFFAFA;"
  alignment = Pos.Center

  def addInitialKitty(backgroundColor: Color): Unit = {
    this.children += new KittyImageView(backgroundColor)
  }

  def changeKittyFrame(kittyIndex: Int, frameIndex: Int): Unit = {
    try {
      val child = jfxImageView2sfx(this.children(kittyIndex).asInstanceOf[javafx.scene.image.ImageView])
      child.image = ANIMATION_FRAMES(frameIndex)
    } catch {
      case e: Throwable => println(e.getMessage)
    }
  }
}

class KittyImageView(val backgroundColor: Color) extends ImageView {
  fitWidth = KITTY_WIDTH
  fitHeight = KITTY_HEIGHT
  image = ANIMATION_FRAMES(0)
  cache = true
  smooth = true
  effect = new Lighting {
    diffuseConstant = 1.0
    specularConstant = 0.0
    specularExponent = 0.0
    surfaceScale = 0.0
    light = new Light.Distant(45, 45, backgroundColor)
  }
}
