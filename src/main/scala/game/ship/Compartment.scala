package game.ship

import utils.math.Scalar

class Compartment(
                   var modules: Seq[CompartmentModule] = Seq(),
                   val physicsProperties: PhysicsProperties = new PhysicsProperties()
                 ) {
  def tick(dt: Scalar): Unit = {
    modules.foreach(_.tick(dt))
  }
  
}
