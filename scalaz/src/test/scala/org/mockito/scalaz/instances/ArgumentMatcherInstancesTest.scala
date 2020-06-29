package org.mockito.scalaz
package instances

import org.mockito.{ ArgumentMatcher, ArgumentMatchersSugar, IdiomaticMockito }
import org.mockito.internal.matchers._
import org.mockito.matchers.Generators
import org.mockito.scalaz.ArgumentMatcherInstances._
import org.scalacheck.{ Arbitrary, Properties }
import org.scalatest.Matchers
import org.scalatest.funsuite.AnyFunSuiteLike
import scalaz._
import scalaz.Scalaz._
import scalaz.scalacheck.ScalazProperties._

class ArgumentMatcherInstancesTest extends Properties("ArgumentMatcher scalaz instances") with AnyFunSuiteLike with ArgumentMatchersSugar with IdiomaticMockito with Matchers {
  import Generators._

  protected implicit def equalArgumentMatcher[A: Arbitrary]: Equal[ArgumentMatcher[A]] =
    Equal.equal((x, y) => Stream.continually(Arbitrary.arbitrary[A].sample).flatten.take(3).forall(z => x.matches(z) == y.matches(z)))

  decidable.laws[ArgumentMatcher].properties.foreach { case (name, prop) => property(name) = prop }
  plus.laws[ArgumentMatcher].properties.foreach { case (name, prop) => property(name) = prop }

  test("contramapped ArgumentMatcher") {
    val aMock = mock[Foo]

    aMock.returnsOptionString(argThat(new StartsWith("prefix").contramap[String](_.toLowerCase))) returns Some("mocked!")

    aMock.returnsOptionString("PREFIX-foo") shouldBe Some("mocked!")
  }

  // test("tupled ArgumentMatchers") {
  //   val aMock = mock[Foo]

  //   aMock.takesTuple(argThat((new StartsWith("prefix"), new EqualsWithDelta(10, 2)).divide2)) returns "mocked!"

  //   aMock.takesTuple(("prefix-foo", 11)) shouldBe "mocked!"
  // }

  // test("tupled and contramapped ArgumentMatchers") {
  //   val aMock = mock[Foo]

  //   def split(s: String): (String, String) = s.split("/", 2) match { case Array(head, tail) => (head, tail) }
  //   aMock.returnsOptionString(
  //     argThat((new StartsWith("prefix1"), new StartsWith("prefix2")).contramapN[String](split))
  //   ) returns Some("mocked!")

  //   aMock.returnsOptionString("prefix1/prefix2/foo") shouldBe Some("mocked!")
  // }

  test("combined ArgumentMatchers") {
    val aMock = mock[Foo]

    aMock.returnsOptionString(argThat(new StartsWith("prefix") <+> new EndsWith("suffix"))) returns Some("mocked!")

    aMock.returnsOptionString("prefix-foo-suffix") shouldBe Some("mocked!")
  }

  // test("folded List of ArgumentMatchers") {
  //   val aMock = mock[Foo]

  //   val matcher = List(new StartsWith("prefix"), new Contains("middle"), new EndsWith("suffix")).foldK
  //   aMock.returnsOptionString(argThat(matcher)) returns Some("mocked!")

  //   aMock.returnsOptionString("prefix-middle-suffix") shouldBe Some("mocked!")
  // }
}
