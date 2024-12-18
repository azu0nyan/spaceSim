package game

import game.entity.Entity
import render.{DrawableSnapshot, DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.Shapes
import utils.math.*
import utils.math.planar.V2

import java.awt.Color


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

  override def drawableSnapshot(params: DrawableSnapshotParams): Option[DrawableSnapshot] = {
    Some(
      DrawableSnapshot(
        Seq(ShapeWithDrawingParams(shape, color = Color.WHITE, zOrder = render.ZOrder.star)),
      )
    )
  }
}
