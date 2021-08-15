package controllers.extension

import cats.data.{EitherT, NonEmptyList}
import play.api.libs.json.Json
import play.api.mvc.Results.BadRequest
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

object http {

  type ActionResult = EitherT[Future, NonEmptyList[String], Result]

  implicit class ActionBuilderOps[+R[_], B](ab: ActionBuilder[R, B]) {

    def asyncEitherT[A](bp: BodyParser[A])(cb: R[A] => ActionResult)(implicit ec: ExecutionContext): Action[A] =
      ab.async(bp) { c =>
        cb(c).value.map(_.fold(x => BadRequest(Json.toJson(x.toList)), identity))
      }

    def asyncEitherT(cb: => ActionResult)(implicit ec: ExecutionContext): Action[AnyContent] =
      ab.async {
        cb.value.map(_.fold(x => BadRequest(Json.toJson(x.toList)), identity))
      }
  }
}
