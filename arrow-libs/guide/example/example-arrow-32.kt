// This file was automatically generated from STM.kt by Knit tool. Do not edit.
package example.exampleArrow32

import arrow.fx.stm.TQueue
import arrow.fx.stm.atomically

suspend fun main() {
  //sampleStart
  val tq = TQueue.new<Int>()
  val result = atomically {
    tq.write(2)

    tq.peek()
  }
  //sampleEnd
  println("Result $result")
  println("Items in queue ${atomically { tq.flush() }}")
}
