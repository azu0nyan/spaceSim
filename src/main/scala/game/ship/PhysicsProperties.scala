package game.ship

import game.physics.{MassData, PolygonOps}
import utils.BasicShape
import utils.ShapeAtTransform
import utils.Shapes.{CircleBasicShape, CompoundShape, RectangleBasicShape}
import utils.math.Scalar
import utils.math.planar.{Polygon, V2}

class MaterialProperties(
                          var color: java.awt.Color = java.awt.Color.WHITE,
                        )

class PhysicsProperties(
                         var position: V2 = V2.ZERO,
                         var rotation: Scalar = 0.0,
                         var shape: BasicShape = RectangleBasicShape(),
                         var mass: Scalar = 100.0,
                         var temperature: Scalar = 0.0,
                         var materialProperties: MaterialProperties = MaterialProperties()
                       ) {
  def shapeAtTransform: ShapeAtTransform =
    shape.atTransform(1.0, rotation, position)


  def massData: MassData =
    MassData.compute(shape, position, rotation, mass)
}