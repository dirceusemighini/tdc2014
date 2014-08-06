package semighini.tdc.atores


import akka.actor.{ActorLogging, Actor}
import semighini.tdc.atores.UserMessages.{GetUser, SetUser, DelUser}

import semighini.tdc.modelo.Usuario
import com.redis._
     import java.util.UUID

/**
 * Created by dirceu on 8/5/14.
 */

object UserMessages{

  case class SetUser(user: Usuario)

  case class GetUser(id: UUID)

  case class DelUser(id: UUID)


}
class UserActor extends Actor with ActorLogging{

  val host = "localhost"
  val port = 6379

  val timeToLive = Some(3600)

  protected lazy val client = new RedisClient(host, port)

  def receive:Receive = {
    case SetUser(user) => sender ! set(user.sessionId.toString,user.id.toString(), timeToLive)

    case GetUser(id) => {
       sender ! client.get(id.toString)
    }

    case DelUser(id) => {
      log.info(s"[DEL] $id")
      client.del(id.toString)
    }
  }

  private def set(key: String, value: String, timeToLive: Option[Int]): String = {
    log.info(s"[SET] $key -> $value")
    client set(key, value)
    timeToLive.map(time => client.expire(key, time))
    value
  }
}
