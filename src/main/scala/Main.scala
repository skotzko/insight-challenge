import akka.actor.{ActorSystem, Props}
import actors._
import messages._

object Main extends App {
  val batchSize = 1500

  val system = ActorSystem("insight")
  val process = system.actorOf(Props[ProcessingMasterActor], "process")
  val cleaner = system.actorOf(Props(new CleanupActor(process)), "cleaner")
  val fetcher = system.actorOf(Props(new DataFetchActor(cleaner, batchSize)), "fetcher")

  fetcher ! BeginWork
  Thread.sleep(3000)
  fetcher ! StopWork

  Thread.sleep(10000)
  system.terminate
}