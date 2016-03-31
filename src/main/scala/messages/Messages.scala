/** Messages used throughout the application. */
package messages

// SYSTEM CONTROL MESSAGES
/** Begin processing tweets. Intended for the [[actors.DataFetchActor]] */
object BeginWork
/** Stop processing tweets. Intended for the [[actors.DataFetchActor]] */
object StopWork

// SHARED ACTOR MESSAGES
/** Received by all actors extending [[actors.ActorBase]].
  * @return Returns an `Int` that is # of child actors recipient has.
  */
object CountYourChildren

// CONTENT MESSAGES
/** Clean the JSON `payload` */
class CleanJson(val payload: String)

/** Class to be extracted from the [[CleanJson.payload]] */
case class Tweet(created_at : String, hashtags: List[String])
