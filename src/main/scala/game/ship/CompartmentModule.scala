package game.ship

import utils.math.Scalar

trait CompartmentModule {
  def physicsProperties: PhysicsProperties
  
  def tick(dt: Scalar): Unit = {}
  
  def massData = physicsProperties.massData
}
