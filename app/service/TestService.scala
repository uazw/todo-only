package service

import cats.implicits.catsSyntaxApplicativeId
import cats.{Applicative, FlatMap, Monad}


class TestService[F[_] : Monad] {

  def eeff()(implicit G: Monad[F]): Unit = {
    implicit val A = new Applicative[F] {
      override def pure[A](x: A): F[A] = ???

      override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] = ???
    }
    val value: F[String] = "sdfds".pure[F](A)

   implicit val B = new Monad[F] {
     override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = ???

     override def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B] = ???

     override def pure[A](x: A): F[A] = ???
   }

    B.flatMap(value)( x => B.pure(x))
  }

}
