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
/** Update command for [[actors.VertexActor]]s */
class UpdateHashtagGraph(val hashtags: List[String])

/** Class extracted from JSON */
case class Tweet(created_at: Long, hashtags: List[String])