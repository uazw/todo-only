package repository

import cats.Applicative
import cats.syntax.applicative._
import entity.Task

import java.util.UUID

class TaskRepository[F[_] : Applicative]() {

  def create(task: Task): F[Task] = {
    tasks = task :: tasks
    task.pure[F]
  }

  def nextTaskId(): F[String] = UUID.randomUUID().toString.pure[F]

  private[this] var tasks = List(
    Task(UUID.randomUUID().toString, "wake up", "wake you up"),
    Task(UUID.randomUUID().toString, "stand up", "read a lot"),
    Task(UUID.randomUUID().toString, "shut up", "keep silent")
  )

  def all(): F[List[Task]] = tasks.pure[F]

}
