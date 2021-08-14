package service

import cats.Monad
import cats.data.OptionT
import cats.syntax.flatMap._
import cats.syntax.functor._
import controllers.request.{CreateTaskRequest, UpdateTaskRequest}
import entity.Task
import repository.TaskRepository

class TaskService[F[_] : Monad](val taskRepository: TaskRepository[F]) {

  def update(taskId: String, command: UpdateTaskRequest): F[Option[Task]] = {
    (for {
      savedTaskOption <- OptionT(taskRepository.queryById(taskId))
      task = Task(savedTaskOption.taskId, command.taskName, command.description)
      _ <- OptionT(taskRepository.update(task).map(Some(_)))
    } yield task).value
  }

  def create(request: CreateTaskRequest): F[Task] = for {
    taskId <- taskRepository.nextTaskId()
    task = Task(taskId, request.taskName, request.description)
    savedTask <- taskRepository.create(task)
  } yield savedTask

  def allTasks(): F[List[Task]] = taskRepository.all()
}
