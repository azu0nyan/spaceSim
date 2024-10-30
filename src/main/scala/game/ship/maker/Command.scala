package game.ship.maker

import utils.math.Scalar
import utils.math.planar.V2

sealed trait Command

object Command {

  case class MakeSection() extends Command()

  case class MakeWeaponEndpoint() extends Command

  case class MakeEngineEndpoint() extends Command

  case class MakeConnectionTo(offsetInLocal: V2) extends Command

  trait ModRotation extends Command {
    def mod: Scalar => Scalar
  }

  trait ModPosition extends Command {
    /**(Position, rotation)*/
    def mod: (V2, Scalar) => V2
  }

  trait ModWidth extends Command {
    def mod: Scalar => Scalar
  }

  trait ModHeight extends Command {
    def mod: Scalar => Scalar
  }

  /////HELPERS//////
  case class SetPosition(p: V2) extends ModPosition {
    def mod: (V2, Scalar) => V2 = (p, _) => p
  }

  case class OffsetPosition(offset: V2) extends ModPosition {
    def mod: (V2, Scalar) => V2 = (p, _) => p + offset
  }
  
  case class OffsetPositionLocal(offset: V2) extends Command {
    def mod: (V2, Scalar) => V2 = (p, r) => p + V2.ox.rotate(r) * offset
  }


  case class SetRotation(r: Scalar) extends ModRotation {
    def mod: Scalar => Scalar = _ => r
  }

  case class OffsetRotation(delta: Scalar) extends ModRotation {
    def mod: Scalar => Scalar = _ + delta
  }
  
  case class IncRotation15() extends ModRotation {
    def mod: Scalar => Scalar = _ + 15
  }

  case class DecRotation15() extends ModRotation {
    def mod: Scalar => Scalar = _ - 15
  }

  case class IncWidth() extends ModWidth {
    def mod: Scalar => Scalar = _ + 1
  }

  case class DecWidth() extends ModWidth {
    def mod: Scalar => Scalar = _ - 1
  }

  case class SetWidth(w: Scalar) extends ModWidth {
    def mod: Scalar => Scalar = _ => w
  }
  
  case class OffsetWidth(delta: Scalar) extends ModWidth {
    def mod: Scalar => Scalar = _ + delta
  }

  case class SetHeight(h: Scalar) extends ModHeight {
    def mod: Scalar => Scalar = _ => h
  }

  case class IncHeight() extends ModHeight {
    def mod: Scalar => Scalar = _ + 1
  }

  case class DecHeight() extends ModHeight {
    def mod: Scalar => Scalar = _ - 1
  }
  
  case class OffsetHeight(delta: Scalar) extends ModHeight {
    def mod: Scalar => Scalar = _ + delta
  }


}
