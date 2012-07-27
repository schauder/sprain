package de.schauderhaft.sprain.db

import javax.servlet.Filter
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterChain
import org.scalaquery.session.Database
import org.scalaquery.session.Database.threadLocalSession

import org.scalaquery.ql.extended.MySQLDriver.Implicit._
import javax.servlet.ServletConfig
import javax.servlet.FilterConfig
import javax.sql.DataSource
import de.schauderhaft.sprain.db.schema.Nodes
import de.schauderhaft.sprain.db.schema.Links

class SessionFilter(dataSource: DataSource) extends Filter {

  val db = Database.forDataSource(dataSource)

  override def doFilter(
    request: ServletRequest,
    response: ServletResponse,
    chain: FilterChain) {

    db withTransaction {
      chain.doFilter(request, response)
    }
  }

  override def init(config: FilterConfig) {
    try {
      // this is extremely hackish. 
      // Multiple Filters will try to create the database multiple times 
      // which will cause bad things tm to happen
      db withSession {
        val ddl = (Nodes.ddl ++ Links.ddl)
        ddl.create
        println(ddl.createStatements.mkString("\n"))
      }
    } catch {
      case ex: Exception => ex.printStackTrace
    }
  }

  override def destroy() {}
}
