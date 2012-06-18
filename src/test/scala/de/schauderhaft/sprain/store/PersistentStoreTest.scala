package de.schauderhaft.sprain.store

import org.junit.runner.RunWith
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.session._
import org.scalatest.junit.JUnitRunner
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class PersistentStoreTest extends FunSuite with BeforeAndAfter {
    import org.scalatest.matchers.ShouldMatchers._

    val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    db withSession {
        val ddl = (Nodes.ddl ++ Links.ddl)
        ddl.create
        println(ddl.createStatements.mkString("\n"))
    }

    def txtest(name : String)(t : => Any) {
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

        store.allNodes should equal(Set("a", "b"))
    }

    txtest("nodes in a store can be retrieved using a different store") {
        val store = new PersistentStore()
        store.add("a")
        store.add("b")

        new PersistentStore().allNodes should equal(Set("a", "b"))
    }

    txtest("adding a node is a projection") {
        val store = new PersistentStore()
        store.add("a")
        store.add("a")

        store.allNodes should equal(Set("a"))
    }

    txtest("new store does not contain links") {
        val store = new PersistentStore()
        store.allLinks should equal(Set())
    }

    txtest("a store contains the links added to it") {
        val store = new PersistentStore()

        store.add("a")
        store.add("b")
        store.add("c")

        store.add("a", "to", "b")

        store.allLinks should equal(Set(("a", "to", "b")))
    }

}