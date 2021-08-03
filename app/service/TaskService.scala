package service

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._
import controllers.request.CreateTaskRequest
import entity.Task
import repository.TaskRepository

import javax.inject.{Inject, Singleton}

@Singleton
class TaskService[F[_] : Monad] @Inject()(val taskRepository: TaskRepository[F]) {

  def create[X](request: CreateTaskRequest): F[Either[X, Unit]] = for {
    taskId <- taskRepository.nextTaskId()
    task = Task(taskId, request.taskName, request.description)
    _ <- taskRepository.create(task)
  } yield Right(())


  def allTasks(): F[List[Task]] = taskRepository.all()

}
