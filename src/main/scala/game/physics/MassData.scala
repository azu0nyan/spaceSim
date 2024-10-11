package game.physics

import utils.math.Scalar
import utils.math.planar.V2

case class MassData(var mass: Scalar, var centroid: V2, var inertia: Scalar)

object MassData {
  
  def combine(mds: Seq[MassData]): MassData = {
    var localCenter = V2.ZERO
    var mass = 0.0
    var m_I = 0.0
    for (md <- mds) {
      mass += md.mass
      localCenter += md.mass * md.centroid
      m_I += md.inertia      
    }
    
    localCenter /= mass
    m_I -= mass * (localCenter ** localCenter)
    
    MassData(mass, localCenter, m_I)
  }
}