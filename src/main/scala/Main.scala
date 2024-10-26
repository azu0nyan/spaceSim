import drawing.Drawing
import drawing.library.FpsCounter
import game.ship.DebugShipMaker
import game.{Game, GameMaker}
import render.{DrawableSnapshotParams, GameControls, GameDrawer, GameUpdater}
import utils.datastructures.IntV2
import utils.math.planar.V2

import java.awt.Font

object Main {

  def main(args: Array[String]): Unit = {
    Drawing.startDrawingThread(
      size = IntV2(1920, 1080),
      decorated = true
    )

    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight + 1)
    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight - 1)
    Drawing.FpsCounter.font = new Font("", Font.BOLD, 30)
    Drawing.FpsCounter.dy = 100

    val game = GameMaker.initGame()
    val gameUpdater = new GameUpdater(game.tick)
    val gameDrawer = new GameDrawer(() => game.getDrawablesSnapshot(DrawableSnapshotParams(true, V2.ZERO, V2(1920, 1080))))
    Drawing.addDrawable(gameUpdater)
    Drawing.addDrawable(gameDrawer)
    val ship = DebugShipMaker.makeShipEntity()
    game.addWorldEntity(ship)

    GameControls.init(Drawing, game, ship)


  }

}
