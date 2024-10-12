package game.ship

import game.entity.{Entity, WorldEntity}
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
    
    
  def tick(dt: Scalar): Unit = {
    compartments.foreach(_.tick(dt))
  }

  override def drawableSnapshot: Option[DrawableSnapshot] =
    val compartmentShapes =
      compartments
        .map(c => ShapeWithDrawingParams(
          c.physicsProperties.shapeAtTransform.atTransform(1.0, parentEntity.worldRotation, parentEntity.worldPosition),
          c.physicsProperties.materialProperties.color
        ))


    val modulesShapes =
      compartments.flatMap { cmp =>
        cmp.modules
          .map { m =>
            val shapeAtTransform = m.physicsProperties.shapeAtTransform
              .atTransform(1.0, cmp.physicsProperties.rotation, cmp.physicsProperties.position)
              .atTransform(1.0, parentEntity.worldRotation, parentEntity.worldPosition)
            ShapeWithDrawingParams(shapeAtTransform,
              m.physicsProperties.materialProperties.color,
            )
          }
      }

    Some(DrawableSnapshot(
      compartmentShapes ++ modulesShapes
    ))
  end drawableSnapshot


}
