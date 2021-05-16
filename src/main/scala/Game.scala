package Package

import akka.actor.{Actor, Props}

object Game {
  case object Start

  def props(): Props = Props(classOf[Game])

  type MatrixOfActors = IndexedSeq[IndexedSeq[KittyActor.State]]
}

class Game extends Actor {
  var score = 0
  val player = context.actorOf(Player.props(self))

  override def receive: Receive = {
    case Player.Click => {

    }
    case Player.Restart => {

    }
    case Player.Leave => context.system.terminate()
  }
}
