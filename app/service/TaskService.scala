package service

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._
import controllers.request.CreateTaskRequest
import entity.Task
import repository.TaskRepository

class TaskService[F[_] : Monad](val taskRepository: TaskRepository[F]) {

  def create(request: CreateTaskRequest): F[Task] = for {
    taskId <- taskRepository.nextTaskId()
    task = Task(taskId, request.taskName, request.description)
    savedTask <- taskRepository.create(task)
  } yield savedTask

  def allTasks(): F[List[Task]] = taskRepository.all()
}
