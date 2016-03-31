package actors

import akka.actor.Props
import messages.{CountYourChildren, Tweet}

/**
  * Builds the hashtag graph through child [[VertexActor]]s, and
  * keeps the stats on the graph.
  */
class ProcessingMasterActor extends ActorBase {
  def receive = {
    case CountYourChildren => sender ! CountMyChildren
    case x:Tweet => processIntoGraph(x)
  }


  def processIntoGraph(tweet: Tweet): Unit = {
    // create child unless already exists by hashtag name
    // hashtags = ["spark", "akka", "hadoop"]
    createOrUpdateHashtagActors(tweet.hashtags)

//    case CountVertices => sender ! numberOfChildren; checkIfShouldTerminate
//    case


  }

  // TODO: shouldn't this go in the vertex actors instead?
  def checkIfShouldTerminate = {
    // if has no more children (aka no more vertices) then this actor should shut itself down
    if (CountMyChildren == 0) context.stop(self)
  }


  def createOrUpdateHashtagActors(hashtags: List[String]) = {
    for (t <- hashtags ) {
      // check if vertex actor exists for this exact hashtag
      val child = context.child(t)
      if (child.isEmpty) {
        // create the vertex actor by name
        context.actorOf(Props[VertexActor], t)
      } else {
        // update the graph for this vertex
        // TODO: tell the child the new hashtags for its graph
      }

    }

  }


}
