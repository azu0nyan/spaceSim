package game.ship

import game.ship.SectionModuleWeaponEndpoint.WeaponShapeVs
import render.{DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.Shapes.{CircleBasicShape, PolygonBasicShape}
import utils.datastructures.spatial.AARectangle
import utils.math.*
import utils.math.planar.V2

import java.awt.Color

class SectionModuleWeaponEndpoint(
                                   var rotationFromMainDirection: Scalar = 0.0,
                                   override val physicsProperties: PhysicsProperties = new PhysicsProperties()
                                 ) extends SectionModule {

  override def tick(dt: Scalar, ship: Ship): Unit = {
    rotationFromMainDirection += dt * 0.5
  }

  override def drawables(params: DrawableSnapshotParams): Seq[ShapeWithDrawingParams] =
    weaponShape(params).toSeq


  def weaponShape(params: DrawableSnapshotParams): Option[ShapeWithDrawingParams] =
    Some(
      ShapeWithDrawingParams(
        PolygonBasicShape(WeaponShapeVs)
          .atTransform(
            physicsProperties.shape.asInstanceOf[CircleBasicShape].radius,
            physicsProperties.rotation + rotationFromMainDirection,
            physicsProperties.position
          ),
        color = new Color(255, 10, 10),
        zOrder = render.ZOrder.Ship.weapon,
      )
    )


}

object SectionModuleWeaponEndpoint {
  val WeaponShapeVs = Seq(V2(-.5, 0), V2(.0, -.5), V2(.5, -.1), V2(1.5, -.1), V2(1.5, .1), V2(.5, .1), V2(0, .5))

}
