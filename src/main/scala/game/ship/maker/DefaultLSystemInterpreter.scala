package game.ship.maker

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
                                         throwStackEmpty: Boolean = true,
                                         throwErrorOnUnknown: Boolean = true,
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
      case `toggleMakeConnections` =>
        (Seq(), state.copy(makeConnections = !state.makeConnections))
      case `pushState` =>
        (Seq(), state.copy(stateStack = Some(state)))
      case `popState` =>
        (
          Seq(),
          state.stateStack.getOrElse(
            if (throwStackEmpty) throw new Exception(s"Stack is empty for state $state")
            else state
          )
        )
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

  def interpret(initial: LSystemInterpreter.State[T])(lSystemOutput: Seq[T]): Seq[Command] =
    lSystemOutput
      .foldLeft((Seq[Command](), initial)) {
        case ((commands, state), t) =>
          val (newCommands, newState) = mappings(t, state)
          (commands ++ newCommands, newState)
      }
      ._1
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
  )
}
