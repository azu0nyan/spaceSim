package game.ship

import game.physics.MassData
import render.{DrawableSnapshot, ShapeWithDrawingParams}
import utils.math.Scalar

class Compartment(
                   var modules: Seq[CompartmentModule] = Seq(),
                   val physicsProperties: PhysicsProperties = new PhysicsProperties()
                 ) {
  def tick(dt: Scalar): Unit =
    modules.foreach(_.tick(dt))

  def massData: MassData =
    MassData.combine(
      physicsProperties.massData,
      modules.map(md => md.massData.copy(centroid = md.massData.centroid + physicsProperties.position)): _ *
    )

  def drawables: Seq[ShapeWithDrawingParams] =
    Seq(ShapeWithDrawingParams(physicsProperties.shapeAtTransform, physicsProperties.materialProperties.color)) ++
      modules.flatMap(_.drawables).map(sh => sh.atTransform(1.0, physicsProperties.rotation, physicsProperties.position))


}
