package render

import drawing.core.DrawingWindow
import game.Game
import game.ship.Ship
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.{Color, Font, Graphics2D}
import java.awt.event.KeyEvent

object GameControls {
  def init(d: DrawingWindow, game: Game, ship: Ship ): Unit = {
    
    d.addKeyBinding(KeyEvent.VK_W, ship.velocity = V2(20, 0).rotate(ship.rotation), true)
    d.addKeyBinding(KeyEvent.VK_W, ship.velocity = V2(0, 0), false)

    d.addKeyBinding(KeyEvent.VK_S, ship.velocity = V2(-20, 0).rotate(ship.rotation), true)
    d.addKeyBinding(KeyEvent.VK_S, ship.velocity = V2(0, 0), false)
    
    d.addKeyBinding(KeyEvent.VK_A, ship.angularSpeed = 1.0, true)
    d.addKeyBinding(KeyEvent.VK_A, ship.angularSpeed = 0.0, false)
    
    d.addKeyBinding(KeyEvent.VK_D, ship.angularSpeed = -1.0, true)
    d.addKeyBinding(KeyEvent.VK_D, ship.angularSpeed = 0.0, false)
    
    
    
    d.addKeyBinding(KeyEvent.VK_ESCAPE, System.exit(0))
    d.addKeyBinding(KeyEvent.VK_SPACE, d.camera.controlsEnabled = !d.camera.controlsEnabled)
    

    d.addDrawable(new drawing.core.SimpleDrawable(){
      override def drawAndUpdate(g: Graphics2D, dt: Scalar): Unit = {
        if(!d.camera.controlsEnabled) {
          d.camera.lookAt(ship.position)
          d.camera.setRotation(ship.rotation)
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
