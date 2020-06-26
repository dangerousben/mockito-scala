package org.mockito
package matchers

package object syntax {
  implicit class ArgumentMatcherOps[A](private val matcher: ArgumentMatcher[A]) {
    def &&(other: ArgumentMatcher[A]): ArgumentMatcher[A]              = AllOf(matcher, other)
    def product[B](other: ArgumentMatcher[B]): ArgumentMatcher[(A, B)] = ProductOf(matcher, other)
    def transform[B](f: B => A): ArgumentMatcher[B]                    = Transformed(matcher)(f)
  }
}
