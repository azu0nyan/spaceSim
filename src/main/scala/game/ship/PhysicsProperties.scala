package game.ship

import utils.BasicShape
import utils.ShapeAtTransform
import utils.Shapes.RectangleBasicShape
import utils.math.Scalar
import utils.math.planar.V2

class MaterialProperties(
                        var color: java.awt.Color = java.awt.Color.WHITE,
                        )

class PhysicsProperties(
                         var shape: BasicShape = RectangleBasicShape(),
                         var offset: V2 = V2.ZERO,
                         var rotation: Scalar = 0.0,
                         var mass: Scalar = 100.0,
                         var temperature: Scalar = 0.0,
                         var materialProperties: MaterialProperties = MaterialProperties()
                       ) {
  def shapeAtTransform: ShapeAtTransform =
    shape.atTransform(1.0, rotation, offset)
}