package game.ship.maker

import drawing.Drawing
import game.physics.EntityControlledByRigidBody
import game.ship.Ship
import game.ship.maker.rw.LSystemReader
import game.ship.maker.{DefaultLSystemInterpreter, LSystem, LSystemInterpreter, ShipMaker}
import render.{DrawableSnapshot, DrawableSnapshotParams}
import utils.datastructures.IntV2
import utils.math.planar.V2

import java.awt.event.KeyEvent
import java.awt.{Color, Font}
import java.io.File
import scala.io.Source
import scala.jdk.CollectionConverters.CollectionHasAsScala

object LSystemsVisualiser {


  var lSystem: LSystem[Char] = LSystem[Char](Seq(), Map(), LSystemInterpreter.State())
  
  var currentSystem: String = "example.lsystem"
  

  var lsystemList: Seq[String] = Seq()

  private val dir = "lsystems"
  def sheduleReload() = {
    import java.io.IOException
    import java.nio.file._


    val directoryPath = Paths.get(dir)
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
              readLSystemFromFile()

            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE)
              reloadSystemList()

            if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
              reloadSystemList()
          }
        }

        key.reset()
      }

    }).start()
  }


  def readLSystemFromFile() = {
    import java.io.IOException
    import java.nio.file._

    val lines = Source.fromFile(dir + "/" +currentSystem).getLines().toSeq
    val lSystemLoaded = LSystemReader
      .readFromLines(lines)

    println(s"Loaded lsystem ${lSystemLoaded}")
    lSystem = lSystemLoaded.right.get
  }

  def reloadSystemList(): Unit = {
    lsystemList = new File(dir)
      .listFiles()
      .map(_.getName)
      .filter(_.endsWith(".lsystem"))
      .toList

    currentSystem = lsystemList
      .find(_ == currentSystem)
      .orElse(lsystemList.headOption)
      .getOrElse("lsystem folder empty")

    println(s"Reloaded system list ${lsystemList}")
  }

  def next() = {
    currentSystem = lsystemList
      .dropWhile(_ != currentSystem)
      .drop(1)
      .headOption
      .orElse(lsystemList.headOption)
      .getOrElse(currentSystem)

    println(s"Switching to ${currentSystem}")
    
    readLSystemFromFile()
  }

  def prev() = {
    currentSystem = lsystemList
      .reverse
      .dropWhile(_ != currentSystem)
      .drop(1)
      .headOption
      .orElse(lsystemList.lastOption)
      .getOrElse(currentSystem)
    println(s"Switching to ${currentSystem}")

    readLSystemFromFile()
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

    reloadSystemList()
    readLSystemFromFile()
    sheduleReload()

    var spawnDelta = 150
    val count = 6
    val min = 0
    val rows = math.sqrt(count).floor
    val cols = count / rows


    Drawing.addKeyBinding(KeyEvent.VK_LEFT, prev())

    Drawing.addKeyBinding(KeyEvent.VK_RIGHT, next())

    for (i <- 0 until count) {

      val x = i % cols.toInt - cols / 2
      val y = i / cols.toInt - rows / 2

      Drawing.addDrawer(g => {
        g.setColor(Color.WHITE)
        g.setFont(new Font("", Font.BOLD, 20))
        g.drawString(s"Current: $currentSystem", 20, 60)


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
