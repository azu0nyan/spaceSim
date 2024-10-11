package game.physics

import drawing.Drawing
import drawing.library.DrawingUtils
import game.ship.RigidBody
import utils.datastructures.IntV2
import utils.datastructures.spatial.AARectangle
import utils.math.planar.V2

import java.awt.{Color, Font}
import java.awt.event.KeyEvent

object PhysicsTestPlayground {

  def main(args: Array[String]): Unit = {


    Drawing.startDrawingThread(
      size = IntV2(1920, 1080),
      decorated = true
    )
    Drawing.camera.invertY = true
    Drawing.camera.modZoom(7)

    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight + 1)
    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight - 1)
    Drawing.FpsCounter.font = new Font("", Font.BOLD, 30)
    Drawing.FpsCounter.dy = 100


    val shape = AARectangle(V2(-1), V2(1))

    val md = PolygonOps.computeMass(shape.vertices.toIndexedSeq, 1)

    println(md)

    val rigidBody = new RigidBody(

    ) {
      m_I = md.inertia
    }

    object Keys {
      var upPressed = false
      var downPressed = false
      var leftPressed = false
      var rightPressed = false

    }
    Drawing.addKeyBinding(KeyEvent.VK_W, Keys.upPressed = true, true)
    Drawing.addKeyBinding(KeyEvent.VK_W, Keys.upPressed = false, false)
    Drawing.addKeyBinding(KeyEvent.VK_S, Keys.downPressed = true, true)
    Drawing.addKeyBinding(KeyEvent.VK_S, Keys.downPressed = false, false)
    Drawing.addKeyBinding(KeyEvent.VK_A, Keys.leftPressed = true, true)
    Drawing.addKeyBinding(KeyEvent.VK_A, Keys.leftPressed = false, false)
    Drawing.addKeyBinding(KeyEvent.VK_D, Keys.rightPressed = true, true)
    Drawing.addKeyBinding(KeyEvent.VK_D, Keys.rightPressed = false, false)


    Drawing.addDrawer(g => {

      val poly = shape.vertices

      DrawingUtils.drawPolygon(shape.toPolygon.rotate(rigidBody.angle).map(_ + rigidBody.position), g, true, Color.RED)

      var forceVector = V2.ZERO
      if (Keys.upPressed) forceVector += V2(0, 1)
      if (Keys.downPressed) forceVector -= V2(0, 1)
      if (Keys.leftPressed) forceVector -= V2(1, 0)
      if (Keys.rightPressed) forceVector += V2(1, 0)

      forceVector *= 20

      g.setFont(new Font("", Font.BOLD, 30))
      g.setColor(Color.WHITE)
      val debugStr = f"pos:${rigidBody.position.toShortString}%-20s rot:${rigidBody.angle}%-7.3f vel:${rigidBody.linearVelocity.toShortString}%-20s angVel:${rigidBody.angularVelocity}%-7.3f + force ${forceVector.toShortString}%-20s "
      g.drawString(debugStr, 10, 60)
      if (forceVector.nonZero) {
        println(forceVector)
        rigidBody.applyForce(forceVector, rigidBody.position + V2(0.0, 0.4))
      }
      rigidBody.tick(1 / 60.0)
    })

  }
}