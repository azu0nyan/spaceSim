package game

import game.ship.ShipMaker
import utils.datastructures.spatial.AARectangle
import utils.math.planar.V2
import utils.math._

object GameMaker {
  def initGame(): Game =
    val game = new Game
    
    addRandomStars(game, 25)
    
    for(i <- 0 until 64) {
      val ship = ShipMaker.makeShipEntity()
      ship.setLocalPosition(V2(400, 0).rotate(i * TWO_PI / 64.0))
      ship.setLocalRotation(i * TWO_PI / 64.0)

      game.addWorldEntity(ship)
    }
    game
  end initGame
    

  def addRandomStars(game: Game, count: Int): Unit = {
    val toAvoid = AARectangle(V2(-1000000), V2(1000000))
    for (_ <- 0 until count) {
      val randomPosX = (math.random() - 0.5) * 10000000
      val randomPosY = (math.random() - 0.5) * 10000000
      val star = Star(V2(randomPosX, randomPosY))
      if(!toAvoid.intersects(star.shape.aabb)) {
        game.entities = star +: game.entities
      } else {
        println(s"Can't add star at $randomPosX, $randomPosY, intersection")
      }
    }
  }
}
