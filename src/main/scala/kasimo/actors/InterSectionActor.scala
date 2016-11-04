package kasimo.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import kasimo.actors.InterSectionActor._
import kasimo.simulation.TickScheduler.{RenderTick, SimTick}

import scala.util.Random

/**
  * Created by info on 04.11.2016.
  */
class InterSectionActor(id: String, stateKeeper: ActorRef) extends Actor with ActorLogging {

  case class RelativeCarPosition(nextInterSection: ActorRef, car: ActorRef, var done: Float)

  var carCapacity: Option[ActorRef] = None
  var outgoingRoads = List[ActorRef]()
  var carPositions = List[RelativeCarPosition]()


  def receive: Receive = {

    case DriveIntoStreetRequest(nextInterSection) =>
      carCapacity = None
      carPositions ::= RelativeCarPosition(nextInterSection, sender(), 0)

    case PossibleTarget(possibleTargetRef) =>
      outgoingRoads ::= possibleTargetRef

    case DriveInRequest(car) =>
      carCapacity match {
        case Some(x) =>
          // ignore full!
        case None =>
          carCapacity = Some(car)
          sender ! DriveInSuccess(car)
          car ! ArrivedAtIntersection(outgoingRoads)
      }

    case DriveInSuccess(car) =>
      // remove car
      carPositions = carPositions.filterNot(_.car == car)


    case SimTick() =>
      // transport tick
      carPositions.filter(_.done < 1).foreach(_.done += 0.0025f + Random.nextFloat()/1000)
      // some are finished
      val done = carPositions.filter(_.done >= 1)
      // request one drive into the intersection
      done.headOption.foreach(x => x.nextInterSection ! DriveInRequest(x.car))

    case RenderTick() =>
      val roadString = outgoingRoads.map(x => {
        val carPos = carPositions.filter(_.nextInterSection == x).map(z =>s"""{"carId": "${z.car.path.name}","pos": ${z.done}}""").mkString(",")
        s"""{"edgeId": "${self.path.name}${x.path.name}","from":"${self.path.name}","to":"${x.path.name}","carPositions": [$carPos]}"""
      }).mkString(",")
      stateKeeper ! s"""{"nodeId": "${self.path.name}","edges": [$roadString]}"""

    case x => //println(x)

  }
}

object InterSectionActor {

  case class DriveIntoStreetRequest(street: ActorRef)

  case class DriveInSuccess(car: ActorRef)

  case class DriveInRequest(car: ActorRef)

  case class PossibleTarget(possibleTarget: ActorRef)

  case class ArrivedAtIntersection(outgoingRoads: List[ActorRef])

}