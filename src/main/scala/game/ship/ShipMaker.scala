package game.ship

import utils.math.planar.V2
import utils.Shapes._

object ShipMaker {

  def makeShip(): Ship = {
    val hull = Compartment(
      modules = Seq(
        CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(-20, 0),
          shape = RectangleBasicShape(halfExtents = V2(1, 3)),
          materialProperties = new MaterialProperties(java.awt.Color.RED)
        )),
        CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(20, 0),
          shape = RectangleBasicShape(halfExtents = V2(1, 3)),
          materialProperties = new MaterialProperties(java.awt.Color.RED)
        )),
        CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(-15, -2),
          shape = RectangleBasicShape(halfExtents = V2(2, 1)),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        )),
        CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(5, -2),
          shape = RectangleBasicShape(halfExtents = V2(2, 1)),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        )),
      ),
      physicsProperties = new PhysicsProperties(
        shape = RectangleBasicShape(halfExtents = V2(30, 6)),
        materialProperties = new MaterialProperties(java.awt.Color.GRAY)
      )
    )
    
    //def!!!!! important

    def engineModules() = Seq(
      CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
        position = V2(-5, -0.5),
        shape = CircleBasicShape(radius = .5),
        materialProperties = new MaterialProperties(java.awt.Color.WHITE)
      )),
      CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
        position = V2(-5, 0.5),
        shape = CircleBasicShape(radius = .5),
        materialProperties = new MaterialProperties(java.awt.Color.WHITE)
      )),
    )
    val engine1 = Compartment(
      modules = engineModules(),
      physicsProperties = new PhysicsProperties(
        position = V2(-10, 8),
        shape = RectangleBasicShape(halfExtents = V2(5, 2)),
        materialProperties = new MaterialProperties(java.awt.Color.GRAY.darker())
      )
    )

    val engine2 = Compartment(
      modules = engineModules(),
      physicsProperties = new PhysicsProperties(
        position = V2(-10, -8),
        shape = RectangleBasicShape(halfExtents = V2(5, 2)),
        materialProperties = new MaterialProperties(java.awt.Color.GRAY.darker())
      )
    )

    val ship = new Ship(
      compartments = Seq(
        hull,
        engine1,
        engine2
      )
    )


    ship
  }
}
