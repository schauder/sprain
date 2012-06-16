package de.schauderhaft.knoweb.store

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalaquery.session._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql.basic.BasicDriver.Implicit._

@RunWith(classOf[JUnitRunner])
class StoreTest extends FunSuite {
    import org.scalatest.matchers.ShouldMatchers._

    val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    db withSession {
        Nodes.ddl create
    }

    test("new store does not contain nodes") {
        val store = new Store(db)
        store.allNodes should equal(Set())
    }

    test("new store does not contain links") {
        val store = new Store(db)
        store.allLinks should equal(Set())
    }

    test("a store contains nodes added to it") {
        val store = new Store(db)
        store.add("a")
        store.add("b")

        store.allNodes should equal(Set("a", "b"))
    }

    test("nodes in a store can be retrieved using a different store") {
        val store = new Store(db)
        store.add("a")
        store.add("b")

        new Store(db).allNodes should equal(Set("a", "b"))
    }

}