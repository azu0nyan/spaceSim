package game.ship.maker

import game.ship.{PhysicsProperties, Section, Ship}
import game.ship.maker.CommandInterpreter.InterpreterState
import utils.Shapes.RectangleBasicShape
import utils.math.planar.V2

object ShipOps {

  def addSection(ship: Ship, interpreterStatе: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    val shape = RectangleBasicShape(
      center = V2.ZERO,
      halfExtents = V2(
        interpreterStatе.width / 2.0,
        interpreterStatе.height / 2.0,
      ),
      ox = V2.ox,
    )

    val mass = shape.area

    val section = new Section(
      physicsProperties = new PhysicsProperties(
        position = interpreterStatе.position,
        rotation = interpreterStatе.rotation,
        shape = shape,
        mass = mass,
      )
    )

    ship.sections = ship.sections :+ section

    Right(
      ship
    )
  }

  def addEngineEndpoint(ship: Ship, interpreterStatе: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    ???
  }

  def addWeaponEndpoint(ship: Ship, interpreterStatе: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    ???
  }
}