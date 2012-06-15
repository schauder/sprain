package de.schauderhaft.knoweb
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EntranceTest extends FunSuite {
    import org.scalatest.matchers.ShouldMatchers._

    test("home renders as graph") {
        val entrance = new Entrance
        entrance.home.getViewName() should be("graph")
    }

    test("home model contains an empty set of nodes") {
        val entrance = new Entrance
        entrance.home.getModel() should contain key ("nodes")
        entrance.home.getModel.get("nodes") should be(Set())
    }
}