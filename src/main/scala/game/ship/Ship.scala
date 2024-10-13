package game.ship

import game.entity.{Entity, WorldEntity}
import game.physics.{EntityControlledByRigidBody, MassData}
import game.ship.Ship.ShipControlState
import render.{DrawableSnapshot, DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.Color

class Ship(
            var compartments: Seq[Compartment] = Seq()
          ) extends Entity {

  val controlState = new ShipControlState(0, 0)
  
  var parentEntity: EntityControlledByRigidBody[Ship] = _

  override def onAttach(entity: WorldEntity[_]): Unit =
    parentEntity = entity.asInstanceOf[EntityControlledByRigidBody[Ship]]
    parentEntity.updateMassData(massData)

  def tick(dt: Scalar): Unit =
    compartments.foreach(_.tick(dt, this))

  override def drawableSnapshot(params: DrawableSnapshotParams): Option[DrawableSnapshot] =
    Some(
      DrawableSnapshot(
        compartments
          .flatMap(_.drawables(params))
          .map(sh => sh.atTransform(1.0, parentEntity.worldRotation, parentEntity.worldPosition))
      )
    )

  def massData: MassData =
    MassData.combineSeq(compartments.map(_.massData))


}

object Ship{
  class ShipControlState(
                        var forward: Scalar,
                        var lr: Scalar
                        )
}
