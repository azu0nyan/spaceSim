import drawing.Drawing
import game.{Game, GameMaker}
import render.{GameControls, GameDrawer, GameUpdater}
import utils.datastructures.IntV2
import utils.math.planar.V2

object Main{

  def main(args: Array[String]): Unit = {
    Drawing.startDrawingThread(
      size = IntV2(1920, 1080),
      decorated = true
    )

    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight + 1)
    Thread.sleep(100)
    Drawing.setSize(Drawing.getWidth, Drawing.getHeight - 1)
    
    
    val game = GameMaker.initGame()
    val gameUpdater = new GameUpdater(game.tick)
    val gameDrawer = new GameDrawer(() => game.getDrawablesSnapshot(V2.ZERO, V2(1920, 1080)))
    Drawing.addDrawable(gameUpdater)
    Drawing.addDrawable(gameDrawer)
    GameControls.init(Drawing, game)




  }

}
