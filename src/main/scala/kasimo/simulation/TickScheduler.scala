package kasimo.simulation

import akka.actor.{Actor, ActorLogging, ActorRef}
import kasimo.simulation.TickScheduler.{RegisterActor, RenderTick, SimTick}

import scala.concurrent.duration.Duration
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by info on 04.11.2016.
  */
class TickScheduler(id: String) extends Actor with ActorLogging {

  var actors = List[ActorRef]()

  val simInterval = 10 /* milliseconds */ * 1000 * 1000
  context.system.scheduler.schedule(Duration.fromNanos(Random.nextInt(simInterval)), Duration.fromNanos(simInterval)) {
    actors.foreach(_ ! SimTick())
  }

  val renderInterval = 10 /* milliseconds */  * 1000 * 1000
  context.system.scheduler.schedule(Duration.fromNanos(Random.nextInt(renderInterval)), Duration.fromNanos(renderInterval)) {
    actors.foreach(_ ! RenderTick())
  }

  def receive: Receive = {
    case RegisterActor(x) => actors ::= x
    case x => //println(x)
  }
}

object TickScheduler {

  case class SimTick()

  case class RenderTick()

  case class RegisterActor(actorRef: ActorRef)

}