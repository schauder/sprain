package de.schauderhaft.sprain.view

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import UrlBuilder._

@RunWith(classOf[JUnitRunner])
class UrlBuilderTest extends FunSuite {
  import org.scalatest.matchers.ShouldMatchers._

  test("form URL for delete") {
    UrlBuilder("resourceCollection", "someId", delete).formUrl should equal("resourceCollection/someId/delete")

  }
  test("rest URL for delete") {

    UrlBuilder("resourceCollection", "someId", delete).restUrl should equal("resourceCollection/someId")
  }

}