package semighini.tdc

import akka.actor.Props
import semighini.tdc.atores.{AtorEmpresa, AtorUsuario}

/**
 * Created by dirceu on 8/7/14.
 */
trait Atores {
      this : AppCore =>

  val atorEmpresa = system.actorOf(Props[AtorEmpresa])
  val atorUsuario = system.actorOf(Props[AtorUsuario])
}
