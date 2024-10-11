package game.physics

import org.scalatest.funsuite.AnyFunSuite
import utils.datastructures.spatial.AARectangle
import utils.math.planar.V2


class PolygonOpsTest extends AnyFunSuite {
  test("1"){
    val vertices = AARectangle(V2(0, 0), V2(15, 10)).vertices
    println(vertices)
    val result = PolygonOps.computeMass(vertices.toIndexedSeq, 1)

    //should be 150.0 (7.5,5.0) 16250.0
    println(result)
  }

  test("2") {
    val vertices = AARectangle(V2(-50, -12), V2(15, 10)).vertices
    println(vertices)
    val result = PolygonOps.computeMass(vertices.toIndexedSeq, 1)

    //should be 1430.0 (-17.5,-1.0) 1000523.4
    println(result)
  }

}


