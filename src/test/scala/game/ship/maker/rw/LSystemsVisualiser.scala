package game.ship.maker.rw

import drawing.Drawing
import game.physics.EntityControlledByRigidBody
import game.ship.maker.{DefaultLSystemInterpreter, LSystem, ShipMaker}
import render.{DrawableSnapshot, DrawableSnapshotParams}
import utils.datastructures.IntV2
import utils.math.planar.V2

import java.awt.Font
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

object LSystemsVisualiser {


  var lSystem: LSystem[Char] = _
  def sheduleReload() = {
    import java.io.IOException
    import java.nio.file._


    val directoryPath = Paths.get("lsystems/")
    val watchService = FileSystems.getDefault.newWatchService

    directoryPath.register(watchService,
      StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_DELETE,
      StandardWatchEventKinds.ENTRY_MODIFY)


    new Thread(() => {
      while (true) {
        val key = watchService.take()
        val kind = key.pollEvents()
        if (kind.isEmpty) {
          Thread.sleep(50)
        } else {
          for (event <- kind.asScala) {
            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
              readLSystemFromFile("")
          }
        }

        key.reset()
      }

    }).start()
  }


  def readLSystemFromFile(path: String) = {
    import java.io.IOException
    import java.nio.file._


    //    var lines = Source.fromResource("lsystems/example.lsystem").getLines().toSeq
    var lines = Source.fromFile("lsystems/example.lsystem").getLines().toSeq
    var lSystemLoaded = LSystemReader
      .readFromLines(lines)

    println(s"Loaded lsystem ${lSystemLoaded}")
    lSystem = lSystemLoaded.right.get
  }


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


    readLSystemFromFile("")
    sheduleReload()

    var spawnDelta = 150
    val count = 6
    val min = 0
    val rows = math.sqrt(count).floor
    val cols = count / rows

    for (i <- 0 until count) {

      val x = i % cols.toInt - cols / 2
      val y = i / cols.toInt - rows / 2

      Drawing.addDrawer(g => {

        val ship = ShipMaker
          .makeShip(lSystem = lSystem, lSystemInterpreter = DefaultLSystemInterpreter.string)(i + min)
          .right.get

        val e = EntityControlledByRigidBody(ship)
        e.setLocalPosition(x * spawnDelta, y * spawnDelta)


        DrawableSnapshot.draw(ship.drawableSnapshot(DrawableSnapshotParams(true)).get)(g)
      })
    }
  }
}
