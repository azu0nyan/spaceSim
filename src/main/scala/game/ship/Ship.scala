package game.ship

import game.Entity
import render.{DrawableSnapshot, ShapeWithDrawingParams}
import utils.math.Scalar
import utils.math.planar.V2

import java.awt.Color

class Ship(
            var velocity: V2 = V2.ZERO,
            var angularSpeed: Scalar = 0.0,
            var position: V2 = V2.ZERO,
            var rotation: Scalar = 0.0,
            var compartments: Seq[Compartment] = Seq()
          ) extends Entity {

  def tick(dt: Scalar): Unit = {
    position += velocity * dt
    rotation += angularSpeed * dt
    compartments.foreach(_.tick(dt))
//    println(s"${velocity.toShortString} ${position.toShortString}")
  }

  override def drawableSnapshot: Option[DrawableSnapshot] =
    val compartmentShapes =
      compartments
        .map(c => ShapeWithDrawingParams(
          c.physicsProperties.shapeAtTransform.atTransform(1.0, rotation, position),
          c.physicsProperties.materialProperties.color
        ))


    val modulesShapes =
      compartments.flatMap { cmp =>
        cmp.modules
          .map { m =>
            val shapeAtTransform = m.physicsProperties.shapeAtTransform
              .atTransform(1.0, cmp.physicsProperties.rotation, cmp.physicsProperties.position)
              .atTransform(1.0, rotation, position)
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
