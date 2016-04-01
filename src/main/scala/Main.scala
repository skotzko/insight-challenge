import akka.actor.{ActorSystem, Props}
import actors._
import messages._

object Main extends App {
  val system = ActorSystem("insight")
  val process = system.actorOf(Props[ProcessingMasterActor], "process")
  val cleaner = system.actorOf(Props(new CleanupActor(process)), "cleaner")
  val fetcher = system.actorOf(Props(new DataFetchActor(cleaner)), "fetcher")

  fetcher ! BeginWork
  Thread.sleep(3000)
  fetcher ! StopWork


  Thread.sleep(3000)
  system.terminate
}