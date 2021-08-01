package repository

import entity.Task

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.Future

class TaskRepository @Inject()() {

  private[this] val tasks = List(
    Task(UUID.randomUUID().toString, "wake up", "wake you up"),
    Task(UUID.randomUUID().toString, "stand up", "read a lot"),
    Task(UUID.randomUUID().toString, "shut up", "keep silent")
  )

  def all(): Future[List[Task]] = Future.successful(tasks)

}
