package utils

import utils.Shape.{Circle, Rectangle}
import utils.datastructures.spatial.AARectangle
import utils.math.*
import utils.math.planar.V2

import scala.collection.immutable.ArraySeq


sealed trait BasicShape {
  def aabb: AARectangle
  def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeAtTransform
}

sealed trait ShapeAtTransform {
  def aabb: AARectangle
}

object Shapes {
  
  case class CircleBasicShape(override val center: V2 = V2.ZERO, override val radius: Scalar = 1.0) extends Circle(center, radius) with BasicShape

  case class CircleShapeAtTransform private[utils](override val center: V2, override val radius: Scalar) extends Circle(center, radius) with ShapeAtTransform

  case class RectangleBasicShape(override val center: V2 = V2.ZERO, override val halExtents: V2 = V2(.5, .5), override val ox: V2 = V2.ox) extends Rectangle(center, halExtents, ox) with BasicShape

  case class RectangleShapeAtTransform private[utils](override val center: V2, override val halExtents: V2, override val ox: V2) extends Rectangle(center, halExtents, ox) with ShapeAtTransform

  case class CompoundShape(shapes: Seq[BasicShape]) extends BasicShape {
    val aabb: AARectangle = shapes.map(_.aabb).reduceOption(_.combine(_)).getOrElse(AARectangle.ZERO_EMPTY)

    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): CompoundShapeAtTransform =
      CompoundShapeAtTransform(shapes.map(_.atTransform(scale, rotate, translate)))
  }

  case class CompoundShapeAtTransform(shapes: Seq[ShapeAtTransform]) extends ShapeAtTransform {
    val aabb: AARectangle = shapes.map(_.aabb).reduceOption(_.combine(_)).getOrElse(AARectangle.ZERO_EMPTY)
  }
  

}