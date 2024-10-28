package utils

import utils.Shapes.{CircleShapeAtTransform, PolygonShapeAtTransform, RectangleShapeAtTransform}
import utils.datastructures.spatial.AARectangle
import utils.math.planar.{PolygonRegion, SegmentPlanar, V2}
import utils.math.*

import scala.collection.immutable.ArraySeq


trait Shape {
  def atTransform(scale: Scalar, rotate: Scalar, translate: V2): ShapeAtTransform
  
  def area: Scalar
}

object Shape {
  trait Circle(val center: V2 = V2.ZERO, val radius: Scalar = 1.0) extends Shape {
    val aabb: AARectangle = AARectangle(center - radius, center + radius)
    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): CircleShapeAtTransform =
      CircleShapeAtTransform((center * scale).rotate(rotate) + translate, radius * scale)

    def area: Scalar = PI * radius * radius
  }


  trait Rectangle(val center: V2 = V2.ZERO, val halfExtents: V2 = V2(.5, .5), val ox: V2 = V2.ox) extends Shape {
    val angles = ArraySeq(
      center + ox * halfExtents.x - ox.rotate90CCW * halfExtents.y,
      center + ox * halfExtents.x + ox.rotate90CCW * halfExtents.y,
      center - ox * halfExtents.x + ox.rotate90CCW * halfExtents.y,
      center - ox * halfExtents.x - ox.rotate90CCW * halfExtents.y,
    )

    val sides = (angles.last +: angles)
      .sliding(2)
      .map { case Seq(a, b) => SegmentPlanar(a, b) }
      .to(ArraySeq)

    def aabb: AARectangle = AARectangle.fromPoints(angles)
    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): RectangleShapeAtTransform =
      RectangleShapeAtTransform((center * scale).rotate(rotate) + translate, halfExtents * scale, ox.rotate(rotate))
    
    def area: Scalar = 4 * halfExtents.x * halfExtents.y
  }
  
  trait Polygon(val vertices: Seq[V2]) extends Shape {
    val aabb: AARectangle = AARectangle.fromPoints(vertices)
    
    def atTransform(scale: Scalar, rotate: Scalar, translate: V2): PolygonShapeAtTransform =
      PolygonShapeAtTransform(vertices.map(v => (v * scale).rotate(rotate) + translate))
      
    val area: Scalar = PolygonRegion(vertices).area
  }
}

