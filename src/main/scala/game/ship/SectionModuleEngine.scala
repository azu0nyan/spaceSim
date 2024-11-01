package game.ship

import game.ship.SectionModuleEngine.engineShapeVs
import render.{DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.Shapes.PolygonBasicShape
import utils.datastructures.spatial.AARectangle
import utils.math.*
import utils.math.planar.V2

import java.awt.Color

class SectionModuleEngine(
                               val thrust: Scalar = 1000.0,
                               var active: Scalar = 1.0,
                               override val physicsProperties: PhysicsProperties = new PhysicsProperties()
                             ) extends SectionModule {

  override def tick(dt: Scalar, ship: Ship): Unit = {

  }

  override def drawables(params: DrawableSnapshotParams): Seq[ShapeWithDrawingParams] =
    super.drawables(params) ++ enginShape(params).toSeq


  def enginShape(params: DrawableSnapshotParams): Option[ShapeWithDrawingParams] =
    Option.when(active > 0) {
      ShapeWithDrawingParams(
        PolygonBasicShape(engineShapeVs)
          .atTransform(
            thrust.cbrt,
            physicsProperties.rotation,
            physicsProperties.position
          ),
        color = new Color(255, 255, 0, math.min(255, (255 * active)).toInt),
        zOrder = render.ZOrder.Ship.engine,
      )
    }


}

object SectionModuleEngine {
  val engineShapeVs = Seq(V2(-1.5, -.5), V2(.0, -.1), V2(.0, .1), V2(-1.5, .5))

}
