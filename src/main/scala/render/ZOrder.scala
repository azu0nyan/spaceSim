package render

object ZOrder {

  val bg = Int.MinValue
  val star = -100000
  val planet = -5000
  val asteroid = -10000

  val ship = 0

  object Ship {
    val hull = ship + 100
    val endpoint = ship + 200
    val engine = ship + 300
    val weapon = ship + 400
  }

  val debug = Int.MaxValue - 1000

}
