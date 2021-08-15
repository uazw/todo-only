package controllers

import cats.ApplicativeError
import cats.data.EitherT
import cats.syntax.either._
import controllers.extension.http._
import controllers.request.{CreateTaskRequest, UpdateTaskRequest}
import controllers.validator.{CreateTaskRequestValidator, UpdateTaskRequestValidator}
import effect.effect._
import play.api.libs.json._
import play.api.mvc._
import service.TaskService

import scala.concurrent.{ExecutionContext, Future}

class TodoController(val taskService: TaskService[Effect])
                    (implicit cc: ControllerComponents, ec: ExecutionContext) extends AbstractController(cc) {

  def all(): Action[AnyContent] = Action.asyncEitherT {
    val value: EitherT[Future, Error, Result] = for {
      tasks <- taskService.allTasks()
    } yield Ok(Json.toJson(tasks))
    value
  }


  def deleteTask(taskId: String): Action[AnyContent] = Action.asyncEitherT {
    for {
      _ <- taskService.delete(taskId)
    } yield NoContent
  }

  def createTask(): Action[JsValue] = Action.asyncEitherT(parse.json) { request =>
    for {
      toCreated <- retrieveParam(request.body.validate[CreateTaskRequest])
      request <- CreateTaskRequestValidator.validate(toCreated).liftTo[Effect]
      _ <- taskService.create(request)
    } yield Created
  }

  def updateTask(taskId: String): Action[JsValue] = Action.asyncEitherT(parse.json) { request =>
    for {
      toCreated <- retrieveParam(request.body.validate[UpdateTaskRequest])
      command <- UpdateTaskRequestValidator.validate(toCreated).liftTo[Effect]
      createdTask <- taskService.update(taskId, command)
    } yield Ok(Json.toJson(createdTask))
  }

  private def retrieveParam[T](request: JsResult[T])(implicit applicative: ApplicativeError[Effect, Error]): Effect[T] = {
    request.asEither.fold(_ => applicative.raiseError(Error("sfdf")), applicative.pure)
  }
}
