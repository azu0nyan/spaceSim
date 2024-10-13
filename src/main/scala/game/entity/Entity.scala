package game.entity

import render.{DrawableSnapshot, DrawableSnapshotParams}
import utils.math.Scalar

trait Entity{
  def tick(dt: Scalar): Unit
  
  def onAttach(entity: WorldEntity[_]): Unit = {}
  
  def drawableSnapshot(params: DrawableSnapshotParams): Option[DrawableSnapshot] = None
}


