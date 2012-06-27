package de.schauderhaft.sprain.model

case class Link(
    id : String,
    from : Node,
    link : String,
    to : Node)