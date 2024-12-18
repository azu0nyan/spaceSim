package game.ship.maker

import game.ship.maker.Command.OffsetWidth
import utils.math.planar.V2

case class DefaultLSystemInterpreter[T](
                                         forward: T,
                                         left: T,
                                         right: T,
                                         incWidth: T,
                                         incHeight: T,
                                         decWidth: T,
                                         decHeight: T,
                                         pushState: T,
                                         popState: T,
                                         toggleMakeConnections: T,
                                         makeSection: T,
                                         makeWeaponEndpoint: T,
                                         makeEngineEndpoint: T,
                                         divSizes: T,
                                         mullSizes: T,
                                         throwStackEmpty: Boolean = true,
                                         throwErrorOnUnknown: Boolean = false,
                                       ) extends LSystemInterpreter[T] {

  val mappings: (T, LSystemInterpreter.State[T]) => (Seq[Command], LSystemInterpreter.State[T]) = (t, state) =>
    t match
      case `forward` =>
        forwardCommands(state)
      case `left` => (
        Seq(Command.OffsetRotation(state.turnAngle)),
        state.copy(rotation = state.rotation + state.turnAngle)
      )
      case `right` =>
        (
          Seq(Command.OffsetRotation(-state.turnAngle)),
          state.copy(rotation = state.rotation - state.turnAngle)
        )
      case `incWidth` =>
        (
          Seq(Command.OffsetWidth(state.widthDelta)),
          state.copy(width = state.width + state.widthDelta)
        )

      case `incHeight` =>
        (
          Seq(Command.OffsetHeight(state.heightDelta)),
          state.copy(height = state.height + state.heightDelta)
        )
      case `decWidth` =>
        (
          Seq(Command.OffsetWidth(-state.widthDelta)),
          state.copy(width = state.width - state.widthDelta)
        )
      case `decHeight` =>
        (
          Seq(Command.OffsetHeight(-state.heightDelta)),
          state.copy(height = state.height - state.heightDelta)
        )
      case `divSizes` =>
        val newWidth = state.width / state.sizesDivisor
        val newHeight = state.height / state.sizesDivisor
        (
          Seq(
            Command.OffsetWidth(newWidth - state.width),
            Command.OffsetHeight(newHeight - state.height)
          ),
          state.copy(
            width = newWidth,
            height = newHeight,
            widthDelta = state.widthDelta / state.sizesDivisor,
            heightDelta = state.heightDelta / state.sizesDivisor,
          )
        )

      case `mullSizes` =>
        (
          Seq(
            Command.OffsetWidth(state.width * state.sizesDivisor - state.width),
            Command.OffsetHeight(state.height * state.sizesDivisor - state.height)
          ),
          state.copy(
            width = state.width * state.sizesDivisor,
            height = state.height * state.sizesDivisor,
            widthDelta = state.widthDelta * state.sizesDivisor,
            heightDelta = state.heightDelta * state.sizesDivisor,
          )
        )

      case `toggleMakeConnections` =>
        (Seq(), state.copy(makeConnections = !state.makeConnections))
      case `pushState` =>
        (Seq(), state.copy(stateStack = Some(state)))
      case `popState` =>
        popState(state)
      case `makeSection` =>
        (Seq(Command.MakeSection()), state)
      case `makeWeaponEndpoint` =>
        (Seq(Command.MakeWeaponEndpoint()), state)
      case `makeEngineEndpoint` =>
        (Seq(Command.MakeEngineEndpoint()), state)
      case _ if throwErrorOnUnknown =>
        throw new Exception(s"Unknown command $t for state $state")
      case _ =>
        (Seq(), state)

  def popState(state: LSystemInterpreter.State[T]): (Seq[Command], LSystemInterpreter.State[T]) = {
    val positionDelta = state.stateStack.map(value => value.position - state.position)

    val rotationDelta = state.stateStack.map(value => value.rotation - state.rotation)

    val widthDelta = state.stateStack.map(value => value.width - state.width)

    val heightDelta = state.stateStack.map(value => value.height - state.height)

    (
      Seq(
        positionDelta.map(Command.OffsetPosition),
        rotationDelta.map(Command.OffsetRotation),
        widthDelta.map(Command.OffsetWidth),
        heightDelta.map(Command.OffsetHeight),
      ).flatten,
      state.stateStack.getOrElse(
        if (throwStackEmpty) throw new Exception(s"Stack is empty for state $state")
        else state
      )
    )
  }

  def forwardCommands(state: LSystemInterpreter.State[T]): (Seq[Command], LSystemInterpreter.State[T]) =
    val positionDelta = V2.ox.rotate(state.rotation) * state.forwardLength
    val newPosition = state.position + positionDelta
    (
      Seq(
        Command.MakeConnectionTo(offsetInLocal = V2(state.forwardLength, 0)),
        Command.OffsetPositionLocal(V2(state.forwardLength, 0)),
      ),
      state.copy(position = newPosition)
    )

  def interpret(initial: LSystemInterpreter.State[T])(lSystemOutput: Seq[T]): Seq[Command] = {
    val initialCommands = Seq(
      Command.SetPosition(initial.position),
      Command.SetRotation(initial.rotation),
      Command.SetWidth(initial.width),
      Command.SetHeight(initial.height),
    )
    
    lSystemOutput
      .foldLeft((initialCommands, initial)) {
        case ((commands, state), t) =>
          val (newCommands, newState) = mappings(t, state)
          (commands ++ newCommands, newState)
      }
      ._1
  }
}

object DefaultLSystemInterpreter {
  val string = DefaultLSystemInterpreter[Char](
    forward = 'f',
    left = '-',
    right = '+',
    incWidth = 'W',
    incHeight = 'H',
    decWidth = 'w',
    decHeight = 'h',
    pushState = '[',
    popState = ']',
    toggleMakeConnections = 'c',
    makeSection = 'O',
    makeWeaponEndpoint = '*',
    makeEngineEndpoint = '^',
    divSizes = '/',
    mullSizes = '\\',
  )
}
