package controllers.extension

import cats.data.EitherT
import play.api.mvc.{Action, ActionBuilder, BodyParser, Result}

import scala.concurrent.{ExecutionContext, Future}

object http {

  type ActionResult[A] = EitherT[Future, Result, A]

  implicit class ActionBuilderOps[+R[_], B](ab: ActionBuilder[R, B]) {
    def asyncEitherT[A](bp: BodyParser[A])(cb: R[A] => EitherT[Future, Result, Result])(implicit ec: ExecutionContext): Action[A] =
      ab.async(bp) { c =>
        cb(c).value.map(x => x.fold(identity, identity))
      }
  }

}
