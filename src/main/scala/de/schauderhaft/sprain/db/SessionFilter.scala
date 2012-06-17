package de.schauderhaft.sprain.db

import javax.servlet.Filter
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterChain
import org.scalaquery.session.Database
import org.scalaquery.session.Database.threadLocalSession
import org.scalaquery.ql.basic.BasicDriver.Implicit._

import de.schauderhaft.sprain.store.Links;
import de.schauderhaft.sprain.store.Nodes;

import javax.servlet.ServletConfig
import javax.servlet.FilterConfig

class SessionFilter extends Filter {

    // this is extremely hackish. 
    // Multiple Filters will try to create the database multiple times 
    // which will cause bad things tm to happen
    val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    db withSession {
        val ddl = (Nodes.ddl ++ Links.ddl)
        ddl.create
        println(ddl.createStatements.mkString("\n"))
    }

    override def doFilter(
        request : ServletRequest,
        response : ServletResponse,
        chain : FilterChain) {

        db withTransaction {
            chain.doFilter(request, response)
        }
    }

    override def init(config : FilterConfig) {}

    override def destroy() {}
}
