package actors

import akka.actor._
import messages._
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Cleans up JSON payload sent by [[DataFetchActor]] and extracts [[Tweet]].
  * Filters out rate limit messages from the Twitter API.
  * Sends [[Tweet]]s on for handling by [[ProcessingMasterActor]].
  *
  * @param processor Actor that processes the tweets into a hashtag graph
  */
class CleanupActor(val processor: ActorRef) extends ActorBase {
  var receivedCount = 0
  var rateLimitCount = 0
  var processedCount = 0

  implicit lazy val formats = DefaultFormats

  def receive = {
    case x: String if x.toString.startsWith("{\"limit\":") => rateLimitCount += 1; receivedCount += 1
    case x: String => cleanAndSend(x)
  }

  /**
    * Sends parsed [[Tweet]]s to the [[ProcessingMasterActor]].
    * @param json
    */
  def cleanAndSend(json: String) = {
//    log(s"cleaning: $json")
    receivedCount += 1
    processor ! parseTweet(json)
  }

  /**
    * Extracts a [[Tweet]] object from JSON.
    *
    * @param rawJson to parse
    * @return [[Tweet]]
    */
  def parseTweet(rawJson: String) = {
    val json = parse(rawJson)
    val created_at = parseUnixTime((json \ "created_at").extract[String])

    // TODO: find better way to extract single vs multiple hashtags, this is hacky
    var hashtags = List[String]()
    try hashtags = (json \\ "hashtags" \ "text").extract[List[String]]
    catch {case e: MappingException =>
      { hashtags = List((json \\ "hashtags" \ "text").extract[String]) }
    }

    processedCount += 1
//    (created_at, hashtags)
    new Tweet(created_at, hashtags)
  }


  override def postStop(): Unit = {
    log(s"shutting down. Received $receivedCount payloads. Processed $processedCount. $rateLimitCount rate limit messages.")
    super.postStop()
  }

}

