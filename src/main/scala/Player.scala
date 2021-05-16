package Package

import akka.actor.{ActorRef, Props}

object Player {
  case object Click
  case object Restart
  case object Leave

  def props(game: ActorRef): Props = Props(classOf[Player], game)
}

class Player(game: ActorRef) {

}
