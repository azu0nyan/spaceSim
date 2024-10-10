package utils

import utils.Shapes.{CircleShapeAtTransform, RectangleShapeAtTransform}
import utils.datastructures.spatial.AARectangle
import utils.math.planar.{SegmentPlanar, V2}
import utils.math.*

import scala.collection.immutable.ArraySeq


sealed trait Shape

object Shape {
  trait Circle(val center: V2 = V2.ZERO, val radius: Scalar = 1.0) extends Shape {
    val aabb: AARectangle = AARectangle(center - radius, center + radius)
    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): CircleShapeAtTransform =
      CircleShapeAtTransform((center * scale).rotate(rotate) + translate, radius * scale)
  }


  trait Rectangle(val center: V2 = V2.ZERO, val halExtents: V2 = V2(.5, .5), val ox: V2 = V2.ox) extends Shape {
    val angles = ArraySeq(
      center + ox * halExtents.x - ox.rotate90CCW * halExtents.y,
      center + ox * halExtents.x + ox.rotate90CCW * halExtents.y,
      center - ox * halExtents.x + ox.rotate90CCW * halExtents.y,
      center - ox * halExtents.x - ox.rotate90CCW * halExtents.y,
    )

    val sides = (angles.last +: angles)
      .sliding(2)
      .map { case Seq(a, b) => SegmentPlanar(a, b) }
      .to(ArraySeq)

    def aabb: AARectangle = AARectangle.fromPoints(angles)
    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeAtTransform =
      RectangleShapeAtTransform((center * scale).rotate(rotate) + translate, halExtents * scale, ox.rotate(rotate))
  }
}

