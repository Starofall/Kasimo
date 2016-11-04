package kasimo.network

import akka.actor.{Actor, ActorLogging, Props}
import spray.can.Http


final case class Push(msg: String)
final case class PushToChildren(msg: String)

/**
  * Created by info on 04.11.2016.
  */
object WebSocketServer {
  def props() = Props(classOf[WebSocketServer])
}

class WebSocketServer extends Actor with ActorLogging {
  def receive = {
    // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()
      val conn = context.actorOf(WebSocketWorker.props(serverConnection))
      serverConnection ! Http.Register(conn)
    case PushToChildren(msg: String) =>
      val children = context.children
//      println("pushing to all children : " + msg)
      children.foreach(ref => ref ! Push(msg))
  }
}