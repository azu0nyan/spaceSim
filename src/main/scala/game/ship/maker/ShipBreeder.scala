package game.ship.maker

import scala.util.Random

object ShipBreeder {
  case class ShipBreederParams(
                                seed: Int = 0,
                                splits: Int = 5,
                              )

  def breed(params: ShipBreederParams)(shipA: Seq[Command], shipB: Seq[Command]): Seq[Command] = {
    val r = new Random(params.seed)

    val shipASplitSize = shipA.size / params.splits
    val shipBSplitSize = shipB.size / params.splits

    val shipASplit = shipA.sliding(shipASplitSize, shipASplitSize).toSeq
    val shipBSplit = shipB.sliding(shipBSplitSize, shipBSplitSize).toSeq

    (0 until params.splits).flatMap(i => if (r.nextBoolean()) shipASplit(i) else shipBSplit(i))
  }
}
