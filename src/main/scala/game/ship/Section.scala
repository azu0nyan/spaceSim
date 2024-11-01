package game.ship

import game.physics.MassData
import render.{DrawableSnapshot, DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.math.Scalar

class Section(
                   var modules: Seq[SectionModule] = Seq(),
                   val physicsProperties: PhysicsProperties = new PhysicsProperties()
                 ) {
  def tick(dt: Scalar, ship: Ship): Unit =
    modules.foreach(_.tick(dt, ship))

  def massData: MassData =
    MassData.combine(
      physicsProperties.massData,
      modules.map(md => md.massData.copy(centroid = md.massData.centroid + physicsProperties.position)): _ *
    )

  def drawables(params: DrawableSnapshotParams): Seq[ShapeWithDrawingParams] =
    Seq(ShapeWithDrawingParams(physicsProperties.shapeAtTransform, physicsProperties.materialProperties.color,zOrder = render.ZOrder.Ship.hull)) ++
      modules.flatMap(_.drawables(params)).map(sh => sh.atTransform(1.0, physicsProperties.rotation, physicsProperties.position))


}
