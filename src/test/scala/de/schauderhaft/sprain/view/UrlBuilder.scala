package de.schauderhaft.sprain.view

case class UrlBuilder(resource: String, id: String, verb: String) {
  def formUrl: String = resource + "/" + id + "/" + verb
  def restUrl: String = resource + "/" + id
}

object UrlBuilder {
  def delete = "delete"
}