package game.ship.maker.rw

import game.ship.maker.{LSystem, LSystemInterpreter}
import utils.math.planar.V2

import scala.util.{Success, Try}

object LSystemReader {

  sealed trait LSystemError extends Exception

  case object AxiomNotFound extends LSystemError
  case object CantReadAxiom extends LSystemError
  case object AxiomIsEmpty extends LSystemError
  case object CantReadRules extends LSystemError
  case class CantParseRule(s: String) extends LSystemError
  case class OtherError(s: String, t: Option[Throwable]) extends LSystemError

  def readFromLines(lines: Seq[String]): Either[LSystemError, LSystem[Char]] =
    Try {
      val noComments = lines.filterNot(_.startsWith("#"))


      val kvMap = lines
        .filter(_.contains(":"))
        .map(_.split(":"))
        .map  { 
          case Array(k, v) => (k.trim, v.trim)
          case other => throw OtherError(s"Can't parse ${other.mkString("(", ": ", ")")}", None)  
        
        }
        .toMap

      val rulesId =
        noComments.indexOf("!rules")

      val rules =
        if (rulesId < 0)
          throw CantReadRules
        else if (rulesId >= lines.size)
          throw CantReadRules
        else
          noComments
            .drop(rulesId + 1)
            .map(parseRule)
            .toMap


      def strToV2(str: String): V2 =
        val Array(x, y) = str.split(" ")
        V2(x.toDouble, y.toDouble)

      val default = LSystemInterpreter.State[Char]()
      val state =
        default
          .copy(position = kvMap.get("position").map(strToV2).getOrElse(default.position))
          .copy(rotation = kvMap.get("rotation").map(_.toDouble).getOrElse(default.rotation))
          .copy(width = kvMap.get("width").map(_.toDouble).getOrElse(default.width))
          .copy(height = kvMap.get("height").map(_.toDouble).getOrElse(default.height))
          .copy(widthDelta = kvMap.get("widthDelta").map(_.toDouble).getOrElse(default.widthDelta))
          .copy(heightDelta = kvMap.get("heightDelta").map(_.toDouble).getOrElse(default.heightDelta))
          .copy(turnAngleDegrees = kvMap.get("turnAngleDegrees").map(_.toDouble).getOrElse(default.turnAngleDegrees))
          .copy(forwardLength = kvMap.get("forwardLength").map(_.toDouble).getOrElse(default.forwardLength))
          .copy(makeConnections = kvMap.get("makeConnections").map(_.toBoolean).getOrElse(default.makeConnections))


      LSystem(
        axiom = kvMap.getOrElse("axiom", throw AxiomNotFound).toSeq,
        rules = rules,
        initialState = state,
      )

    }.transform(
        x => Success(Right(x)),
        {
          case e: LSystemError => Success(Left(e))
          case e: Throwable => 
            (e.printStackTrace())
            Success(Left(OtherError(e.getMessage, Some(e))))
        })
      .get


  def parseRule(s: String): (Char, Seq[Char]) = {
    val Array(a, b) = s.split("->")
    (a(0), b.strip().toSeq)
  }

}
