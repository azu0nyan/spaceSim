package game.physics

import utils.{BasicShape, Shapes}
import utils.math.Scalar
import utils.math.planar.V2

case class MassData(var mass: Scalar, var centroid: V2, var inertia: Scalar)

object MassData {

  def combine(md: MassData, mds: MassData *): MassData = 
    combineSeq(md +: mds.toSeq)
    
  def combineSeq(mds: Seq[MassData]): MassData = {
    var localCenter = V2.ZERO
    var mass = 0.0
    var m_I = 0.0
    for (md <- mds) {
      mass += md.mass
      localCenter += md.mass * md.centroid
      m_I += md.inertia
    }

    localCenter /= mass
    m_I += mass * (localCenter ** localCenter)
    
//    m_I = Math.max(1.0, m_I) //todo why?

    MassData(mass, localCenter, m_I)
  }

  def compute(shape: BasicShape, position: V2, rotation: Scalar, mass: Scalar): MassData =
    shape match
      case Shapes.CircleBasicShape(center, radius) =>
        val I = CircleOps.rotationalInertia(mass, radius, position + center)
        MassData(mass, position + center, I)
      case s@Shapes.RectangleBasicShape(center, halfExtents, ox) =>
        val vs = s.angles.map(_ + position)
        PolygonOps.computeMassData(vs, 1).copy(mass = mass)
      case Shapes.CompoundShape(shapes) =>
        if (shapes.nonEmpty)
          val shapeMass = mass / shapes.size
          combineSeq(shapes.map(s => compute(s, position, rotation, shapeMass)))
        else MassData(0, V2.ZERO, 0)

}