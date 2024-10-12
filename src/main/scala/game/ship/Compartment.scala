package game.ship

import game.physics.MassData
import utils.math.Scalar

class Compartment(
                   var modules: Seq[CompartmentModule] = Seq(),
                   val physicsProperties: PhysicsProperties = new PhysicsProperties()
                 ) {
  def tick(dt: Scalar): Unit =
    modules.foreach(_.tick(dt))

  def massData: MassData =
    MassData.combine(
      physicsProperties.massData,
      modules.map(md => md.massData.copy(centroid = md.massData.centroid + physicsProperties.position)): _ *
    )

}
