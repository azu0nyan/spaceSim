package game.physics

import utils.math.Scalar
import utils.math.planar.V2

object PolygonOps {

  case class MassData(var mass: Scalar, var centroid: V2, var inertia: Scalar)
  def computeMass(vertices: IndexedSeq[V2], density: Scalar): MassData = {
    // Polygon mass, centroid, and inertia.
    // Let rho be the polygon density in mass per unit area.
    // Then:
    // mass = rho * int(dA)
    // centroid.x = (1/mass) * rho * int(x * dA)
    // centroid.y = (1/mass) * rho * int(y * dA)
    // I = rho * int((x*x + y*y) * dA)
    //
    // We can compute these integrals by summing all the integrals
    // for each triangle of the polygon. To evaluate the integral
    // for a single triangle, we make a change of variables to
    // the (u,v) coordinates of the triangle:
    // x = x0 + e1x * u + e2x * v
    // y = y0 + e1y * u + e2y * v
    // where 0 <= u && 0 <= v && u + v <= 1.
    //
    // We integrate u from [0,1-v] and then v from [0,1].
    // We also need to use the Jacobian of the transformation:
    // D = cross(e1, e2)
    //
    // Simplification: triangle centroid = (1/3) * (p1 + p2 + p3)
    //
    // The rest of the derivation is handled by computer algebra.

    var center = V2.ZERO
    var area = 0.0d
    var I = 0.0d

    // pRef is the reference point for forming triangles.
    // It's location doesn't change the result (except for rounding error).
    var s = V2.ZERO

    // This code would put the reference point inside the polygon.
    vertices.foreach(v => s += v)

    s = s / V2(vertices.size.toDouble)


    val k_inv3 = 1.0d / 3.0d
    for (i <- vertices.indices) {
      // Triangle vertices.
      var e1 = vertices(i) - s
      var e2 = (if (i + 1 < vertices.size) vertices(i + 1) else vertices(0)) - s
      val D = e1.cross(e2)
      val triangleArea = 0.5d * D
      area += triangleArea
      // Area weighted centroid
      center += triangleArea * k_inv3 * (e1 + e2)


      val ex1 = e1.x
      val ey1 = e1.y
      val ex2 = e2.x
      val ey2 = e2.y
      val intx2 = ex1 * ex1 + ex2 * ex1 + ex2 * ex2
      val inty2 = ey1 * ey1 + ey2 * ey1 + ey2 * ey2
      I += (0.25d * k_inv3 * D) * (intx2 + inty2)
    }
    var mdMass = density * area
    // Center of mass
    center = center / area

    val mdCenter = center + s

    var mdI = I * density
    // Shift to center of mass then to original body origin.
    mdI += mdMass * mdCenter ** mdCenter

    MassData(mdMass, mdCenter, mdI)
  }

}
