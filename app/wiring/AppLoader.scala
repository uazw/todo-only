package wiring

import play.api.{Application, ApplicationLoader}

class AppLoader extends ApplicationLoader {
  override def load(context: ApplicationLoader.Context): Application = {
    val appComponents = new AppComponents(context)
    appComponents.application
  }
}
