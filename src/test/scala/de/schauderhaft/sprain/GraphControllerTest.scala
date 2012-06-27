package de.schauderhaft.sprain
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import de.schauderhaft.sprain.store.Store
import de.schauderhaft.sprain.model.Node
import de.schauderhaft.sprain.model.Link

@RunWith(classOf[JUnitRunner])
class GraphControllerTest extends FunSuite {
    import org.scalatest.matchers.ShouldMatchers._

    test("home renders as graph") {
        val entrance = new GraphController(new InMemoryStore())
        entrance.home.getViewName() should be("graph")
    }

    test("home model contains an empty set of nodes") {
        val entrance = new GraphController(new InMemoryStore())
        entrance.home.getModel.get("nodes") should be(Set())
    }

    test("home model contains an empty set of links") {
        val entrance = new GraphController(new InMemoryStore())
        entrance.home.getModel.get("links") should be(Set())
    }

    test("add node adds the node to the store") {
        val store = new InMemoryStore()
        val entrance = new GraphController(store)

        entrance.add("alpha")

        store.nodes.values.map(_.name) should equal(List("alpha"))
    }

    test("add link adds the link and nodes to the store") {
        val store = new InMemoryStore()
        val entrance = new GraphController(store)

        entrance.addLink("alpha", "into", "beta")

        store.nodes.values.map(_.name).toSet should be(Set("alpha", "beta"))
        store.links.values.map(l => (l.from.name, l.link, l.to.name)).toSet should be(Set(("alpha", "into", "beta")))
    }

    test("delete node deletes a node from the store") {
        pending
    }

    test("delete node deletes a node and its attached links from the store") {
        pending
    }

    test("deleting a link deletes the link from the store, but not its nodes") {
        pending
    }

    class InMemoryStore extends Store {
        import java.util.UUID
        var nodes = Map[String, Node]()
        var links = Map[String, Link]()

        def allNodes() = nodes.values.toSet

        def allLinks() = links.values.toSet

        /** adds a node to the store */
        def add(node : String) = {
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
        def add(from : String, link : String, to : String) = {
            val id = UUID.randomUUID.toString
            links += (id -> Link(id, nodes(from), link, nodes(to)))
            id
        }

        /** removes a node from the store, does nothing when the node is not present in the store*/
        def deleteNode(node : String) {
            nodes -= node
        }

        /** removes a link from the store, does nothing when the node is not present in the store*/
        def deleteLink(id : String) {
            links -= (id)
        }
    }
}