import scalafx.scene.image.Image
import scalafx.scene.paint.Color

import java.io.FileInputStream

package object kitties {
  val INITIAL_TIMER = 1000

  val KITTIES_NUMBER = 5

  val WINDOW_HEIGHT = 250
  val WINDOW_WIDTH = 620
  val KITTY_WIDTH = 100
  val KITTY_HEIGHT = 100
  val SPACE_BETWEEN_KITTIES = 20
  val INITIAL_KITTIES_X = 20

  val KITTY_ACTOR_NAMES: Array[String] = Array(
    "Kitty1",
    "Kitty2",
    "Kitty3",
    "Kitty4",
    "Kitty5",
  )

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

  val FRAME_POINTS: Array[Int] = Array(
    1, // bieg1
    1, // bieg2
    -1, // drapanie1
    -1, //drapanie2
    10, // pobudka
    -2, // spanie1
    -2, // spanie2
    10, // zatrzymanie
    10, // ziewanie
  )

  val COLORS: Array[Color] = Array(
    Color.Pink, Color.AliceBlue, Color.Beige, Color.Tomato, Color.Crimson
  )

  val ANIMATION_LENGTH: Int = ANIMATION_FRAMES.length

}
