package semighini.tdc.atores

import java.util.UUID

import akka.actor.{Actor, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import semighini.tdc.atores.MensagensEmpresa.GetEmpresaPorCnpj
import semighini.tdc.repositorios.RepositorioEmpresaComponente
import semighini.tdc.repositorios.mock.MockRepositorioEmpresaComponente
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}


object MensagensEmpresa {

  case class GetEmpresaPorCnpj(cnpj: String)

}

class AtorEmpresa extends MockRepositorioEmpresaComponente with ComponenteAtorEmpresa

trait ComponenteAtorEmpresa extends Actor {
 this: RepositorioEmpresaComponente   =>

  private val timeToLive = Some(3600)

  implicit val timeout = Timeout(2 seconds)

  def receive = {
    case GetEmpresaPorCnpj(cnpj) => sender ! empresaRepositorio.buscaPorCNPJ(cnpj)
  }
}
