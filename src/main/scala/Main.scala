package main

import akka.actor._
import actors._

object Main extends App {
  val system = ActorSystem("insight")
  val process = system.actorOf(Props[ProcessActor], "process")
  val stats = system.actorOf(Props[StatsActor], "stats")
  val cleanup = system.actorOf(Props(new CleanupActor(process)), "cleaning")

  cleanup ! """{"limit":{"track":1,"timestamp_ms":"1459207521864"}}     """
  cleanup ! "msg1"
  cleanup ! "msg2"
  cleanup ! "msg3"


  system.terminate
}