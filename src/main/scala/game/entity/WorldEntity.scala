package game.entity

import utils.datastructures.spatial.AARectangle
import utils.math.Scalar
import utils.math.planar.V2

trait WorldEntity[DATA <: Entity] {

  def inner: DATA

  def localPosition: V2

  def localRotation: Scalar

  def move(dt: Scalar): Option[V2]

  def parent: Option[WorldEntity[_]] = None

  def childs: Seq[WorldEntity[_]] = Nil

  final def worldRotation: Scalar = parent match {
    case Some(parent) => parent.worldRotation + localRotation
    case None => localRotation
  }

  final def worldPosition: V2 = parent match {
    case Some(parent) => parent.worldPosition + localPosition.rotate(parent.worldRotation) //todo check
    case None => localPosition
  }

  /** Used to filter by area queries */
  def aabb: AARectangle = AARectangle(worldPosition, worldPosition)


  def setLocalPosition(position: V2): Unit
  
  def setLocalRotation(rotation: Scalar): Unit


  ////////////!!!!!!!!/////////////////////////
  ////EFFECT///////////////////////////////////
  ////////////!!!!!!!!/////////////////////////
  inner.onAttach(this)
}


