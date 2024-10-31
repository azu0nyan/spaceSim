package game.ship.maker

import game.ship.maker
import game.ship.maker.LSystemInterpreter.State
import utils.math.Scalar
import utils.math.planar.V2

trait LSystemInterpreter[T] {
  def interpret(initial: State[T])(lSystemOutput: Seq[T]): Seq[Command]
}

object LSystemInterpreter {

  case class State[T](
                       position: V2 = V2.ZERO,
                       rotation: Scalar = 0,
                       width: Scalar = 1,
                       height: Scalar = 1,
                       makeConnections: Boolean = true,
                       turnAngleDegrees: Scalar = 90,
                       forwardLength: Scalar = 1,
                       widthDelta: Scalar = 1,
                       heightDelta: Scalar = 1,
                       stateStack: Option[State[T]] = None,
                     ) {
    def turnAngle = Math.toRadians(turnAngleDegrees)
  }
}
