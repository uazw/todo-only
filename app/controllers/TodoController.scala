package controllers

import cats.data.{EitherT, NonEmptyChain}
import controllers.request.CreateTaskRequest
import controllers.validator.{CreateTaskRequestValidator, DomainValidation}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import service.TaskService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoController @Inject()(val taskService: TaskService[Future])
                              (implicit cc: ControllerComponents, ec: ExecutionContext)
  extends AbstractController(cc) {

  def all() = Action.async(_ => taskService.allTasks().map(tasks => Ok(Json.toJson(tasks))))

  def createTask() = Action.async(parse.json) { request =>

    val taskRequest = request.body.as[CreateTaskRequest]

    for {
      createTaskRequest <- EitherT(Future(CreateTaskRequestValidator.validate(taskRequest).toEither))
      y <- EitherT(taskService.create[NonEmptyChain[DomainValidation]](createTaskRequest))
    } yield y
    null
  }
}
