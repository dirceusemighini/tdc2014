package semighini.tdc


import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

/**
 * Created by dirceu on 7/26/14.
 */
object MyApp extends App  {

  implicit val system = ActorSystem()

  // the handler actor replies to incoming HttpRequests
  val handler = system.actorOf(Props[DemoServiceActor], name = "handler")

  IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8080)
}