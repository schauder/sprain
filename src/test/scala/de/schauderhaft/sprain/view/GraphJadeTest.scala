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

    val uri = "src/main/webapp/WEB-INF/views/graph.jade"
    val engine = new TemplateEngine
    val template = engine.load(uri)

    test("rendering without parameter") {
        val output = engine.layout(uri, template, Map[String, Any]())
    }

    test ("rendering add nodes form"){
        val output = engine.layout(uri, template, Map[String, Any]())

	output should include regex pattern("form nodes/new")
	output should include regex pattern("input nodeName")
	output should include regex pattern("input submit")
    }

    test("rendering nodes") {
        val output = engine.layout(uri, template,
            Map("nodes" -> Set(
                Node("1", "alpha"),
                Node("2", "beta"))))

        output should include("1")
        output should include("2")
        output should include("alpha")
        output should include("beta")
    }

    test ("rendering add link form per node"){
        pending
    }

    test ("rendering delete node form per node"){
        pending
    }

    test("rendering links") {
        val output = engine.layout(uri, template,
            Map("links" -> Set(
                Link("42", Node("1", "one"), "likes", Node("2", "two")),
                Link("23", Node("1", "one"), "loves", Node("1", "one")))))

        output should include("1")
        output should include("2")
        output should include("likes")
        output should include("loves")
    }

    
    test ("rendering delete link form per link"){
        pending
    }

    def pattern(pat : String) = pat.replace(" ", ".*").r
}
