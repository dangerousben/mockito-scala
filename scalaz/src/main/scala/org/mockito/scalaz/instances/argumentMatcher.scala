package org.mockito.scalaz

import scalaz._
import org.mockito.ArgumentMatcher
import org.mockito.matchers._

trait ArgumentMatcherInstances {
  implicit val argumentMatcherInstance: Decidable[ArgumentMatcher] with Plus[ArgumentMatcher] = new Decidable[ArgumentMatcher] with Plus[ArgumentMatcher] {
    override def conquer[A] = narrow(AnyArg)

    override def contramap[A, B](fa: ArgumentMatcher[A])(f: B => A) = Transformed(fa)(f)

    override def choose2[Z, A1, A2](a1: => ArgumentMatcher[A1], a2: => ArgumentMatcher[A2])(f: Z => A1 \/ A2) =
      contramap(EitherOf(a1, a2))(z => f(z).toEither)

    override def divide2[A1, A2, Z](a1: => ArgumentMatcher[A1], a2: => ArgumentMatcher[A2])(f: Z => (A1, A2)) =
      contramap(ProductOf(a1, a2))(f)

    override def plus[A](a: ArgumentMatcher[A], b: => ArgumentMatcher[A]): ArgumentMatcher[A] = AllOf(a, b)
  }
}

object ArgumentMatcherInstances extends ArgumentMatcherInstances
