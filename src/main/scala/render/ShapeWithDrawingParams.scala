package render

import drawing.library.DrawingUtils
import utils.math.planar.PolygonRegion
import utils.{Shape, ShapeAtTransform, Shapes}

import java.awt.{Color, Graphics2D}

case class ShapeWithDrawingParams(shape: Shape, color: Color = Color.WHITE, fill: Boolean = true, lineWidth: Int = 1)

object ShapeWithDrawingParams{
  def draw(shapeWithDrawingParams: ShapeWithDrawingParams) (g: Graphics2D) =
    shapeWithDrawingParams.shape match
      case circle: Shape.Circle =>
        DrawingUtils.drawCircle(circle.center, circle.radius.toFloat, g, shapeWithDrawingParams.color, shapeWithDrawingParams.fill, shapeWithDrawingParams.lineWidth)
      case rectangle: Shape.Rectangle =>
        val polygon = PolygonRegion(rectangle.angles)
        DrawingUtils.drawPolygon(polygon, g, shapeWithDrawingParams.fill, shapeWithDrawingParams.color, shapeWithDrawingParams.lineWidth)
}