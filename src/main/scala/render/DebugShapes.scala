package render

import drawing.library.DrawingUtils
import utils.math.planar.V2

import java.awt.{Color, Graphics2D}

sealed trait DebugShapes

object DebugShapes {
  case class DebugPointInWorldWithText(point: V2, text: String, color: Color = Color.WHITE, fontSize: Int = 12) extends DebugShapes

  def draw(debugShape: DebugShapes)(g: Graphics2D): Unit = {
    debugShape match {
      case DebugPointInWorldWithText(point, text, color, fontSize) =>
        DrawingUtils.drawText(text = text, where = point, g = g, fontSize = fontSize, scaleFont = false, color = color)
        DrawingUtils.drawPoint(point, g, color = color, pointWidth = 6)
    }
  }
}
