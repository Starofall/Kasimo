package kasimo

/**
  * Created by info on 04.11.2016.
  */
object GraphNetwork {

  sealed trait NodeTypes

  case class GasStation() extends NodeTypes

  case class Intersection() extends NodeTypes

  case class NodePosition(x: Int, y: Int)


  case class Node(id: String, nodeTypes: NodeTypes, position: NodePosition)

  case class Edge(id: String, from: Node, to: Node)

  case class Network(nodes: List[Node], edges: List[Edge]) {

    def nodeOutgoing(node: Node): List[Edge] = edges.filter(_.from == node)

  }


  val a = Node("a", Intersection(), NodePosition(0, 0))
  val b = Node("b", GasStation(), NodePosition(200, 200))
  val c = Node("c", Intersection(), NodePosition(200, 50))
  val d = Node("d", Intersection(), NodePosition(150, -40))
  val e = Node("e", Intersection(), NodePosition(320, 260))
  val f = Node("f", Intersection(), NodePosition(150, 280))
  var network = Network(List(a, b,c,d,e,f), List(
    Edge("ab", a, b),
    Edge("ba", b, a),
    Edge("ac", a, c),
    Edge("ad", a, d),
    Edge("dc", d, c),
    Edge("ce", c, e),
    Edge("eb", e, b),
    Edge("ef", e, f),
    Edge("fa", f, a),
    Edge("cb", c, b)
  ))


}
