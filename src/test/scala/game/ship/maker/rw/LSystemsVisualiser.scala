package game.ship.maker.rw

import drawing.Drawing
import game.physics.EntityControlledByRigidBody
import game.ship.maker.{DefaultLSystemInterpreter, ShipMaker}
import render.{DrawableSnapshot, DrawableSnapshotParams}
import utils.datastructures.IntV2
import utils.math.planar.V2

import java.awt.Font
import scala.io.Source

object LSystemsVisualiser {
  def main(args: Array[String]): Unit = {
    Drawing.startDrawingThread(
      size = IntV2(1920, 1080),
      decorated = true
    )
    Drawing.camera.invertY = true
    Drawing.camera.modZoom(3)

    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight + 1)
    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight - 1)
    Drawing.FpsCounter.font = new Font("", Font.BOLD, 30)
    Drawing.FpsCounter.dy = 100


    val lines = Source.fromResource("lsystems/example.lsystem").getLines().toSeq
    val lSystem = LSystemReader
      .readFromLines(lines)

    var spawnDelta = 150
    val count = 9
    val min = 0
    val rows = math.sqrt(count).floor
    val cols = count / rows
    
    for (i <- 0 until count) {
      val ship = ShipMaker
        .makeShip(lSystem = lSystem.right.get, lSystemInterpreter = DefaultLSystemInterpreter.string)(i + min)
        .right.get

      val x = i % cols.toInt - cols / 2
      val y = i / cols.toInt - rows / 2
      
      val e = EntityControlledByRigidBody(ship)
      e.setLocalPosition(x * spawnDelta, y * spawnDelta)
      

      Drawing.addDrawer(g => {
        DrawableSnapshot.draw(ship.drawableSnapshot(DrawableSnapshotParams(true)).get)(g)
      })
    }
  }
}
