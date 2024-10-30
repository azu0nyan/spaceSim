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
                       width: Scalar = 0,
                       height: Scalar = 0,
                       makeConnections: Boolean = true,
                       turnAngle: Scalar = 0,
                       forwardLength: Scalar = 0,
                       widthDelta: Scalar = 0,
                       heightDelta: Scalar = 0,
                       stateStack: Option[State[T]] = None,
                     )
}
