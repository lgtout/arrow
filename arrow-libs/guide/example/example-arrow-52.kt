// This file was automatically generated from STM.kt by Knit tool. Do not edit.
package example.exampleArrow52

import arrow.fx.stm.TSet
import arrow.fx.stm.atomically

suspend fun main() {
  //sampleStart
  val tset = TSet.new<String>()
  atomically {
    tset.insert("Hello")
  }
  //sampleEnd
}
