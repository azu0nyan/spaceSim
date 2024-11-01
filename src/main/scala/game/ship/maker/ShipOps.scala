package game.ship.maker

import game.ship.{MaterialProperties, PhysicsProperties, Section, SectionModuleEngine, SectionModuleWeaponEndpoint, Ship}
import game.ship.maker.CommandInterpreter.{InterpreterState, NoSectionToAddEngine, NoSectionToAddWeapon}
import utils.Shapes.{CircleBasicShape, RectangleBasicShape}
import utils.math.planar.V2

object ShipOps {

  def addSection(ship: Ship, interpreterState: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    val shape = RectangleBasicShape(
      center = V2.ZERO,
      halfExtents = V2(
        interpreterState.width / 2.0,
        interpreterState.height / 2.0,
      ),
      ox = V2.ox,
    )

    val mass = shape.area

    val section = new Section(
      physicsProperties = new PhysicsProperties(
        position = interpreterState.position,
        rotation = interpreterState.rotation,
        shape = shape,
        mass = mass,
      )
    )

    ship.sections = ship.sections :+ section

    Right(
      ship
    )
  }

  def addEngineEndpoint(ship: Ship, interpreterState: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    val shape = CircleBasicShape(
      center = V2.ZERO,
      radius = interpreterState.width
    )

    val spawnAt = interpreterState.position

    val sectionToAdd =
      ship.sections
        .find(_.physicsProperties.shapeAtTransform.contains(spawnAt))
        .orElse(
          ship.sections.minByOption(_.physicsProperties.position.distance(spawnAt))
        )

    sectionToAdd match
      case Some(parentSection) =>
        val spawnAtLocal = parentSection.physicsProperties.toLocalSpacePosition(spawnAt)
        val rotationLocal = parentSection.physicsProperties.toLocalSpaceRotation(interpreterState.rotation)

        val moduleWeapon = new SectionModuleEngine(
          physicsProperties = new PhysicsProperties(
            shape = shape,
            position = spawnAtLocal,
            rotation = rotationLocal,
            mass = shape.area,
            materialProperties = MaterialProperties(
              color = java.awt.Color.CYAN
            ),
          )
        )

        parentSection.modules = parentSection.modules :+ moduleWeapon

        Right(ship)
      case None =>
        println(s"No section to add engine $spawnAt $interpreterState")
        Left(NoSectionToAddEngine)
  }

  def addWeaponEndpoint(ship: Ship, interpreterState: InterpreterState): Either[CommandInterpreter.InterpreterError, Ship] = {
    val shape = CircleBasicShape(
      center = V2.ZERO,
      radius = interpreterState.width
    )

    val spawnAt = interpreterState.position

    val sectionToAdd =
      ship.sections
        .find(_.physicsProperties.shapeAtTransform.contains(spawnAt))
        .orElse(
          ship.sections.minByOption(_.physicsProperties.position.distance(spawnAt))
        )

    sectionToAdd match
      case Some(parentSection) =>
        val spawnAtLocal = parentSection.physicsProperties.toLocalSpacePosition(spawnAt)
        val rotationLocal = parentSection.physicsProperties.toLocalSpaceRotation(interpreterState.rotation)

        val moduleWeapon = new SectionModuleWeaponEndpoint(
          physicsProperties = new PhysicsProperties(
            shape = shape,
            position = spawnAtLocal,
            rotation = rotationLocal,
            mass = shape.area,
            materialProperties = MaterialProperties(
              color = java.awt.Color.RED
            ),
          )
        )

        parentSection.modules = parentSection.modules :+ moduleWeapon

        Right(ship)
      case None =>
        println(s"No section to add engine $spawnAt $interpreterState")
        Left(NoSectionToAddWeapon)
  }
}
