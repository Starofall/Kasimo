package kasimo

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import kasimo.GraphNetwork.Node
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


  var map = Map[Node, ActorRef]()

  GraphNetwork.network.nodes.foreach(n => {
    val x = system.actorOf(Props(new InterSectionActor(n.id, n, stateKeeper)), name = s"${n.id}")
    scheduler ! RegisterActor(x)
    map += n -> x
  })

  GraphNetwork.network.edges.foreach(n => {
    map(n.from) ! PossibleTarget(map(n.to))
  })

  Future {
    val nodes = map.values
    for (i <- 0 to 39) {
      val car = system.actorOf(Props(new CarActor(s"car-$i", GraphNetwork.network, stateKeeper)), name = s"car-$i")
      Random.shuffle(nodes).head ! DriveInRequest(car)
      scheduler ! RegisterActor(car)
      Thread.sleep(100)
    }

  }

  //    GraphNetwork.test()
}





