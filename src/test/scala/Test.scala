import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import kitties.{ChangeFrame, Clicked, KittiesPanel, KittiesPanelActor, KittyActor, NextFrame, Start}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.must.Matchers
import scalafx.scene.paint.Color

import scala.concurrent.duration._
import scala.language.postfixOps

class Test extends TestKit(ActorSystem("test-system"))
  with AnyFlatSpecLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Kitty Actor" should "not react on handleClick when not active" in {
    val probe = TestProbe()
    val kittiesPanel = new KittiesPanel
    val kittiesPanelActor = system.actorOf(Props(new KittiesPanelActor(kittiesPanel)))
    val kittyActor = system.actorOf(Props(new KittyActor(0, Color.Blue, 0, kittiesPanelActor) {
      override def handleClick(): Unit = {
        probe.ref ! "msg"
      }
    }))

    kittyActor ! Clicked
    probe.expectNoMessage(1000 millis)
  }

  it should "react on handleClick when active" in {
    val probe = TestProbe()
    val kittiesPanel = new KittiesPanel
    val kittiesPanelActor = system.actorOf(Props(new KittiesPanelActor(kittiesPanel)))
    val kittyActor = system.actorOf(Props(new KittyActor(0, Color.Blue, 0, kittiesPanelActor) {
      override def handleClick(): Unit = {
        probe.ref ! "msg"
      }
    }))

    kittyActor ! Start

    kittyActor ! Clicked

    probe.expectMsg("msg")
  }

  it should "schedule NextFrame on NextFrame message when active" in {
    val probe = TestProbe()
    val kittiesPanel = new KittiesPanel
    val kittiesPanelActor = system.actorOf(Props(new KittiesPanelActor(kittiesPanel)))
    val kittyActor = system.actorOf(Props(new KittyActor(0, Color.Blue, 0, kittiesPanelActor) {
      override def handleFrameChange(hasStarted: Boolean): Unit = {
        import system.dispatcher
        system.scheduler.scheduleOnce(1000.millis)(probe.ref ! NextFrame)
      }
    }))

    kittyActor ! Start

    kittyActor ! NextFrame

    probe.expectMsg(NextFrame)
    probe.expectNoMessage()
  }
}
