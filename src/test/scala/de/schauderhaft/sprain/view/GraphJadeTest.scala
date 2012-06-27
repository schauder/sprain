package de.schauderhaft.sprain.view

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.fusesource.scalate.TemplateEngine
import de.schauderhaft.sprain.model.Node
import de.schauderhaft.sprain.model.Link

@RunWith(classOf[JUnitRunner])
class GraphJadeTest extends FunSuite {
    import org.scalatest.matchers.ShouldMatchers._

    val engine = new TemplateEngine

    test("rendering without parameter") {
        val output = engine.layout("src/main/webapp/WEB-INF/views/graph.jade", Map("name" -> ("Hiram", "Chirino"), "city" -> "Tampa"))

    }
    test("rendering nodes") {
        val output = engine.layout("src/main/webapp/WEB-INF/views/graph.jade",
            Map("nodes" -> Set(
                Node("1", "alpha"),
                Node("2", "beta"))))

        output should include("1")
        output should include("2")
        output should include("alpha")
        output should include("beta")

    }

    test("rendering links") {
        pending
        val output = engine.layout("src/main/webapp/WEB-INF/views/graph.jade",
            Map("nodes" -> Set(
                Link("1", "likes", "2"),
                Link("1", "loves", "2"))))

        output should include("1")
        output should include("2")
        output should include("alpha")
        output should include("beta")
    }
}