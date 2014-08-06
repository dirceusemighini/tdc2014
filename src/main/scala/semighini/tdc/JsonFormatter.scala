package semighini.tdc

import java.util.UUID

import spray.json._
/**
 * Created by dirceu on 8/5/14.
 */
trait JsonFormatter {
  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString) //Never execute this line
    def read(value: JsValue) = value match {
        case JsString(x) => UUID.fromString(x)
        case x           => deserializationError("Expected UUID as JsString, but got " + x)
      }
  }
}
