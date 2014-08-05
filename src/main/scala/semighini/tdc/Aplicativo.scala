package semighini.tdc

/**
 * Created by dirceu on 8/4/14.
 */
object Aplicativo extends App{
                        implicit val system = ActorSystem()

  val handler = system.actorOf(Props[DemoServiceActor], name = "handler")
  IO(Http) ! Http.Vind(Handler, interface = "localhost", port=8080)
}
