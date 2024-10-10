package render

import drawing.core.DrawingWindow
import game.Game
import utils.math.Scalar

import java.awt.{Color, Font, Graphics2D}
import java.awt.event.KeyEvent

object GameControls {
  def init(d: DrawingWindow, game: Game ): Unit = {
    d.addKeyBinding(KeyEvent.VK_ESCAPE, System.exit(0))
    d.camera.controlsEnabled = true

    d.addDrawable(new drawing.core.SimpleDrawable(){
      override def drawAndUpdate(g: Graphics2D, dt: Scalar): Unit = {
        val debugString = f"z: ${d.camera.getZoom}%.7f ${d.camera.getLookAt.toShortString } ${game.entities.size}"
        g.setColor(Color.WHITE)
        g.setFont(new Font("", Font.BOLD, 20))
        g.drawString(debugString, 20, 50)
      }
    })
//    d.addKeyBinding(KeyEvent.VK_Q, d.camera.zoomIN)
  }
}
