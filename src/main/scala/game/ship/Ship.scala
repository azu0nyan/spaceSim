package game.ship

import game.entity.{Entity, WorldEntity}
import game.physics.{EntityControlledByRigidBody, MassData}
import game.ship.Ship.ShipControlState
import render.DebugShapes.DebugPointInWorldWithText
import render.{DrawableSnapshot, DrawableSnapshotParams, ShapeWithDrawingParams}
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.Color

class Ship(
            var sections: Seq[Section] = Seq()
          ) extends Entity {

  val controlState = new ShipControlState(0, 0)

  var parentEntity: EntityControlledByRigidBody[Ship] = _

  override def onAttach(entity: WorldEntity[_]): Unit =
    parentEntity = entity.asInstanceOf[EntityControlledByRigidBody[Ship]]
    parentEntity.updateMassData(massData)

  def tick(dt: Scalar): Unit =
    applyControlsToEngines(controlState)
    sections.foreach(_.tick(dt, this))

  override def drawableSnapshot(params: DrawableSnapshotParams): Option[DrawableSnapshot] = {
    val shipDrawables = sections
      .flatMap(_.drawables(params))
      .map(sh => sh.atTransform(1.0, parentEntity.worldRotation, parentEntity.worldPosition - parentEntity.centroid.rotate(parentEntity.worldRotation)))

    val debugShapes = if(params.debug)
      Seq(
        DebugPointInWorldWithText(parentEntity.worldPosition, "WP", color = Color.WHITE),
        DebugPointInWorldWithText(parentEntity.worldPosition - parentEntity.centroid.rotate(parentEntity.worldRotation), "С", color = Color.BLUE),
      )
      else Seq()

    Some(
      DrawableSnapshot(
        shapes = shipDrawables,
        debugShapes = debugShapes,
      )
    )
  }

  def massData: MassData =
    val md = MassData.combineSeq(sections.map(_.massData))

    md



  def applyControlsToEngines(state: ShipControlState) =
    engines.foreach{ e =>
      var dir = V2(state.forward, state.lr)
      val enginActivation = e.thrustDirection ** dir
      if(enginActivation > 0) {
        e.engine.active = enginActivation
        parentEntity.applyForceLocal(e.thrustDirection * e.engine.thrust * enginActivation, e.position)
      } else {
        e.engine.active = 0
      }
    }


  class EngineParams(
                      var position: V2 = V2.ZERO,
                      var rotation: Scalar = 0.0,
                      val engine: SectionModuleEngine,
                    ) {
    val thrustDirection = V2.ox.rotate(rotation)
  }

  def engines: Seq[EngineParams] =
    sections
      .flatMap(c => c.modules
        .collect { case m: SectionModuleEngine =>
          EngineParams(
            c.physicsProperties.position + m.physicsProperties.position.rotate(c.physicsProperties.rotation),
            c.physicsProperties.rotation + m.physicsProperties.rotation,
            m,
          )
        })

}

object Ship {
  class ShipControlState(
                          var forward: Scalar,
                          var lr: Scalar
                        )
}
