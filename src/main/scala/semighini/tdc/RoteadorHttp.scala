package semighini.tdc

import akka.actor.{ActorLogging, Actor}
import spray.http.{HttpEntity, StatusCode}
import spray.http.StatusCodes._
import spray.routing._
import spray.util.LoggingContext

import scala.util.control.NonFatal

case class ExceptionDeResposta(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception

/**
 * Created by dirceu on 8/7/14.
 */
class RoteadorHttp(rota: Route) extends Actor with HttpService with ActorLogging {

  implicit def actorRefFactory = context

  implicit val handler = ExceptionHandler {
    case NonFatal(ExceptionDeResposta(statusCode, entity)) => ctx =>
      ctx.complete(statusCode, entity)

    case NonFatal(e) => ctx => {
      log.error(e, InternalServerError.defaultMessage)
      ctx.complete(InternalServerError)
    }
  }

  def receive: Receive =
    runRoute(rota)(handler, RejectionHandler.Default, context, RoutingSettings.default, LoggingContext.fromActorRefFactory)

}
