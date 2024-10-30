package game.ship.maker

import game.ship.Ship

object ShipMaker {
  def makeShip[T](
                   lSystem: LSystem[T],
                   lSystemInterpreter: LSystemInterpreter[T])(
                   iteration: Int
                 ): Either[CommandInterpreter.InterpreterError, Ship] = {
    val lSystemOutput =
      lSystem
        .nth(iteration)

    val commands =
      lSystemInterpreter
        .interpret(lSystem.initialState)(lSystemOutput)

    CommandInterpreter
      .interpret(commands)
  }
}
