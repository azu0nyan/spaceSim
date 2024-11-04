package game.ship.maker

import drawing.Drawing
import game.physics.EntityControlledByRigidBody
import game.ship.Ship
import game.ship.maker.ShipBreeder.ShipBreederParams
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

object ShipBreedingVisualisation {

  var lSystemA: LSystem[Char] = LSystem[Char](Seq(), Map(), LSystemInterpreter.State())
  var lSystemB: LSystem[Char] = LSystem[Char](Seq(), Map(), LSystemInterpreter.State())

  var lSystemAName = "square_ship.lsystem"
  var lSystemBName = "triangle_ship.lsystem"

  var lSystemAGeneration = 5
  var lSystemBGeneration = 5

  var seed = 0

  val dir = "lsystems"


  var lsystemList: Seq[String] = Seq()

  def readSystemsFromFile(): Unit = {
    val linesA = Source.fromFile(dir + "/" + lSystemAName).getLines().toSeq
    val linesB = Source.fromFile(dir + "/" + lSystemBName).getLines().toSeq
    val lSystemALoaded = LSystemReader
      .readFromLines(linesA)
    val lSystemBLoaded = LSystemReader
      .readFromLines(linesB)

    println(s"Read systems ${lSystemA} and ${lSystemB}")

    lSystemA = lSystemALoaded.right.get
    lSystemB = lSystemBLoaded.right.get
  }

  def reloadSystemList(): Unit = {
    lsystemList = new File(dir)
      .listFiles()
      .map(_.getName)
      .filter(_.endsWith(".lsystem"))
      .toList

    lSystemAName = lsystemList
      .find(_ == lSystemAName)
      .orElse(lsystemList.headOption)
      .getOrElse("lsystem folder empty")

    lSystemBName = lsystemList
      .find(_ == lSystemBName)
      .orElse(lsystemList.headOption)
      .getOrElse("lsystem folder empty")

    println(s"Reloaded system list ${lsystemList}")
  }

  def incA(): Unit = {}

  def incB(): Unit = {}

  def decA(): Unit = {}

  def decB(): Unit = {}


  def nextA(): Unit = {

  }

  def nextB(): Unit = {

  }

  def prevA(): Unit = {

  }

  def prevB(): Unit = {

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
    readSystemsFromFile()


    Drawing.addDrawer(g => {
      g.setColor(Color.WHITE)
      g.setFont(new Font("", Font.BOLD, 20))
      g.drawString(s"CurrentA: $lSystemAName CurrentB: $lSystemBName", 20, 60)


      val lSystemOutputA =
        lSystemA
          .nth(lSystemAGeneration)

      val commandsA =
        DefaultLSystemInterpreter.string
          .interpret(lSystemA.initialState)(lSystemOutputA)


      val lSystemOutputB =
        lSystemB
          .nth(lSystemBGeneration)

      val commandsB =
        DefaultLSystemInterpreter.string
          .interpret(lSystemB.initialState)(lSystemOutputB)

      val commandsC =
        ShipBreeder
          .breed(
            ShipBreederParams(
              seed = 10,
              splits = 5,
            )
          )(commandsA, commandsB)

      val ship =
        CommandInterpreter
          .interpret(commandsC)
          .right
          .get


      val e = EntityControlledByRigidBody(ship)
//      e.setLocalPosition(x * spawnDelta, y * spawnDelta)
      
      DrawableSnapshot.draw(ship.drawableSnapshot(DrawableSnapshotParams(true)).get)(g)

    })
  }
}