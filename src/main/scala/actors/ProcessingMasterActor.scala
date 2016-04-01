package actors

import akka.actor.{ActorRef, Props}
import messages.{Tweet, UpdateHashtagGraph}

import scala.collection.mutable

/**
  * Builds the hashtag graph through child [[VertexActor]]s, and
  * keeps the stats on the graph.
  */
class ProcessingMasterActor(val cutoffWindowMs : Int = 60000) extends ActorBase {
  var maxTimestamp : Long = _
  var expireThreshold : Long = _
  var printer : ActorRef = _
  var processedCount = 0
  var receivedCount = 0
  var lastAvgDegree = 0.00
  var timeStampChanges = 0
  var calculationCount = 0
  var nodeTimestampMap = mutable.Map[ActorRef, Long]()

  override def preStart = printer = context.actorOf(Props[PrinterActor], "printer")

  def receive = {
    case t:Tweet => process(t)
    case _ => log("received something random")
  }

  /**
    * All tweets will trigger an expiration pass and printing of new average,
    * but only tweets that are within the window, and have hashtags, go into the graph.
 *
    * @param t [[Tweet]]
    */
  def process(t: Tweet) = {
    receivedCount +=1
    updateTimestamps(t)
    if (t.created_at > expireThreshold && !t.hashtags.isEmpty) updateGraph(t.hashtags)
    expireOldGraphNodes // order-sensitive, needs to be after graph update in case new tweet updates a node
    calculateAvgDegree
    processedCount +=1
  }

  def updateTimestamps(t: Tweet) = {
    // update maxTimestamp if need be
    if (t.created_at > maxTimestamp) maxTimestamp = t.created_at; timeStampChanges += 1

    // calculate the expiry cutoff
    expireThreshold = maxTimestamp - cutoffWindowMs

    // build/update map of actorRef -> timestamp, then
    // TODO: LEFT OFF HERE BUILDING MAP FOR EXPIRING GRAPH NODES
//    for (tag <- t.hashtags) nodeTimestampMap(tag, t.created_at)

    // 1. expire old actorrefs behind 60s window
    // 2. update timestamps when new tweets come in within the window and graphs are updated
  }

  def expireOldGraphNodes = {
    // TODO: add expiring logic
  }

  def calculateAvgDegree = {
    val vertexCount = CountMyChildren
    // how many edges (children) do each of my children have?
    // need to do this efficiently
    val avgDegree = 0.00
    lastAvgDegree = avgDegree
    calculationCount += 1

    printer ! avgDegree
  }


  def updateGraph(hashtags: List[String]) = {
    // evict nodes w/ timestamps older than 60s

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

  override def postStop(): Unit = {
    log(s"shutting down. Received $receivedCount. Processed $processedCount. maxTimeStamp changed $timeStampChanges times\nfinal value was $maxTimestamp. ")
  }
}

