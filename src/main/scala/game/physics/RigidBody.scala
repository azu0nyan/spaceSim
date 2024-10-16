package game.physics

import game.entity.Entity
import utils.math._
import utils.math.planar.V2

class RigidBody(
                 var position: V2 = V2.ZERO,
                 var angle: Scalar = 0.0,
                 var linearVelocity: V2 = V2.ZERO,
                 var angularVelocity: Scalar = 0.0,
                 var mass: Scalar = 1.0,

                 /** Rotational inertia about the center of mass. */
                 var m_I: Scalar = 0.0
               ) {

  var force = V2.ZERO
  var torque = 0.0

  def m_I_inv = 1.0 / m_I

  def invMass = 1.0 / mass

  /**
   * Apply a force at a world point. If the force is not applied at the center of mass, it will
   * generate a torque and affect the angular velocity.
   *
   * @param force the world force vector
   * @param point the world position of the point of application.
   */
  def applyForce(force: V2, point: V2): Unit = {
    this.force += force

    val torqueDelta = (point.x - position.x) * force.y - (point.y - position.y) * force.x
    torque += torqueDelta
  }
  /** Apply a force to the center of mass. */
  def applyForceToCenter(force: V2): Unit = {
    this.force += force
  }

  /**
   * Apply a torque. This affects the angular velocity without affecting the linear velocity of the
   * center of mass.
   *
   * @param torque about the z-axis (out of the screen), usually in N-m.
   */
  def applyTorque(torque: Scalar): Unit = {
    this.torque += torque
  }

  /**
   * Apply an impulse at a point. This immediately modifies the velocity. It also modifies the
   * angular velocity if the point of application is not at the center of mass.
   *
   * @param impulse the world impulse vector, usually in N-seconds or kg-m/s.
   * @param point   the world position of the point of application.
   */

  def applyLinearImpulse(impulse: V2, point: V2): Unit = {
    linearVelocity += impulse * invMass

    angularVelocity += m_I_inv * ((point.x - position.x) * impulse.y - (point.y - position.y) * impulse.x)
  }

  /**
   * Get the world linear velocity of a world point attached to this body.
   *
   * @param a point in world coordinates.
   * @return the world velocity of a point.
   */
  def getLinearVelocityFromWorldPoint(worldPoint: V2): Unit = {
    val tempX = worldPoint.x - position.x
    val tempY = worldPoint.y - position.y
    V2(-angularVelocity * tempY + linearVelocity.x,
      angularVelocity * tempX + linearVelocity.y)
  }


  def tickAndGetNewPosition(dt: Scalar): (V2, Scalar) = {
    linearVelocity += force * invMass * dt
    angularVelocity += m_I_inv * torque * dt

    val newPosition = position + linearVelocity * dt
    val newAngle = angle + angularVelocity * dt

    force = V2.ZERO
    torque = 0.0

    (newPosition, newAngle)
  }

  /** for debug */
  def tickAndApply(dt: Scalar): Unit = {
    val (nposition, nangle) = tickAndGetNewPosition(dt)

    position = nposition
    angle = nangle
  }
}
