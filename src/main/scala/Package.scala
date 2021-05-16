import scalafx.scene.image.Image

import java.io.FileInputStream

package object Package {
  val ACTOR_SYSTEM_NAME = "KittyActorSystem"
  val INITIAL_TIMER = 1000

  val ANIMATION_FRAMES: Array[Image] = Array(
    new Image(new FileInputStream("resources\\bieg1.gif")),
    new Image(new FileInputStream("resources\\bieg2.gif")),
    new Image(new FileInputStream("resources\\drapanie1.gif")),
    new Image(new FileInputStream("resources\\drapanie2.gif")),
    new Image(new FileInputStream("resources\\pobudka.gif")),
    new Image(new FileInputStream("resources\\spanie1.gif")),
    new Image(new FileInputStream("resources\\spanie2.gif")),
    new Image(new FileInputStream("resources\\zatrzymanie.gif")),
    new Image(new FileInputStream("resources\\ziewanie.gif"))
  )

  val ANIMATION_LENGTH: Int = ANIMATION_FRAMES.length

}
