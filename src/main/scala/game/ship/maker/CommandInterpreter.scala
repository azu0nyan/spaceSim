package game.ship.maker

import game.ship.Ship
import utils.math.Scalar
import utils.math.planar.V2

import scala.annotation.tailrec
import scala.collection.immutable.{AbstractSeq, LinearSeq}
import scala.util.Try

object CommandInterpreter {

  sealed trait InterpreterError

  case class InterpreterState(
                               width: Scalar,
                               height: Scalar,
                               position: V2,
                               rotation: Scalar,
                             )

  def interpret(commands: Seq[Command]): Either[InterpreterError, Ship] = {
    val state = InterpreterState(
      width = 10.0,
      height = 10.0,
      position = V2.ZERO,
      rotation = 0.0,
    )
    val ship = new Ship()


    interpret(state, ship)(commands)
      .map(_._2)
  }

  def interpret(state: InterpreterState, ship: Ship)(commands: Seq[Command]): Either[InterpreterError, (InterpreterState, Ship)] =
    foldLeftWhile[(InterpreterState, Ship), InterpreterError, Command] {
      case ((state, ship), command) => interpretCommand(state, ship)(command)
    }(
      (state, ship)
    )(
      commands
    )

  @tailrec
  def foldLeftWhile[ACC, ERR, EL](fold: (ACC, EL) => Either[ERR, ACC])(acc: ACC)(els: Seq[EL]): Either[ERR, ACC] =
    els match
      case Nil => Right(acc)
      case h :: t => fold(acc, h) match
        case Left(value) => Left(value)
        case Right(value) => foldLeftWhile(fold)(value)(t)


  def interpretCommand(state: InterpreterState, ship: Ship)(command: Command): Either[InterpreterError, (InterpreterState, Ship)] =
    command match
      case Command.MakeSection() => ShipOps.addSection(ship, state).map((state, _))
      case Command.MakeWeaponEndpoint() => ShipOps.addWeaponEndpoint(ship, state).map((state, _))
      case Command.MakeEngineEndpoint() => ShipOps.addEngineEndpoint(ship, state).map((state, _))
      case modRotation: Command.ModRotation => Right(state.copy(rotation = modRotation.mod(state.rotation)), ship)
      case modPosition: Command.ModPosition => Right(state.copy(position = modPosition.mod(state.position, state.rotation)), ship)
      case modWidth: Command.ModWidth => Right(state.copy(width = modWidth.mod(state.width)), ship)
      case modHeight: Command.ModHeight => Right(state.copy(height = modHeight.mod(state.height)), ship)
      case Command.MakeConnectionTo(offsetInLocal) => Right(state, ship) ///todo
      case Command.OffsetPositionLocal(offsetInLocal) =>
        Right(state.copy(position =
          state.position +
            offsetInLocal.x * V2.ox.rotate(state.rotation) +
            offsetInLocal.y * V2.oy.rotate(state.rotation)
        ), ship)

}
