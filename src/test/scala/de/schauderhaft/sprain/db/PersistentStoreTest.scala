package de.schauderhaft.sprain.db

import org.junit.runner.RunWith

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.session._
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import de.schauderhaft.sprain.db.schema.Links
import de.schauderhaft.sprain.db.schema.Nodes
import de.schauderhaft.sprain.model.Link

@RunWith(classOf[JUnitRunner])
class PersistentStoreTest extends FunSuite with BeforeAndAfter {
    import org.scalatest.matchers.ShouldMatchers._

    val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    db withSession {
        val ddl = (Nodes.ddl ++ Links.ddl)
        ddl.create
        println(ddl.createStatements.mkString("\n"))
    }

    def txtest(name: String)(t: => Any) {
        test(name) {
            db withTransaction {
                try t
                finally threadLocalSession.rollback
            }
        }
    }

    txtest("new store does not contain nodes") {
        val store = new PersistentStore()
        store.allNodes should equal(Set())
    }

    txtest("a store contains nodes added to it") {
        val store = new PersistentStore()
        store.add("a")
        store.add("b")

        store.allNodes.map(_.name) should equal(Set("a", "b"))
    }

    txtest("nodes in a store can be retrieved using a different store") {
        val store = new PersistentStore()
        store.add("a")
        store.add("b")

        new PersistentStore().allNodes.map(_.name).toSet should equal(Set("a", "b"))
    }

    txtest("adding a node is a projection") {
        val store = new PersistentStore()
        val a1Id = store.add("a")
        val a2Id = store.add("a")

        a1Id should equal(a2Id)
        store.allNodes.map(_.name) should equal(Set("a"))
    }

    txtest("new store does not contain links") {
        val store = new PersistentStore()
        store.allLinks should equal(Set())
    }

    txtest("deleting a node removes the node") {
        val store = new PersistentStore()
        val id = store.add("a")
        store.add("b")
        store.deleteNode(id);
        store.allNodes.map(_.name) should equal(Set("b"))
    }

    txtest("deleting a node from an empty store does nothing") {
        val store = new PersistentStore()
        store.deleteNode("a");
        store.allNodes should equal(Set())
    }

    txtest("deleting a link removes the link") {
        val store = new PersistentStore()
        val fromId = store.add("from")
        val toId = store.add("to")
        val toOtherId = store.add("toOther")

        val id = store.add(fromId, "verb", toId)
        store.add(fromId, "verb", toOtherId)
        store.deleteLink(id)

        store.allLinks.map(l => (l.from.name, l.link, l.to.name)) should equal(Set(("from", "verb", "toOther")))
    }

    txtest("deleting a link from an empty store does nothing") {
        val store = new PersistentStore()
        store.deleteLink("IdontEvenHaveAuuid")

        store.allLinks should equal(Set())
    }

    txtest("a store contains the links added to it") {
        val store = new PersistentStore()

        val aId = store.add("a")
        val bId = store.add("b")
        val cId = store.add("c")

        store.add(aId, "to", bId)

        store.allLinks.map(l => (l.from.name, l.link, l.to.name)) should equal(Set(("a", "to", "b")))
    }

    txtest("allForNode on an empty store returns the empty Set") {
        val store = new PersistentStore()

        store.allForNode("23") should equal(Set())
    }

    txtest("allForNode on a from-Node returns the Set with that link") {
        val store = new PersistentStore()

        val aId = store.add("a")
        val bId = store.add("b")
        val cId = store.add("c")

        store.add(aId, "to", bId)

        names(store.allForNode(aId)) should equal(Set(("a", "to", "b")))
    }

    txtest("allForNode on a to-Node returns the Set with that link") {
        val store = new PersistentStore()

        val aId = store.add("a")
        val bId = store.add("b")
        val cId = store.add("c")

        store.add(aId, "to", bId)

        names(store.allForNode(bId)) should equal(Set(("a", "to", "b")))
    }

    txtest("allForNode on a not connected Node returns the empty Set") {
        val store = new PersistentStore()

        val aId = store.add("a")
        val bId = store.add("b")
        val cId = store.add("c")

        store.add(aId, "to", bId)

        names(store.allForNode(cId)) should equal(Set())
    }

    def names(t: Set[Link]) = t.map(l => (l.from.name, l.link, l.to.name))
}
