package effect

import cats.data.{EitherT, NonEmptyList}

import scala.concurrent.Future

object effect {

  type Error = NonEmptyList[String]

  val Error: String => NonEmptyList[String] = NonEmptyList.one

  type Effect[A] = EitherT[Future, Error, A]
}
