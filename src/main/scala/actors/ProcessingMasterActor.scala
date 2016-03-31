package actors

import akka.actor.Actor

/**
  * Created by andrew on 3/31/16.
  */
class ProcessingMasterActor extends Actor {
  // actor uses child per entity pattern
  def receive = {
    // create child unless already exists by hashtag name
    // hashtags = ["spark", "akka", "hadoop"]

    case CountVertices => sender ! numberOfChildren; checkIfShouldTerminate
//    case


  }

  def numberOfChildren = context.children.toList.length

  def checkIfShouldTerminate = {
    // if has no more children (aka no more vertices) then this actor should shut itself down
    if (numberOfChildren == 0) context.stop(self)
  }

  def createOrUpdateHashtagActors(hashtags: List[String]) = {
    var actor = context.children
  }


}
