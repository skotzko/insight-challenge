package actors

import akka.actor.Actor

class VertexActor extends Actor {
  def receive = {
    case x:String => println(s"processor got a message! $x")
    case _ => println("processor received something else")
  }

}
