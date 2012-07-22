package de.schauderhaft.sprain
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.server.setup.MockMvcBuilders
import org.springframework.test.web.server.request.MockMvcRequestBuilders._
import org.springframework.test.web.server.result.MockMvcResultMatchers._
import org.mockito.Mockito._
import org.springframework.test.web.server.RequestBuilder

@RunWith(classOf[JUnitRunner])
class GraphControllerMappingTest extends FunSuite with MockitoSugar {
  import org.scalatest.matchers.ShouldMatchers._

  test("/ is mapped to home method") {

    val controller = new GraphController(new InMemoryStore())

    MockMvcBuilders.standaloneSetup(controller).build().
      perform(get("/")).
      andExpect(status().isOk())
    //.andExpect(content().`type`("text/html; charset=UTF-8"))
  }

  test("delete /nodes/23  is mapped to deleteNode method") {
    testRequest(delete("/nodes/23"), _.deleteNode("23"))
  }

  test("post /nodes/23/delete  is mapped to deleteNode method") {
    testRequest(post("/nodes/23/delete"), _.deleteNode("23"))
  }

  test("delete /links/23  is mapped to deleteLink method") {
    testRequest(delete("/links/23"), _.deleteLink("23"))
  }

  test("post /links/23/delete  is mapped to deleteNode method") {
    testRequest(post("/links/23/delete"), _.deleteLink("23"))
  }

  def testRequest(request: RequestBuilder, expectedMethod: GraphController => Unit) {
    val controller = mock[GraphController]

    MockMvcBuilders.standaloneSetup(controller).build().
      perform(request).
      andExpect(status().isOk())
    //.andExpect(content().`type`("text/html; charset=UTF-8"))

    expectedMethod(verify(controller))
  }
}
