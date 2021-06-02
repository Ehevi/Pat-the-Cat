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
    new Image(new FileInputStream("resources/bieg1.gif")),
    new Image(new FileInputStream("resources/bieg2.gif")),
    new Image(new FileInputStream("resources/zatrzymanie.gif")),
    new Image(new FileInputStream("resources/drapanie1.gif")),
    new Image(new FileInputStream("resources/drapanie2.gif")),
    new Image(new FileInputStream("resources/ziewanie.gif")),
    new Image(new FileInputStream("resources/spanie1.gif")),
    new Image(new FileInputStream("resources/spanie2.gif")),
    new Image(new FileInputStream("resources/pobudka.gif"))
  )

  val STATE_INDEXES: Array[Int] = Array(
    1, // bieg2
    4, // drapanie2
    7, // spanie2
  )

  val FRAME_POINTS: Array[Int] = Array(
    1, // bieg1
    1, // bieg2
    10, // zatrzymanie
    -1, // drapanie1
    -1, // drapanie2
    10, // ziewanie
    -2, // spanie1
    -2, // spanie2
    10, // pobudka
  )

  val COLORS: Array[Color] = Array(
    Color.Pink, Color.AliceBlue, Color.Beige, Color.Tomato, Color.Crimson
  )

  val ANIMATION_LENGTH: Int = ANIMATION_FRAMES.length

  object KITTY_X_MATCHER {
    def firstKittyMatched(x: Double): Boolean = {
      INITIAL_KITTIES_X < x && x < (KITTY_WIDTH + INITIAL_KITTIES_X)
    }

    def secondKittyMatched(x: Double): Boolean = {
      (SPACE_BETWEEN_KITTIES + INITIAL_KITTIES_X + KITTY_WIDTH) < x &&
        x < (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES + KITTY_WIDTH * 2)
    }

    def thirdKittyMatched(x: Double): Boolean = {
      (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 2 + KITTY_WIDTH * 2) < x &&
        x < (SPACE_BETWEEN_KITTIES * 2 + INITIAL_KITTIES_X + KITTY_WIDTH * 3)
    }

    def fourthKittyMatched(x: Double): Boolean = {
      (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 3 + KITTY_WIDTH * 3) < x &&
        x < (SPACE_BETWEEN_KITTIES * 3 + INITIAL_KITTIES_X + KITTY_WIDTH * 4)
    }

    def fifthKittyMatched(x: Double): Boolean = {
      (INITIAL_KITTIES_X + SPACE_BETWEEN_KITTIES * 4 + KITTY_WIDTH * 4) < x &&
        x < (WINDOW_WIDTH - SPACE_BETWEEN_KITTIES)
    }
  }


}
