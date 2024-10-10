package game

import render.DrawableSnapshot
import utils.math.Scalar
import utils.math.planar.V2

class Game {
  
  
  
  var entities: Seq[Entity] = Seq()

  def tick(dt: Scalar): Unit = {
    entities.foreach(_.tick(dt))    
  }
  
  def getDrawablesSnapshot(minPos: V2, maxPos: V2): Seq[DrawableSnapshot] =
    entities.flatMap(_.drawableSnapshot) //todo use min max
}

