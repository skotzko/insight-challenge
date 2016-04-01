package actors

import akka.actor.{Actor, PoisonPill}
import com.github.nscala_time.time.Imports._

/**
  * Shared behavior across all actors.
  */
trait ActorBase extends Actor {
  def CountMyChildren = context.children.toList.length

  val timeFormatter = DateTimeFormat.forPattern("EEE MMM dd H:mm:ss Z yyyy")

  def parseUnixTime(timestamp: String) = timeFormatter.parseMillis(timestamp) / 1000

  override def preStart = {
    // use UTC time for everything
    DateTimeZone.setDefault(DateTimeZone.UTC)
  }

  def shutdown(message: String = "shutting self down.") = {
    log(message)
    self ! PoisonPill
  }


  /** Built-in logging to identify the actor sending message.
    * @example {{{
    *           // inside an actor
    *           log("done with work!")
    * }}}
    */
  def log(s: String) = print(self.path.name + s" | $s\n\n")

}