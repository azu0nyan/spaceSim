package game.physics

import game.entity.{Entity, WorldEntity}
import utils.math.Scalar
import utils.math.planar.V2

class EntityControlledByRigidBody[DATA <: Entity](
                                                   override val inner: DATA,
                                                   initialMassData: MassData,
                                                   val rb: RigidBody = new RigidBody()
                                                 ) extends WorldEntity[DATA] {
  updateMassData(initialMassData)
  
  /**inner body offset*/
  var centroid = V2.ZERO
  def updateMassData(md: MassData): Unit = {
    rb.mass = md.mass
    rb.m_I = md.inertia
    centroid = md.centroid
  }

  override def move(dt: Scalar): Option[V2] = {
    val (pos, rot) = rb.tickAndGetNewPosition(dt)
    rb.position = pos
    rb.angle = rot
    //todo apply new position later
    Some(pos)
  }
  
  def applyForceLocal(localForce: V2, localPoint: V2): Unit = {
    rb.applyForce(localForce.rotate(rb.angle), (localPoint - centroid).rotate(rb.angle) + rb.position)
  }

  override def localPosition: V2 = rb.position
  override def localRotation: Scalar = rb.angle
  override def setLocalPosition(position: V2): Unit = rb.position = position
  override def setLocalRotation(rotation: Scalar): Unit = rb.angle = rotation
}
