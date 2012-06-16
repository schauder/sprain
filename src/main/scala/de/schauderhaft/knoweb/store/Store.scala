package de.schauderhaft.knoweb.store

import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql._
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.session._

class Store(val db : Database) {

    def allNodes() = {
        db withSession {
            val nodes = for (n <- Nodes) yield n.name
            nodes.list.toSet
        }
    }

    def allLinks() = Set()
    def add(node : String) {
        db withSession {
            Nodes.insert(node)
        }
    }
}

object Nodes extends Table[(String)]("node") {
    def name = column[String]("name")
    def * = name
}
