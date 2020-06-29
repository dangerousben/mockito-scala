package org.mockito.cats
package matchers

import cats._
import cats.implicits._
import org.mockito.ArgumentMatcher

/** Combine two functors using any Bifoldable instance
 */
class BifoldableMatcher[F[_, _], A, B](ma: ArgumentMatcher[A], mb: ArgumentMatcher[B])(implicit F: Bifoldable[F]) extends ArgumentMatcher[F[A, B]] {
  override def matches(fab: F[A, B]) =
    fab.bifoldLeft(true)((r, a) => r && ma.matches(a), (r, b) => r && mb.matches(b))

  override def toString = s"bifunctor(($ma, $mb): $F)"
}
