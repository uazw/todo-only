package controllers

import cats.data.EitherT
import controllers.extension.http._
import controllers.request.CreateTaskRequest
import controllers.validator.CreateTaskRequestValidator
import entity.Task
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import service.TaskService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoController @Inject()(val taskService: TaskService[Future])
                              (implicit cc: ControllerComponents, ec: ExecutionContext)
  extends AbstractController(cc) {

  def all() = Action.async(_ => taskService.allTasks().map(tasks => Ok(Json.toJson(tasks))))

  def createTask() = Action.asyncEitherT(parse.json) { request =>
    val taskRequest = request.body.as[CreateTaskRequest]

    for {
      createTaskRequest <- EitherT(Future(CreateTaskRequestValidator.validate(taskRequest)
        .leftMap(value => BadRequest(Json.toJson(value.map(_.errorMessage).toChain.toList))).toEither))
      _ <- EitherT[Future, Result, Task](taskService.create(createTaskRequest).map(Right(_)))
    } yield Created
  }
}
