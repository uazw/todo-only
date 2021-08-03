package controllers.request

import scala.language.implicitConversions

case class CreateTaskRequest(taskName: String, description: String)
