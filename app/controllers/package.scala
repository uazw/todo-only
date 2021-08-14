import controllers.request.{CreateTaskRequest, UpdateTaskRequest}
import entity.Task
import play.api.libs.json.{Json, OWrites, Reads}

package object controllers {
  implicit val taskWrite: OWrites[Task] = Json.writes[Task]
  implicit val createTaskRequestRead: Reads[CreateTaskRequest] = Json.reads[CreateTaskRequest]
  implicit val updateTaskRequestRead: Reads[UpdateTaskRequest] = Json.reads[UpdateTaskRequest]
}
