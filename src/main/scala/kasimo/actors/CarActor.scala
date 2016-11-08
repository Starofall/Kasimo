package kasimo.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import kasimo.GraphNetwork.Network
import kasimo.actors.InterSectionActor.{ArrivedAtIntersection, DriveIntoStreetRequest}
import kasimo.simulation.TickScheduler.SimTick

import scala.util.Random

/**
  * Created by info on 04.11.2016.
  */
class CarActor(id: String, network: Network, stateKeeper: ActorRef) extends Actor with ActorLogging {

  var simTickCounter = 0
  val mySpeed = Random.nextFloat()/200 + 0.001f

  def receive: Receive = {

    case SimTick() => simTickCounter += 1

    case ArrivedAtIntersection(list) =>
//      log.info(s"${self.path.name} reached end in $simTickCounter: options ${list.map(_.path.name)}")
      simTickCounter = 0
      sender() ! DriveIntoStreetRequest(Random.shuffle(list).head,mySpeed)
    //    case RoadEnded(crossing) => println("yeah finished!")
    case x => //println(x)

  }
}
