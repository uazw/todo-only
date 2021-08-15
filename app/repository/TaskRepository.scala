package repository

import cats.Applicative
import cats.syntax.applicative._
import entity.Task

import java.util.UUID

class TaskRepository[F[_] : Applicative]() {

  private[this] var tasks: Map[String, Task] = List(
    Task(UUID.randomUUID().toString, "wake up", "wake you up"),
    Task(UUID.randomUUID().toString, "stand up", "read a lot"),
    Task(UUID.randomUUID().toString, "shut up", "keep silent")
  ).map(task => task.taskId -> task).toMap

  def update(task: Task): F[Task] = {
    tasks = tasks + (task.taskId -> task)
    task.pure
  }

  def queryById(taskId: String): F[Option[Task]] = tasks.get(taskId).pure

  def nextTaskId(): F[String] = UUID.randomUUID().toString.pure

  def create(task: Task): F[Task] = {
    tasks += (task.taskId -> task)
    task.pure[F]
  }

  def all(): F[List[Task]] = tasks.values.toList.pure
}
