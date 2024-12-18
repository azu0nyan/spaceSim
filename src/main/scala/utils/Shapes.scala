package utils

import utils.Shape.{Circle, Rectangle, Polygon}
import utils.datastructures.spatial.AARectangle
import utils.math.*
import utils.math.planar.V2

import scala.collection.immutable.ArraySeq


sealed trait BasicShape extends Shape {
  def aabb: AARectangle
  def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeAtTransform
}

sealed trait ShapeAtTransform extends Shape {
  def aabb: AARectangle
  def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeAtTransform
}

object Shapes {

  case class CircleBasicShape(override val center: V2 = V2.ZERO, override val radius: Scalar = 1.0) extends Circle(center, radius) with BasicShape

  case class CircleShapeAtTransform private[utils](override val center: V2, override val radius: Scalar) extends Circle(center, radius) with ShapeAtTransform

  case class RectangleBasicShape(
                                  override val center: V2 = V2.ZERO,
                                  override val halfExtents: V2 = V2(.5, .5),
                                  override val ox: V2 = V2.ox) extends Rectangle(center, halfExtents, ox) with BasicShape

  case class RectangleShapeAtTransform private[utils](override val center: V2, override val halfExtents: V2, override val ox: V2) extends Rectangle(center, halfExtents, ox) with ShapeAtTransform

  case class CompoundShape(shapes: Seq[BasicShape]) extends BasicShape {
    val aabb: AARectangle = shapes.map(_.aabb).reduceOption(_.combine(_)).getOrElse(AARectangle.ZERO_EMPTY)

    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): CompoundShapeAtTransform =
      CompoundShapeAtTransform(shapes.map(_.atTransform(scale, rotate, translate)))

    def area: Scalar = shapes.map(_.area).reduceOption(_ + _).getOrElse(0.0)

    override def contains(p: V2): Boolean = shapes.exists(_.contains(p))
  }

  case class CompoundShapeAtTransform(shapes: Seq[ShapeAtTransform]) extends ShapeAtTransform {
    val aabb: AARectangle = shapes.map(_.aabb).reduceOption(_.combine(_)).getOrElse(AARectangle.ZERO_EMPTY)

    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): CompoundShapeAtTransform =
      CompoundShapeAtTransform(shapes.map(_.atTransform(scale, rotate, translate)))

    def area: Scalar = shapes.map(_.area).reduceOption(_ + _).getOrElse(0.0)
    
    override def contains(p: V2): Boolean = shapes.exists(_.contains(p))
  }                                                                      

  case class PolygonBasicShape(override val vertices: Seq[V2]) extends Polygon(vertices) with BasicShape

  case class PolygonShapeAtTransform(override val vertices: Seq[V2]) extends Polygon(vertices) with ShapeAtTransform

}