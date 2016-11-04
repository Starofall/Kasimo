package kasimo.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import kasimo.actors.InterSectionActor.{ArrivedAtIntersection, DriveIntoStreetRequest}

import scala.util.Random

/**
  * Created by info on 04.11.2016.
  */
class CarActor(id: String, stateKeeper: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case ArrivedAtIntersection(list) => sender() ! DriveIntoStreetRequest(Random.shuffle(list).head)
    //    case RoadEnded(crossing) => println("yeah finished!")
    case x => //println(x)

  }
}
