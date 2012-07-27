package de.schauderhaft.sprain.db

import de.schauderhaft.sprain.store.Store
import java.util.UUID
import org.scalaquery.ql.TypeMapper.StringTypeMapper

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import org.scalaquery.session.Database.threadLocalSession
import de.schauderhaft.sprain.db.schema.Nodes
import de.schauderhaft.sprain.db.schema.Links
import de.schauderhaft.sprain.model.Node
import de.schauderhaft.sprain.model.Link
import org.scalaquery.ql.Join

class PersistentStore extends Store {

    def allNodes() = {
        val nodes = for (n <- Nodes) yield n
        nodes.
            list.
            map(Node.tupled(_)).
            toSet
    }

    def allLinks() = {
        val links = for {
            l <- Links
            f <- Nodes
            t <- Nodes
            if l.from === f.id
            if l.to === t.id
        } yield (l.id, f, l.link, t)
        println(links.selectStatement)
        links.list.
            map(l => Link.tupled(l._1, Node.tupled(l._2), l._3, Node.tupled(l._4)))
            .toSet
    }

    /** adds a node to the store */
    def add(node: String) = {
        (Nodes.where(_.name === node) map (_.id)).
            firstOption match {
                case Some(id) => id
                case None =>
                    val id = UUID.randomUUID.toString
                    Nodes.insert(id, node)
                    id
            }
    }

    /** adds a link between to nodes to the store */
    def add(from: String, link: String, to: String) = {
        val id = UUID.randomUUID.toString
        Links.insert(id, from, link, to)
        id
    }

    /** removes a node from the store, does nothing when the node is not present in the store*/
    def deleteNode(id: String) {
        Nodes.where(_.id === id).delete
    }

    /** removes a link from the store, does nothing when the node is not present in the store*/
    def deleteLink(id: String) {

        val linksToDelete = for {
            l <- Links
            if l.id === id
        } yield l

        linksToDelete.delete
    }

    def allForNode(id: String) = {
        val links = for {
            l <- Links
            f <- Nodes
            t <- Nodes
            if l.from === f.id
            if l.to === t.id
            if l.from === id || l.to === id
        } yield (l.id, f, l.link, t)
        links.list.
            map(l => Link.tupled(l._1, Node.tupled(l._2), l._3, Node.tupled(l._4)))
            .toSet
    }
}

