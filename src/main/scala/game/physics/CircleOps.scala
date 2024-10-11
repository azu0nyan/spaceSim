package game.physics

import utils.math.Scalar
import utils.math.planar.V2

object CircleOps {
  def massFromDensity(density: Scalar, radius: Scalar): Scalar = 
    density * math.Pi * radius * radius
  
  def rotationalInertia(mass: Scalar, radius: Scalar, origin: V2 = V2.ZERO): Scalar = 
    mass * (radius * radius * .5 + origin  ** origin)
}
