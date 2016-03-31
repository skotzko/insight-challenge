package actors

import akka.actor.Actor

/**
  * Created by andrew on 3/31/16.
  */
class ProcessActor extends Actor {
  def receive = {
    case x:String => println(s"processor got a message! $x")
    case _ => println("processor received something else")
  }

}
