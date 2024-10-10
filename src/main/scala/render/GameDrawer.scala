package render

import utils.math.Scalar

import java.awt.Graphics2D


class GameDrawer(
                  getSnapshots: () => Seq[DrawableSnapshot]
                ) extends drawing.core.SimpleDrawable() {
  override def drawAndUpdate(g: Graphics2D, dt: Scalar): Unit = {
    getSnapshots().foreach(DrawableSnapshot.draw(_)(g))
  }
}
