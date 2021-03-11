---
layout: docs-core
title: Monoid
permalink: /arrow/typeclasses/monoid/
---

## Monoid

`Monoid` extends the `Semigroup` type class, adding an `empty` method to semigroup's `combine`. The empty method must return a value that, when combined with any other instance of that type, returns the other instance, i.e.,

```kotlin
(combine(x, empty) == combine(empty, x) == x)
```

For example, if we have a `Monoid<String>` with `combine` defined as string concatenation, then `empty = ""`.

Having an empty defined allows us to combine all the elements of some potentially empty collection of `T` for which a `Monoid<T>` is defined and return a `T`, rather than an `Option<T>` as we have a sensible default to fall back to.

And let's see the instance of Monoid<String> in action.

```kotlin:ank
import arrow.core.*
import arrow.typeclasses.*

Monoid.string().run { empty() }
```

```kotlin:ank
Monoid.string().run {
  listOf<String>("Λ", "R", "R", "O", "W").combineAll()
}
```

```kotlin:ank
import arrow.core.*

Monoid.option(Monoid.int()).run { listOf<Option<Int>>(Some(1), Some(1)).combineAll() }
```

The advantage of using these type class provided methods, rather than the specific ones for each type, is that we can compose monoids to allow us to operate on more complex types, for example.

This is also true if we define our own instances. As an example, let's use `Foldable`'s `foldMap`, which maps over values accumulating the results, using the available `Monoid` for the type mapped onto.

```kotlin:ank
import arrow.core.*

listOf(1, 2, 3, 4, 5).foldMap(Monoid.int(), ::identity)
```

```kotlin:ank
listOf(1, 2, 3, 4, 5).foldMap(Monoid.string(), { it.toString() })
```

To use this with a function that produces a tuple, we can define a Monoid for a tuple that will be valid for any tuple where the types it contains also have a Monoid available.

```kotlin:ank:silent
fun <A, B> monoidPair(MA: Monoid<A>, MB: Monoid<B>): Monoid<Pair<A, B>> =
  object: Monoid<Pair<A, B>> {

    override fun Pair<A, B>.combine(y: Pair<A, B>): Pair<A, B> {
      val (xa, xb) = this
      val (ya, yb) = y
      return Pair(MA.run { xa.combine(ya) }, MB.run { xb.combine(yb) })
    }

    override fun empty(): Pair<A, B> = Pair(MA.empty(), MB.empty())
  }
```

This way, we are able to combine both values in one pass, hurrah!

```kotlin:ank
import arrow.core.foldMap

val M = monoidPair(Monoid.int(), Monoid.string())
val list = listOf(1, 2, 3, 4)

list.foldMap(M) { n: Int ->
  Pair(n, n.toString())
}
```