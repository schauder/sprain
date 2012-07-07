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

        nodeNames(store.nodes) should equal(Set("alpha"))
    }

    test("add link adds the link does nothing when ids don't exist") {
        val store = new InMemoryStore()
        val entrance = new GraphController(store)

        entrance.addLink("alpha", "into", "beta")

        nodeNames(store.nodes) should be(Set())
        linkNames(store.links) should be(Set())
    }

    test("add link adds the link to the store") {
        val store = new InMemoryStore()
        val aId = store.add("a")
        val bId = store.add("b")

        val entrance = new GraphController(store)

        entrance.addLink(aId, "into", bId)

        nodeNames(store.nodes) should be(Set("a","b"))
        linkNames(store.links) should be(Set(("a","into","b")))
    }

    
    test("delete node redirects to home") {
        val store = new InMemoryStore()
        val controller = new GraphController(store)

        val result = controller.deleteNode("23")

        result should equal("redirect:/")
    }

    test("delete node deletes a node") {
        val store = new InMemoryStore()
	val id = store.add("eins")
        store.add("other")
        val controller = new GraphController(store)

        controller.deleteNode(id)

        nodeNames(store.nodes) should be(Set("other"))
    }

    test("delete node deletes a node and its attached links from the store") {
        val store = new InMemoryStore()

	val id = store.add("eins")
	val fromId = store.add("from")
        val toId = store.add("to")

	store.add(fromId, "should stay", toId)
        store.add(fromId, "should go", id)
	store.add(id, "should go as well", toId)

        val controller = new GraphController(store)

        controller.deleteNode(id)

        nodeNames(store.nodes) should be(Set("from", "to"))
        linkNames(store.links) should be(Set(("from", "should stay", "to")))
    }

    test("deleting a link deletes the link from the store, but not its nodes") {
                val store = new InMemoryStore()

	val fromId = store.add("from")
        val toId = store.add("to")

	val id = store.add(fromId, "one", toId)

        val controller = new GraphController(store)

        controller.deleteLink(id)

        nodeNames(store.nodes) should be(Set("from", "to"))
        linkNames(store.links) should be(Set())
    }

    private def nodeNames(nodes : Map[_, Node]) = nodes.values.map(_.name).toSet
    private def linkNames(links : Map[_, Link]) = 
        links.values.map(l => (l.from.name, l.link, l.to.name)).toSet
    

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
        def deleteNode(id : String) {
            nodes -= id
        }

        /** removes a link from the store, does nothing when the node is not present in the store*/
        def deleteLink(id : String) {
            links -= (id)
        }

        def allForNode(nodeId : String) : Set[Link] = for {
	    l <- allLinks
            if l.from.id == nodeId || l.to.id == nodeId
        } yield l
    }
}
