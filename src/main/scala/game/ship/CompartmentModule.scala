package game.ship

import render.{DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.math.Scalar

trait CompartmentModule {
  def physicsProperties: PhysicsProperties
  
  def tick(dt: Scalar, ship: Ship): Unit = {}

  def massData = physicsProperties.massData

  def drawables(params: DrawableSnapshotParams): Seq[ShapeWithDrawingParams] =
    Seq(ShapeWithDrawingParams(physicsProperties.shapeAtTransform, physicsProperties.materialProperties.color))
}
