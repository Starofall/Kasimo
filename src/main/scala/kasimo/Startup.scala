package kasimo

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import kasimo.actors.InterSectionActor.{DriveInRequest, PossibleTarget}
import kasimo.actors.{CarActor, InterSectionActor}
import kasimo.network.WebSocketServer
import kasimo.simulation.TickScheduler.RegisterActor
import kasimo.simulation.{StateKeeper, TickScheduler}
import spray.can.Http
import spray.can.server.UHttp

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

object Startup extends App {

  val system = ActorSystem("world")
  println("################")

  val webSocketMaster = system.actorOf(WebSocketServer.props(), "websocket")
  IO(UHttp)(system) ! Http.Bind(webSocketMaster, "localhost", 9898)

  val stateKeeper = system.actorOf(Props(new StateKeeper(webSocketMaster)), name = s"stateKeeper")
  val scheduler = system.actorOf(Props(new TickScheduler("tick")), name = s"tick")

  val nodeA = system.actorOf(Props(new InterSectionActor("a", stateKeeper)), name = s"a")
  val nodeB = system.actorOf(Props(new InterSectionActor("b", stateKeeper)), name = s"b")
  val nodeC = system.actorOf(Props(new InterSectionActor("c", stateKeeper)), name = s"c")
  val nodeD = system.actorOf(Props(new InterSectionActor("d", stateKeeper)), name = s"d")

  scheduler ! RegisterActor(nodeA)
  scheduler ! RegisterActor(nodeB)
  scheduler ! RegisterActor(nodeC)
  scheduler ! RegisterActor(nodeD)

  nodeA ! PossibleTarget(nodeB)
  nodeA ! PossibleTarget(nodeD)
  nodeB ! PossibleTarget(nodeC)
  nodeC ! PossibleTarget(nodeB)
  nodeC ! PossibleTarget(nodeD)
  nodeD ! PossibleTarget(nodeA)
  nodeD ! PossibleTarget(nodeB)




//
//  val car2 = system.actorOf(Props(new CarActor(s"car-2", stateKeeper)), name = s"car-2")
//  nodeB ! DriveInRequest(car2)
//  scheduler ! RegisterActor(car2)
//
//  val car3 = system.actorOf(Props(new CarActor(s"car3", stateKeeper)), name = s"car3")
//  nodeC ! DriveInRequest(car3)
//  scheduler ! RegisterActor(car3)


  Future{
    val nodes = List(nodeA,nodeB,nodeC,nodeD)
    for(i<- 0 to 100){
      val car = system.actorOf(Props(new CarActor(s"car-$i", stateKeeper)), name = s"car-$i")
      Random.shuffle(nodes).head ! DriveInRequest(car)
      scheduler ! RegisterActor(car)
      Thread.sleep(100)
    }

  }

  //    GraphNetwork.test()
}





