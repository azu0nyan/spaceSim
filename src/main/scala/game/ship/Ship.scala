package game.ship

import game.entity.{Entity, WorldEntity}
import game.physics.{EntityControlledByRigidBody, MassData}
import render.{DrawableSnapshot, ShapeWithDrawingParams}
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.Color

class Ship(
            var compartments: Seq[Compartment] = Seq()
          ) extends Entity {

  var parentEntity: WorldEntity[_] = _

  override def onAttach(entity: WorldEntity[_]): Unit =
    parentEntity = entity
    entity.asInstanceOf[EntityControlledByRigidBody[_]].updateMassData(massData)

  def tick(dt: Scalar): Unit = {
    compartments.foreach(_.tick(dt))
  }

  override def drawableSnapshot: Option[DrawableSnapshot] =
    Some(DrawableSnapshot(
      compartments
        .flatMap(_.drawables)
        .map(sh => sh.atTransform(1.0, parentEntity.worldRotation, parentEntity.worldPosition))
    )
    )

  def massData: MassData =
    MassData.combineSeq(compartments.map(_.massData))


}
