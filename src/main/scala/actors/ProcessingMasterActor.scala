package actors

import akka.actor.{ActorRef, Props}
import messages.{Tweet, UpdateHashtagGraph}

import scala.collection.mutable

/**
  * Builds the hashtag graph through child [[VertexActor]]s, and
  * keeps the stats on the graph.
  */
class ProcessingMasterActor(val cutoffWindowSeconds : Int = 60) extends ActorBase {
  var maxTimestamp : Long = _
  var expireThreshold : Long = _
  var printer : ActorRef = _
  var processedCount = 0
  var receivedCount = 0
  var lastAvgDegree = 0.00
  var timeStampChanges = 0
  var calculationCount = 0
  var nodeTimestampMap = mutable.Map[String, Long]()

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
    expireOldGraphNodes // order-sensitive, needs to be after graph update in case new tweet keeps a key from expiring
    calculateAvgDegree
    processedCount +=1
  }


  // is this tweet older than max - 60s? then just re-print the avg
  // is this tweet within 60s, but has <2 hashtags? don't update graph, but expire old



  def updateTimestamps(t: Tweet) = {
    // update maxTimestamp & expire time cutoff if new max
    if (t.created_at > maxTimestamp)
      timeStampChanges += 1
      maxTimestamp = t.created_at
      expireThreshold = maxTimestamp - cutoffWindowSeconds

    // build/update map of actor name -> timestamp, then
    for (tag <- t.hashtags) nodeTimestampMap += (tag -> t.created_at)
  }

  def expireOldGraphNodes = {
    // TODO: add logic to expire all nodes who are still behind cutoff window
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

