package actors

import akka.actor._


trait ActorBase extends Actor {
  def CountMyChildren = context.children.toList.length
}