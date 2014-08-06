package semighini.tdc

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import semighini.tdc.servico.DemoServiceActor
import spray.can.Http

/**
 * Created by dirceu on 8/4/14.
 */
object Aplicativo extends App{
                        implicit val system = ActorSystem()

  val handler = system.actorOf(Props[DemoServiceActor], name = "handler")
  IO(Http) ! Http.Bind(handler, interface = "localhost", port=8080)
}
