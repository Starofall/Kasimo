package kasimo

/**
  * Created by info on 04.11.2016.
  */
object GraphNetwork {

  case class Node(id: String)

  case class Edge(id: String, from: Node, to: Node)

  case class Network(nodes: List[Node], edges: List[Edge]) {

    def nodeOutgoing(node: Node): List[Edge] = edges.filter(_.from == node)

  }

  var network = Network(List(
    Node("a"),
    Node("b")
  ), List(
    Edge("ab", Node("a"), Node("b")),
    Edge("ba", Node("b"), Node("a"))
  ))


  def test(): Unit = {
    println("WORKS:" + network.nodeOutgoing(Node("a")))
  }

}
