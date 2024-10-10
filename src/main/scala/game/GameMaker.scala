package game

import game.ship.ShipMaker
import utils.datastructures.spatial.AARectangle
import utils.math.planar.V2

object GameMaker {
  def initGame(): Game =
    val game = new Game
    
    val star = new Star(V2.ZERO, 1)
    game.entities = star +: game.entities
    addRandomStars(game, 25)
    
    
    val ship = ShipMaker.makeShip()
    game.addEntity(ship)
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
