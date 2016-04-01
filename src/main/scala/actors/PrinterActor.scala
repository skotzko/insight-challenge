package actors

import java.io.{File, PrintWriter}

class PrinterActor(outputPath : String = "tweet_output/output.txt") extends ActorBase {
  val writer = new PrintWriter(new File(outputPath))

  def receive =  {
    case x:String => log(x); writer.write(x)
  }

  override def postStop = writer.close()
}
