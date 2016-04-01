package actors

import akka.actor.{Actor, PoisonPill}
import com.github.nscala_time.time.Imports._

/**
  * Shared behavior across all actors.
  */
trait ActorBase extends Actor {
  // use UTC time for everything
  DateTimeZone.setDefault(DateTimeZone.UTC)

  def CountMyChildren = context.children.toList.length

  val timeFormatter = DateTimeFormat.forPattern("EEE MMM dd H:mm:ss Z yyyy")

  def parseUnixTime(timestamp: String) = timeFormatter.parseMillis(timestamp) / 1000

  def shutdown(message: String = "shutting self down.") = {
    log(message)
    self ! PoisonPill
  }

  /** Logging that identifies the actor sending message. */
  def log(s: String) = print(self.path.name + s" | $s\n\n")

}