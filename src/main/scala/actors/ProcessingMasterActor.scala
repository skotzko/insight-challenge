package actors

import akka.actor.Props
import com.github.nscala_time.time.Imports._
import messages.{CountYourChildren, Tweet, UpdateHashtagGraph}

/**
  * Builds the hashtag graph through child [[VertexActor]]s, and
  * keeps the stats on the graph.
  */

class ProcessingMasterActor extends ActorBase {
  private var max_timestamp : Long = _

  def receive = {
    case CountYourChildren => sender ! CountMyChildren
    case x:Tweet => processTweetIntoGraph(x)
  }

  def processTweetIntoGraph(tweet: Tweet): Unit = {
    log(s"processing tweet: ${tweet.hashtags} | ${tweet.created_at}\n")

    // update max_timestamp if need be
    if (tweet.created_at > max_timestamp) max_timestamp = tweet.created_at

    // TODO: if message is outside 60s window, dont process into graph but reprint avg
    
    // if no hashtags, still process it as this will evict some from graph

    // process as normal
    createOrUpdateHashtagActors(tweet.hashtags)
  }


  def createOrUpdateHashtagActors(hashtags: List[String]) = {
    // TODO: wonder if this would be faster not accessing context and just storing in own map?

    for (t <- hashtags ) {
      // check if vertex actor exists for hashtag, otherwise make it
      val child = context.child(t)
      if (child.isEmpty) {
        context.actorOf(Props[VertexActor], t)
      } else {
        child.get ! new UpdateHashtagGraph(hashtags.filter(_ != t))
      }
    }

  }


}
