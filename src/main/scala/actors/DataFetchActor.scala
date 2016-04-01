package actors

import akka.actor.ActorRef
import messages._
import scala.io.Source

/**
  * Loads batch of tweets and sends them on to [[CleanupActor]] for processing.
  *
  * @param cleaner    The actor that will clean tweets
  * @param batchLimit `Int` of max tweets to process per batch.
  */
class DataFetchActor(val cleaner: ActorRef, val batchLimit: Int = 50, val inputFilePath: String = "tweet_input/tweets.txt") extends ActorBase {
  private var initialized = false
  var fetchCount = 0

  def receive = {
    case BeginWork => loadAndProcessTweets()
    case StopWork => shutdown()
    case CountYourChildren => CountMyChildren
    case x: String => cleaner ! x
  }

  /**
    * Loads tweets in batch and sends on for processing.
    *
    * @return Nothing.
    */
  def loadAndProcessTweets(): Unit = {
    if (!initialized) initialized = true;
    val source = Source.fromFile(inputFilePath, "UTF-8")
    for (tweet <- source.getLines.take(batchLimit)) {
      cleaner ! tweet
      fetchCount += 1
    }
  }

  override def postStop(): Unit = {
    log(s"shutting down. Sent $fetchCount tweets.")
    super.postStop()
  }

}
