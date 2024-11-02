package game.ship.maker

case class LSystem[T](
                       axiom: Seq[T],
                       rules: Map[T, Seq[T]],
                       initialState: LSystemInterpreter.State[T],
                     ) {
  def applyTo(chars: Seq[T]): Seq[T] =
    chars.flatMap(c =>
      rules
        .getOrElse(c, Seq(c))
    )

  def nth: Int => Seq[T] = LSystem.nth(this)
}


object LSystem {

  def applyString(axiom: String, rules: Map[Char, String], initialState: LSystemInterpreter.State[Char]): LSystem[Char] =
    LSystem(
      axiom.toSeq,
      rules.view.mapValues(_.toSeq).toMap,
      initialState,
    )

  def stream[T](lSystem: LSystem[T]) =
    LazyList
      .iterate(
        lSystem.axiom
      )(
        lSystem.applyTo
      )

  def nth[T](lSystem: LSystem[T])(n: Int) = stream(lSystem)(n)
}
