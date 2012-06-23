package de.schauderhaft.sprain.store

trait Store {
    def allNodes() : Set[String]

    def allLinks() : Set[(String, String, String)]

    /** adds a node to the store */
    def add(node : String) : String

    /** adds a link between to nodes to the store */
    def add(from : String, link : String, to : String) : String

    /** removes a node from the store, does nothing when the node is not present in the store*/
    def deleteNode(id : String)

    /** removes a link from the store, does nothing when the node is not present in the store*/
    def deleteLink(id : String)
}

