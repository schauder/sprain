package de.schauderhaft.sprain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.servlet.ModelAndView
import scala.collection.JavaConversions.mapAsJavaMap

@Controller
class Entrance {
    var nodes = Set[String]()
    var links = Set[(String, String, String)]()

    @RequestMapping(value = Array("/"))
    def home() = new ModelAndView("graph", Map("nodes" -> nodes, "links" -> links))

    @RequestMapping(
        value = Array("/nodes/new"),
        method = Array(POST))
    def add(nodeName : String) = {
        nodes = nodes + nodeName

        "redirect:/"
    }

    @RequestMapping(
        value = Array("/links/new"),
        method = Array(POST))
    def addLink(nodeFrom : String, link : String, nodeTo : String) = {
        nodes = nodes + nodeFrom + nodeTo
        links = links + ((nodeFrom, link, nodeTo))

        "redirect:/"
    }
}

