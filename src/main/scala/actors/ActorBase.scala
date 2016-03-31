package actors

import akka.actor.Actor

/**
  * Provides shared behavior across all actors.
  */
trait ActorBase extends Actor {
  def CountMyChildren = context.children.toList.length
}