// This file was automatically generated from STM.kt by Knit tool. Do not edit.
package example.exampleArrow17

import arrow.fx.stm.TMVar
import arrow.fx.stm.atomically

suspend fun main() {
  //sampleStart
  val tmvar = TMVar.empty<Int>()
  val result = atomically {
    tmvar.isEmpty()
  }
  //sampleEnd
  println("Result $result")
}
