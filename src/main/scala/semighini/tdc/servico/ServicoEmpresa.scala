package api.impl

import java.util.UUID

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import semighini.tdc.JsonFormatter
import semighini.tdc.modelo.Empresa
import spray.http.MediaTypes._
import spray.http._
import spray.json._
import spray.routing.Directives


import semighini.tdc.atores.MensagensEmpresa._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class ServicoEmpresa(company: ActorRef)(implicit executionContext: ExecutionContext) extends Directives
with DefaultJsonProtocol
with JsonFormatter
{

  /**
   * Defined Routes:
   *
   * <ul>
   * <li>GET    empresa/v1/busca/cnpj/<cpnj>/<JavaUUID>     </li>
   * </ul>
   */
  implicit val formatadorEmpresa= jsonFormat3(Empresa)

  implicit val timeout = Timeout(5 seconds)

  val rota = pathPrefix("empresa" / "v1") {
      path("busca" / "cnpj" / Segment ) {
        (cnpj) =>
          get {
            respondWithMediaType(`application/json`) {
              onComplete((company ? GetEmpresaPorCnpj(cnpj)).mapTo[Option[Empresa]]) {
                case Success(value) => complete(value.map(_.toJson.prettyPrint))
                case Failure(t) => failWith(t)
              }
            }
          }
      }
      }
}
