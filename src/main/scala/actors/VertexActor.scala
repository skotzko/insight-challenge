package actors

import akka.actor.Actor.Receive
import akka.actor.Props
import messages._

/**
  * Actor representing a vertex in the graph.
  * Represents edges as child actors of same actor class.
  */
class VertexActor extends ActorBase {
  def receive = {
    case x: UpdateHashtagGraph => updateGraph(x.hashtags); checkIfShouldTerminate
    case x: String => log(s"processor got a message! $x")
    case _ => log("processor received something else")
  }

  /**
    * Creates missing graph edges as child actors of type [[VertexActor]]
    *
    * @param hashtags list of hashtags that are the edges for this vertex.
    */
  def updateGraph(hashtags: List[String]) = {
    // TODO: add expiration / removal of edges
    for (t <- hashtags) {
      val child = context.child(t)
      if (child.isEmpty) {
        context.actorOf(Props[VertexActor], t)
      }
    }

    log(s"${self.path.name} updated graph")
  }


/**
    * Called after all update operations. Actor self-terminates if it no
    * longer has any edges (no children).
    */
  def checkIfShouldTerminate = {
    // if has no more children (aka no more vertices) then this actor should shut itself down
    if (CountMyChildren == 0) log(s"${self.path.name} has no children, shutting down")
    //    if (CountMyChildren == 0) context.stop(self)
  }
}
