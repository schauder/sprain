package de.schauderhaft.sprain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.servlet.ModelAndView
import scala.collection.JavaConversions.mapAsJavaMap
import de.schauderhaft.sprain.store.Store
import de.schauderhaft.sprain.db.PersistentStore
import de.schauderhaft.sprain.model.Link


@Controller
class GraphController(val store : Store) {

    def this() = this(new PersistentStore)

    @RequestMapping(value = Array("/"))
    def home() = new ModelAndView("graph",
        Map(
            "nodes" -> store.allNodes,
            "links" -> store.allLinks))

    @RequestMapping(
        value = Array("/nodes/new"),
        method = Array(POST))
    def add(nodeName : String) = {
        store.add(nodeName)

        "redirect:/"
    }

    @RequestMapping(
        value = Array("/links/new"),
        method = Array(POST))
    def addLink(nodeFrom : String, link : String, nodeTo : String) = {
        val fromId = store.add(nodeFrom)
        val toId = store.add(nodeTo)

        store.add(fromId, link, toId)

        "redirect:/"
    }

    def deleteNode(nodeId : String) = {
	for {
	    Link(linkId,f,_,t) <- store.allLinks
            if f.id == nodeId || t.id == nodeId
        } store.deleteLink(linkId)
             
        store.deleteNode(nodeId)
        "redirect:/"
    }

}

