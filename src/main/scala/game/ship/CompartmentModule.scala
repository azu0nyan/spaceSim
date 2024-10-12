package game.ship

import render.ShapeWithDrawingParams
import utils.math.Scalar

trait CompartmentModule {
  def physicsProperties: PhysicsProperties
  
  def tick(dt: Scalar): Unit = {}

  def massData = physicsProperties.massData

  def drawables: Seq[ShapeWithDrawingParams] =
    Seq(ShapeWithDrawingParams(physicsProperties.shapeAtTransform, physicsProperties.materialProperties.color))
}
