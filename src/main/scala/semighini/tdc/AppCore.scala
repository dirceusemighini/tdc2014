package semighini.tdc

import akka.actor.ActorSystem

/**
 * Created by dirceu on 8/7/14.
 */
trait AppCore {
       implicit def system: ActorSystem
}

trait AppCoreConectado extends AppCore {


  implicit lazy val system = ActorSystem("akka-spray")


  sys.addShutdownHook(system.shutdown)

}