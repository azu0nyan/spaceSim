package game.ship.maker.rw

import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class LSystemReaderTest extends AnyFunSuite {
  test("Read") {
    val lines = Source.fromResource("lsystems/example.lsystem").getLines().toSeq
    
    val lSystem = LSystemReader
      .readFromLines(lines)

    println(lSystem)
  }
}
