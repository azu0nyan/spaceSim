package render

import drawing.library.DrawingUtils
import utils.math.Scalar
import utils.math.planar.{PolygonRegion, V2}
import utils.{Shape, ShapeAtTransform, Shapes}

import java.awt.{Color, Graphics2D}

case class ShapeWithDrawingParams(shape: Shape, color: Color = Color.WHITE, fill: Boolean = true, lineWidth: Int = 1) {

  def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeWithDrawingParams =
    this.copy(shape = shape.atTransform(scale, rotate, translate))
}

object ShapeWithDrawingParams {
  def draw(shapeWithDrawingParams: ShapeWithDrawingParams)(g: Graphics2D) =
    shapeWithDrawingParams.shape match
      case circle: Shape.Circle =>
        DrawingUtils.drawCircle(circle.center, circle.radius.toFloat, g, shapeWithDrawingParams.color, shapeWithDrawingParams.fill, shapeWithDrawingParams.lineWidth)
      case rectangle: Shape.Rectangle =>
        val polygon = PolygonRegion(rectangle.angles)
        DrawingUtils.drawPolygon(polygon, g, shapeWithDrawingParams.fill, shapeWithDrawingParams.color, shapeWithDrawingParams.lineWidth)
}