import akka.actor.{ActorSystem, Props}
import actors._
import messages._

object Main extends App {
  val system = ActorSystem("insight")
  val process = system.actorOf(Props[ProcessingMasterActor], "process")
  val cleaner = system.actorOf(Props(new CleanupActor(process)), "cleaner")
  val fetcher = system.actorOf(Props(new DataFetchActor(cleaner)), "fetcher")

  fetcher ! BeginWork
  fetcher ! """{"limit":{"track":1,"timestamp_ms":"1459207521864"}}     """
  fetcher ! "msg1"
  fetcher ! "msg2"
  fetcher ! "msg3"


  Thread.sleep(5000)
  system.terminate
}