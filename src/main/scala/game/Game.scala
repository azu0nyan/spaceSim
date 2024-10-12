package game

import game.entity.{Entity, WorldEntity}
import render.DrawableSnapshot
import utils.math.Scalar
import utils.math.planar.V2

class Game {
  var entities: Seq[Entity] = Seq()
  var worldEntities: Seq[WorldEntity[_]] = Seq()

  def tick(dt: Scalar): Unit = {
    
    worldEntities.foreach(_.move(dt)) 
    entities.foreach(_.tick(dt))
    
  }

  def getDrawablesSnapshot(minPos: V2, maxPos: V2): Seq[DrawableSnapshot] =
    entities.flatMap(_.drawableSnapshot) //todo use min max

  def addEntity(entity: Entity): Unit =
    entities = entity +: entities
    
  def addWorldEntity(entity: WorldEntity[_]): Unit =
    worldEntities = entity +: worldEntities
    addEntity(entity.inner)
}

