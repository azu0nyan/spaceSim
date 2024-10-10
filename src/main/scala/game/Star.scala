package game

import render.{DrawableSnapshot, ShapeWithDrawingParams}
import utils.Shapes
import utils.math.*
import utils.math.planar.V2


class Star(
            val position: V2 = V2.ZERO,
            val radius: Scalar = 10000,
            val mass: Scalar = 10000,
            var age: Scalar = 0
          ) extends Entity {

  override def tick(dt: Scalar): Unit = {
    age += dt
  }

  def shape = Shapes.CircleBasicShape(position, radius)

  override def drawableSnapshot: Option[DrawableSnapshot] = {
    Some(
      DrawableSnapshot(
        Seq(ShapeWithDrawingParams(shape))
      )
    )
  }
}
