package de.schauderhaft.sprain

import de.schauderhaft.sprain.store.Store
import de.schauderhaft.sprain.model.Node
import de.schauderhaft.sprain.model.Link

class InMemoryStore extends Store {
  import java.util.UUID
  var nodes = Map[String, Node]()
  var links = Map[String, Link]()

  def allNodes() = nodes.values.toSet

  def allLinks() = links.values.toSet

  /** adds a node to the store */
  def add(node: String) = {
    val nIds = for {
      (id, n) <- nodes
      if (n.name == node)
    } yield id

    nIds.headOption match {
      case Some(id) => id
      case None =>
        val id = UUID.randomUUID.toString
        nodes += id -> Node(id, node)
        id
    }
  }

  /** adds a link between to nodes to the store */
  def add(from: String, link: String, to: String) = {
    val id = UUID.randomUUID.toString
    links += (id -> Link(id, nodes(from), link, nodes(to)))
    id
  }

  /** removes a node from the store, does nothing when the node is not present in the store*/
  def deleteNode(id: String) {
    nodes -= id
  }

  /** removes a link from the store, does nothing when the node is not present in the store*/
  def deleteLink(id: String) {
    links -= (id)
  }

  def allForNode(nodeId: String): Set[Link] = for {
    l <- allLinks
    if l.from.id == nodeId || l.to.id == nodeId
  } yield l
}