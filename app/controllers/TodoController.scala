package controllers

import entity.Task
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.TaskRepository

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class TodoController @Inject()(val taskRepository: TaskRepository)
                              (implicit cc: ControllerComponents, ec: ExecutionContext)
  extends AbstractController(cc) {


  def all() = Action.async { request =>
    implicit val residentWrites: OWrites[Task] = Json.writes[Task]
    taskRepository.all().map(tasks => Ok(Json.toJson(tasks)))
  }
}
