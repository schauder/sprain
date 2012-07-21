package de.schauderhaft.sprain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.servlet.ModelAndView
import scala.collection.JavaConversions.mapAsJavaMap
import de.schauderhaft.sprain.store.Store
import de.schauderhaft.sprain.db.PersistentStore
import de.schauderhaft.sprain.model.Link
import de.schauderhaft.sprain.model.Node
import org.springframework.web.bind.annotation.PathVariable


@Controller
class GraphController(val store : Store) {

    def this() = this(new PersistentStore)

    @RequestMapping(value = Array("/"), produces=Array("text/html; charset=UTF-8"))
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
    def addLink(fromId : String, link : String, toId : String) = {
	val ids = for {
 	        Node(id,_) <- store.allNodes
        } yield id
        
	if (ids.contains(fromId) && ids.contains(toId))
            store.add(fromId, link, toId)

        "redirect:/"
    }

    @RequestMapping(value = Array("nodes/{nodeId}", "nodes/{nodeId}/delete"))
    def deleteNode(@PathVariable nodeId : String) = {
	for {
	    Link(linkId,_,_,_) <- store.allForNode(nodeId)
        } store.deleteLink(linkId)
             
        store.deleteNode(nodeId)
        "redirect:/"
    }
    
    @RequestMapping(value = Array("links/{linkId}", "links/{linkId}/delete"))
    def deleteLink(@PathVariable linkId : String) = {
	     
        store.deleteLink(linkId)
        "redirect:/"
    }

}

