package arrow.core

import arrow.typeclasses.Semigroup
import io.kotest.core.spec.style.StringSpec
import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.Arb
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.arrow.core.SemigroupLaws
import io.kotest.property.arrow.core.nonEmptyList
import io.kotest.property.arrow.laws.testLaws
import io.kotest.property.checkAll
import kotlin.math.max
import kotlin.math.min

class NonEmptyListTest : StringSpec() {
  init {

    testLaws(SemigroupLaws.laws(Semigroup.nonEmptyList(), Arb.nonEmptyList(Arb.int())))
    
    "iterable.toNonEmptyListOrNull should round trip" {
      checkAll(Arb.nonEmptyList(Arb.int())) { nonEmptyList ->
        nonEmptyList.all.toNonEmptyListOrNull().shouldNotBeNull() shouldBe nonEmptyList
      }
    }

    "iterable.toNonEmptyListOrNone should round trip" {
      checkAll(Arb.nonEmptyList(Arb.int())) { nonEmptyList ->
        nonEmptyList.all.toNonEmptyListOrNone() shouldBe nonEmptyList.some()
      }
    }
    
    // "traverse for Validated stack-safe" {
    //   // also verifies result order and execution order (l to r)
    //   val acc = mutableListOf<Int>()
    //   val res = (0..20_000).traverse(Semigroup.string()) {
    //     acc.add(it)
    //     Validated.Valid(it)
    //   }
    //   res shouldBe Validated.Valid(acc)
    //   res shouldBe Validated.Valid((0..20_000).toList())
    // }
    //
    // "traverse for Validated acummulates" {
    //   checkAll(Arb.nonEmptyList(Arb.int())) { ints ->
    //     val res: ValidatedNel<Int, NonEmptyList<Int>> =
    //       ints.traverse(Semigroup.nonEmptyList()) { i: Int -> if (i % 2 == 0) i.validNel() else i.invalidNel() }
    //
    //     val expected: ValidatedNel<Int, NonEmptyList<Int>> =
    //       ints.filterNot { it % 2 == 0 }.toNonEmptyListOrNull()?.invalid() ?: ints.filter { it % 2 == 0 }.toNonEmptyListOrNull()!!.valid()
    //
    //     res shouldBe expected
    //   }
    // }
    //
    // "sequence for Validated should be consistent with traverseValidated" {
    //   checkAll(Arb.nonEmptyList(Arb.int())) { ints ->
    //     ints.map { if (it % 2 == 0) Valid(it) else Invalid(it) }.sequence(Semigroup.int()) shouldBe
    //       ints.traverse(Semigroup.int()) { if (it % 2 == 0) Valid(it) else Invalid(it) }
    //   }
    // }

    "can align lists with different lengths" {
      checkAll(Arb.nonEmptyList(Arb.boolean()), Arb.nonEmptyList(Arb.boolean())) { a, b ->
        a.align(b).size shouldBe max(a.size, b.size)
      }

      checkAll(Arb.nonEmptyList(Arb.boolean()), Arb.nonEmptyList(Arb.boolean())) { a, b ->
        a.align(b).all.take(min(a.size, b.size)).forEach {
          it.isBoth shouldBe true
        }
      }
    }

    "zip2" {
      checkAll(Arb.nonEmptyList(Arb.int()), Arb.nonEmptyList(Arb.int())) { a, b ->
        val result = a.zip(b)
        val expected = a.all.zip(b.all).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip3" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c ->
        val result = a.zip(b, c, ::Triple)
        val expected = a.all.zip(b.all, c.all, ::Triple).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip4" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d ->
        val result = a.zip(b, c, d, ::Tuple4)
        val expected = a.all.zip(b.all, c.all, d.all, ::Tuple4).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip5" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d, e ->
        val result = a.zip(b, c, d, e, ::Tuple5)
        val expected = a.all.zip(b.all, c.all, d.all, e.all, ::Tuple5).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip6" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d, e, f ->
        val result = a.zip(b, c, d, e, f, ::Tuple6)
        val expected =
          a.all.zip(b.all, c.all, d.all, e.all, f.all, ::Tuple6).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip7" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d, e, f, g ->
        val result = a.zip(b, c, d, e, f, g, ::Tuple7)
        val expected =
          a.all.zip(b.all, c.all, d.all, e.all, f.all, g.all, ::Tuple7).toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip8" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d, e, f, g, h ->
        val result = a.zip(b, c, d, e, f, g, h, ::Tuple8)
        val expected = a.all.zip(b.all, c.all, d.all, e.all, f.all, g.all, h.all, ::Tuple8)
          .toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "zip9" {
      checkAll(
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int()),
        Arb.nonEmptyList(Arb.int())
      ) { a, b, c, d, e, f, g, h, i ->
        val result = a.zip(b, c, d, e, f, g, h, i, ::Tuple9)
        val expected = a.all.zip(b.all, c.all, d.all, e.all, f.all, g.all, h.all, i.all, ::Tuple9)
          .toNonEmptyListOrNull()
        result shouldBe expected
      }
    }

    "max element" {
      checkAll(
        Arb.nonEmptyList(Arb.int())
      ) { a ->
        val result = a.max()
        val expected = a.maxOrNull()
        result shouldBe expected
      }
    }

    "maxBy element" {
      checkAll(
        Arb.nonEmptyList(Arb.int())
      ) { a ->
        val result = a.maxBy(::identity)
        val expected = a.maxByOrNull(::identity)
        result shouldBe expected
      }
    }

    "min element" {
      checkAll(
        Arb.nonEmptyList(Arb.int())
      ) { a ->
        val result = a.min()
        val expected = a.minOrNull()
        result shouldBe expected
      }
    }

    "minBy element" {
      checkAll(
        Arb.nonEmptyList(Arb.int())
      ) { a ->
        val result = a.minBy(::identity)
        val expected = a.minByOrNull(::identity)
        result shouldBe expected
      }
    }

    "NonEmptyList equals List" {
      checkAll(
        Arb.nonEmptyList(Arb.int())
      ) { a ->
        withClue("$a should be equal to ${a.all}") {
          // `shouldBe` doesn't use the `equals` methods on `Iterable`
          (a == a.all).shouldBeTrue()
        }
      }
    }
  }
}
