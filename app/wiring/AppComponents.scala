package wiring

import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repository.TaskRepository
import router.Routes
import service.TaskService


class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents {

  import cats.instances.future._

  lazy val repository = new TaskRepository()
  lazy val service = new TaskService(repository)

  lazy val applicationController = new controllers.TodoController(service)(controllerComponents, executionContext)

  lazy val router: Router = new Routes(httpErrorHandler, applicationController)
}
