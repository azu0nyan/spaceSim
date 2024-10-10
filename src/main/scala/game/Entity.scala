package game

import render.DrawableSnapshot
import utils.math.Scalar

trait Entity{
  def tick(dt: Scalar): Unit
  
  def drawableSnapshot: Option[DrawableSnapshot] = None
}


