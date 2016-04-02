import com.github.nscala_time.time.Imports._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.io.Source

object Main extends App {
  var processedCount = 0
  var rateLimitCount = 0

  // TODO: READ THESE FROM CONSOLE
  var outputFilePath = "tweet_output/output.txt"
  var inputFilePath = "tweet_input/input.txt"

  val Graph = new Graph()

  // TODO: get inputFilepath from args
  val source = Source.fromFile(inputFilePath, "UTF-8")
  for (tweet <- source.getLines) {
    match tweet {
      case x: String if x.toString.startsWith("{\"limit\":") => rateLimitCount += 1 // discard rate limit msgs
      case x: String => {
        val tweet = TweetParser.extract(x)

        // build the graph


        // expire nodes in graph
      }
    }
    }

}

object TweetParser {
  // use UTC time for everything
  DateTimeZone.setDefault(DateTimeZone.UTC)

  val timeFormatter = DateTimeFormat.forPattern("EEE MMM dd H:mm:ss Z yyyy")
  def parseUnixTime(timestamp: String) = timeFormatter.parseMillis(timestamp) / 1000

  /**
    * Parses JSON and extracts `created_at` and `hashtags` into [[Tweet]] object
    * @param rawJson
    * @return [[Tweet]] extracted
    */
  def extract(rawJson: String) = {
    val json = parse(rawJson)
    val created_at = parseUnixTime((json \ "created_at").extract[String])

    // TODO: find better way to extract single vs multiple hashtags, this is hacky
    var hashtags = List[String]()

    try hashtags = (json \\ "hashtags" \ "text").extract[List[String]]
    catch {case e: MappingException =>
      { hashtags = List((json \\ "hashtags" \ "text").extract[String]) }
    }

    new Tweet(created_at, hashtags)
  }
}

case class Tweet(created_at: Long, hashtags: List[String])

object Graph {
  def calculate(tweet: String) = {

  }
}

class Graph {
  var maxTimestamp = 0L
  // map vertex => edge count
  val edgeCountMap = Map[String, Int]

  // make graph of combinations?

}