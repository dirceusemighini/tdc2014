package semighini.tdc.apps

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import api.impl.ServicoEmpresa
import semighini.tdc.{AppCoreConectado, Atores, RoteadorHttp}
import semighini.tdc.servico.ServicoUsuario
import spray.can.Http
import spray.routing.RouteConcatenation

/**
 * Created by dirceu on 8/4/14.
 */
object AppUsuario extends App with RouteConcatenation with Atores with AppCoreConectado{

  private implicit val _ = system.dispatcher

  private val rotas = new ServicoUsuario(atorUsuario).rota ~ new ServicoEmpresa(atorEmpresa).rota

  val handler = system.actorOf(Props(classOf[RoteadorHttp], rotas))
  IO(Http) ! Http.Bind(handler, interface = "localhost", port=8080)
}
