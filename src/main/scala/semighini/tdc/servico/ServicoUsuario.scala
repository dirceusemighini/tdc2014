package semighini.tdc.servico


import akka.actor.ActorRef
import akka.util.Timeout
import semighini.tdc.JsonFormatter
import semighini.tdc.atores.MensagensUsuario.{GetUser, DelUser, SetUser}
import semighini.tdc.modelo.Usuario

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.pattern.ask

import spray.routing.Directives

import spray.http._
import MediaTypes._

import scala.util.{Failure, Success}
import spray.json._
import DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._


/**
 * Created by dirceu on 8/5/14.
 */
class ServicoUsuario(atorUsuario:ActorRef)(implicit executionContext: ExecutionContext) extends Directives with DefaultJsonProtocol
with JsonFormatter
{

    // Request Timeout
    implicit val timeout = Timeout(10 seconds)
    // Message Json Formatters

  implicit val usuarioFormat = jsonFormat2(Usuario)
    implicit val postFormat = jsonFormat1(SetUser)
    implicit val getFormat = jsonFormat1(GetUser)
    implicit val deleteFormat = jsonFormat1(DelUser)
    /**
     * Defined Routes:
     *
     * <ul>
     * <li>POST   usuario/v1/sessao                    </li>
     * <li>GET    usuario/v1/sessao/<JavaUUID>         </li>
     * <li>DELETE usuario/v1/sessao/<JavaUUID>         </li>
     * </ul>
     */
    val rota =
      pathPrefix("usuario" / "v1") {
        path("sessao") {
          respondWithMediaType(`application/json`) {
            post {
              handleWith {
                p: SetUser => {
                  (atorUsuario ? p).mapTo[String].map(_.parseJson.prettyPrint)
                }
              }
            }
          }
        } ~
          path("sessao" / JavaUUID) {
            uuid =>
              get {
                respondWithMediaType(`application/json`) {
                  onComplete((atorUsuario? GetUser(uuid)).mapTo[Option[Usuario]]) {
                    case Success(value) => complete(value.map(_.toJson.prettyPrint))
                    case Failure(t) => failWith(t)
                  }
                }
              } ~
                delete {
                  complete {
                    atorUsuario ! DelUser(uuid)
                    StatusCodes.NoContent
                  }
                }

          }

        }

  }



