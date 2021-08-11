package controllers

import cats.Applicative
import cats.data.{EitherT, NonEmptyList}
import controllers.extension.http._
import controllers.request.CreateTaskRequest
import controllers.validator.{CreateTaskRequestValidator, DomainValidation}
import entity.Task
import play.api.libs.json._
import play.api.mvc._
import service.TaskService

import scala.concurrent.{ExecutionContext, Future}

class TodoController(val taskService: TaskService[Future])
                    (implicit cc: ControllerComponents, ec: ExecutionContext) extends AbstractController(cc) {

  def all(): Action[AnyContent] = Action.async(_ => taskService.allTasks().map(tasks => Ok(Json.toJson(tasks))))

  def createTask(): Action[JsValue] = Action.asyncEitherT(parse.json) { request =>
    import cats.syntax.either._
    for {
      toCreated <- retrieveParam[Future, CreateTaskRequest](request.body.validate)
      a <- CreateTaskRequestValidator.validate(toCreated).liftTo[EitherT[Future, NonEmptyList[DomainValidation], *]]
        .leftMap(x => BadRequest(Json.toJson(x.toList.map(_.errorMessage))))
      _ <- EitherT[Future, Result, Task](taskService.create(a).map(Either.right(_)))
    } yield Created
  }

  private def retrieveParam[F[_] : Applicative, T](request: JsResult[T]): EitherT[F, Result, T] = {
    EitherT.fromEither(request.asEither).leftMap(e => BadRequest(JsError.toJson(e)))
  }
}
