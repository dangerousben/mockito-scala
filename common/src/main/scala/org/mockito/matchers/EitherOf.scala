package org.mockito
package matchers

/** Combine two matchers using Either
 */
case class EitherOf[A, B] private (ma: ArgumentMatcher[A], mb: ArgumentMatcher[B]) extends ArgumentMatcher[Either[A, B]] {
  override def matches(eab: Either[A, B]) =
    eab match {
      case Left(a)  => ma.matches(a)
      case Right(b) => mb.matches(b)
    }

  override def toString = s"eitherOf($ma, $mb)"
}

object EitherOf {
  def apply[A, B](ma: ArgumentMatcher[A], mb: ArgumentMatcher[B]): ArgumentMatcher[Either[A, B]] =
    new EitherOf(ma, mb)
}
