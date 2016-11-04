package kasimo.simulation

import akka.actor.{Actor, ActorLogging, ActorRef}
import kasimo.network.PushToChildren

/**
  * Created by info on 04.11.2016.
  */
class StateKeeper(webSocketMaster: ActorRef) extends Actor with ActorLogging {
  def receive: Receive = {
    case x: String => webSocketMaster ! PushToChildren(x)
  }
}
