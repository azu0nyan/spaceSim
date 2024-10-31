package render

import utils.color.Color
import utils.math.planar.V2

import java.awt.Graphics2D

case class DrawableSnapshotParams(
                                   debug: Boolean,
                                   minPos: V2 = V2(Double.NegativeInfinity, Double.NegativeInfinity),
                                   maxPos: V2 = V2(Double.PositiveInfinity, Double.PositiveInfinity)
                                 )
case class DrawableSnapshot(
                             shapes: Seq[ShapeWithDrawingParams] = Seq(),
                             debugShapes: Seq[DebugShapes] = Seq(),
                             depth: Int = 0
                           )

object DrawableSnapshot {
  
  
  
  def draw(drawableSnapshot: DrawableSnapshot)(g: Graphics2D): Unit = {
    drawableSnapshot.shapes.foreach(ShapeWithDrawingParams.draw(_)(g))
    
    drawableSnapshot.debugShapes.foreach(DebugShapes.draw(_)(g))
  }
}
