package service

import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._
import controllers.request.{CreateTaskRequest, UpdateTaskRequest}
import effect.effect.Error
import entity.Task
import repository.TaskRepository

class TaskService[F[_]](val taskRepository: TaskRepository[F]) {

  def update(taskId: String, command: UpdateTaskRequest)(implicit ae: MonadError[F, Error]): F[Task] = for {
    savedTaskOption <- taskRepository.queryById(taskId)
    savedTask <- ae.fromOption(savedTaskOption, Error("task is not existed"))
    task = Task(savedTask.taskId, command.taskName, command.description)
    _ <- taskRepository.update(task)
  } yield task


  def create(request: CreateTaskRequest)(implicit ae: MonadError[F, Error]): F[Task] = for {
    taskId <- taskRepository.nextTaskId()
    task = Task(taskId, request.taskName, request.description)
    savedTask <- taskRepository.create(task)
  } yield savedTask

  def allTasks(): F[List[Task]] = taskRepository.all()
}
