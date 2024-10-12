package game.entity

import render.DrawableSnapshot
import utils.math.Scalar

trait Entity{
  def tick(dt: Scalar): Unit
  
  def onAttach(entity: WorldEntity[_]): Unit = {}
  
  def drawableSnapshot: Option[DrawableSnapshot] = None
}


