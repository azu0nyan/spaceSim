package render

import drawing.core.DrawingWindow
import game.Game
import game.entity.WorldEntity
import game.physics.EntityControlledByRigidBody
import game.ship.Ship
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.{Color, Font, Graphics2D}
import java.awt.event.KeyEvent

object GameControls {
  def init(d: DrawingWindow, game: Game, ship: WorldEntity[Ship] ): Unit = {
    
//    d.addKeyBinding(KeyEvent.VK_W, ship.velocity = V2(20, 0).rotate(ship.rotation), true)
//    d.addKeyBinding(KeyEvent.VK_W, ship.velocity = V2(0, 0), false)
//
//    d.addKeyBinding(KeyEvent.VK_S, ship.velocity = V2(-20, 0).rotate(ship.rotation), true)
//    d.addKeyBinding(KeyEvent.VK_S, ship.velocity = V2(0, 0), false)
//    
//    d.addKeyBinding(KeyEvent.VK_A, ship.angularSpeed = 1.0, true)
//    d.addKeyBinding(KeyEvent.VK_A, ship.angularSpeed = 0.0, false)
//    
//    d.addKeyBinding(KeyEvent.VK_D, ship.angularSpeed = -1.0, true)
//    d.addKeyBinding(KeyEvent.VK_D, ship.angularSpeed = 0.0, false)

    object Keys {
      var upPressed = false
      var downPressed = false
      var leftPressed = false
      var rightPressed = false

    }
    d.addKeyBinding(KeyEvent.VK_W, Keys.upPressed = true, true)
    d.addKeyBinding(KeyEvent.VK_W, Keys.upPressed = false, false)
    d.addKeyBinding(KeyEvent.VK_S, Keys.downPressed = true, true)
    d.addKeyBinding(KeyEvent.VK_S, Keys.downPressed = false, false)
    d.addKeyBinding(KeyEvent.VK_A, Keys.leftPressed = true, true)
    d.addKeyBinding(KeyEvent.VK_A, Keys.leftPressed = false, false)
    d.addKeyBinding(KeyEvent.VK_D, Keys.rightPressed = true, true)
    d.addKeyBinding(KeyEvent.VK_D, Keys.rightPressed = false, false)
    
    d.addKeyBinding(KeyEvent.VK_ESCAPE, System.exit(0))
    d.addKeyBinding(KeyEvent.VK_SPACE, d.camera.controlsEnabled = !d.camera.controlsEnabled)
    

    d.addDrawable(new drawing.core.SimpleDrawable(){
      override def drawAndUpdate(g: Graphics2D, dt: Scalar): Unit = {

//        var forceVector = .0
//        var lrVector = .0
//        if (Keys.upPressed) forceVector += 1
//        if (Keys.downPressed) forceVector -= 1
//        if (Keys.leftPressed) lrVector -= 1
//        if (Keys.rightPressed) lrVector += 1

        ship.inner.controlState.forward = 0
        ship.inner.controlState.lr = 0
        if (Keys.upPressed) ship.inner.controlState.forward = 1
        if (Keys.downPressed) ship.inner.controlState.forward = -1
        if (Keys.leftPressed) ship.inner.controlState.lr = -1
        if (Keys.rightPressed) ship.inner.controlState.lr = 1
//        forceVector *= 1500
//        lrVector *= 500
//
//        val shipE = ship.asInstanceOf[EntityControlledByRigidBody[Ship]]
//        val shipDir = V2.ox.rotate(shipE.rb.angle)
//        shipE.rb.applyForce(shipDir * forceVector , shipE.rb.position)
//        
//        shipE.rb.applyForce(shipDir.rotate90CW * lrVector, shipE.rb.position + shipDir * -1)
        
        
        
        
        if(!d.camera.controlsEnabled) {
          d.camera.lookAt(ship.worldPosition)
//          d.camera.setRotation(-ship.worldRotation)
        }
        
        
        val debugString = f"z: ${d.camera.getZoom}%.7f ${d.camera.getLookAt.toShortString } c: ${d.camera.controlsEnabled} e:${game.entities.size}" 
        g.setColor(Color.WHITE)
        g.setFont(new Font("", Font.BOLD, 20))
        g.drawString(debugString, 20, 50)
      }
    })
//    d.addKeyBinding(KeyEvent.VK_Q, d.camera.zoomIN)
  }
}
