package render

import java.awt.Graphics2D
import utils.math._

class GameUpdater(tick: Scalar => Unit) extends drawing.core.DrawableUpdatable {
  /** вызывается в потоке рисования каждый кадр */
  def draw(g: Graphics2D): Unit = {}

  /** вызывается до рисования каждый кадр */
  def update(dt: Scalar): Unit = tick(dt)

}
