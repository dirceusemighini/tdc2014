package semighini.tdc


import akka.actor.Actor

import scala.concurrent.duration._
import akka.actor._
import akka.pattern.ask
import spray.routing.{HttpService}
import spray.routing.directives.CachingDirectives
import spray.can.server.Stats
import spray.can.Http
import spray.httpx.marshalling.Marshaller
import spray.util._
import spray.http._
import CachingDirectives._
/**
 * Created by dirceu on 7/24/14.
 */


class DemoServiceActor extends Actor with DemoService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(demoRoute)
}

// this trait defines our service behavior independently from the service actor
trait DemoService extends HttpService {

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  implicit def executionContext = actorRefFactory.dispatcher

  val demoRoute = {
    get {
      // O que sera exibido na raiz
      pathSingleSlash {
        complete(index)
      } ~
      // Implementacao do caminho basico ping
        path("ping") {
          complete("PONG!")
        } ~
        path("stats") {
          complete {
            actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
              .ask(Http.GetStats)(1.second)
              .mapTo[Stats]
          }
        } ~
        path("cached") {
          cache(simpleRouteCache) { ctx =>
            in(1500.millis) {
              ctx.complete("This resource is only slow the first time!\n" +
                "It was produced on " + DateTime.now.toIsoDateTimeString + "\n\n" +
                "(Note that your browser will likely enforce a cache invalidation with a\n" +
                "`Cache-Control: max-age=0` header when you click 'reload', so you might need to `curl` this\n" +
                "resource in order to be able to see the cache effect!)")
            }
          }
        } ~
        path("fail") {
          failWith(new RuntimeException("aaaahhh"))
        }

    } ~
      (post | parameter('method ! "post")) {
        path("stop") {
          complete {
            in(1.second) {
              actorSystem.shutdown()
            }
            "Terminando em 1 segundo..."
          }
        }

      }~ post {

      pathPrefix("checkUser" / IntNumber) {
        uuid =>

        complete {
          s"Checkuser $abc"
        }
      }
    }
  }

  lazy val simpleRouteCache = routeCache()

  lazy val index =
    <html>
      <body>
        <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
        <p>Defined resources:</p>
        <ul>
          <li><a href="/ping">/ping</a></li>
          <li><a href="/stats">/stats</a></li>
          <li><a href="/cached">/cached</a></li>
          <li><a href="/stop?method=post">/stop</a></li>
        </ul>
      </body>
    </html>



  // simple case class whose instances we use as send confirmation message for streaming chunks
  case class Ok(remaining: Int)

  implicit val statsMarshaller: Marshaller[Stats] =
    Marshaller.delegate[Stats, String](ContentTypes.`text/plain`) { stats =>
      "Tempo de Funcionamento                : " + stats.uptime.formatHMS + '\n' +
        "Total de requisicoes               : " + stats.totalRequests + '\n' +
        "Requisicoes abertas                : " + stats.openRequests + '\n' +
        "Maximo de requisicoes abertas      : " + stats.maxOpenRequests + '\n' +
        "Total de conexoes                  : " + stats.totalConnections + '\n' +
        "Conexoes abertas                   : " + stats.openConnections + '\n' +
        "Numero maximo de conexoes abertas  : " + stats.maxOpenConnections + '\n' +
        "Requisicoes com timeout            : " + stats.requestTimeouts + '\n'
    }

  def in[U](duration: FiniteDuration)(body: => U): Unit =
    actorSystem.scheduler.scheduleOnce(duration)(body)
}