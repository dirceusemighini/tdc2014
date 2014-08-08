package semighini.tdc.apps

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import semighini.tdc.servico.DemoServiceActor
import spray.can.Http

/**
 * Created by dirceu on 8/7/14.
 */
object AppTeste extends App {

    implicit val system = ActorSystem()

    val handler = system.actorOf(Props[DemoServiceActor], name = "handler")

    IO(Http) ! Http.Bind(handler, interface = "localhost", port = 8080)

}
