package render

import java.awt.Graphics2D


case class DrawableSnapshot(
                             shapes: Seq[ShapeWithDrawingParams],
                             //todo texts
                             //etc 
                             depth: Int = 0
                           )

object DrawableSnapshot {
  def draw(drawableSnapshot: DrawableSnapshot)(g: Graphics2D): Unit = {
    drawableSnapshot.shapes.foreach(ShapeWithDrawingParams.draw(_)(g))
  }
}
