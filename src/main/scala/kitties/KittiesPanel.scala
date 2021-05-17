package kitties

import akka.actor.ActorRef
import scalafx.scene.image.ImageView
import scalafx.scene.layout.HBox

class KittiesPanel extends HBox {
  def addInitialKitty(): Unit = {
    this.getChildren.add(new ImageView(ANIMATION_FRAMES(0)))
  }

  def changeKittyFrame(kittyIndex: Int, frameIndex: Int): Unit = {
    println(kittyIndex + " zmienia na " + frameIndex)
    val child = this.getChildren.get(kittyIndex)
    println(child)
    if (child.isInstanceOf[ImageView]) {
      println(child)
    } else {
      println("gsdfgsdf")
    }
//    this.getChildren.get(kittyIndex).asInstanceOf[ImageView].image = ANIMATION_FRAMES(frameIndex)
  }
}
