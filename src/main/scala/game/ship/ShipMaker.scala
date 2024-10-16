package game.ship

import game.entity.WorldEntity
import game.physics.{EntityControlledByRigidBody, MassData}
import utils.math.planar.V2
import utils.Shapes.*

object ShipMaker {

  def makeShipw(): Ship = {
    val hull = Compartment(
      modules = Seq(
        CompartmentModuleEngine(
          physicsProperties = new PhysicsProperties(
            position = V2(10, -9),
            rotation = Math.PI / 2.0,
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 49600,
        ),  
        CompartmentModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(-30, 0),
          shape = RectangleBasicShape(halfExtents = V2(4, 4)),
          mass = 2500.0,
          materialProperties = new MaterialProperties(java.awt.Color.GREEN),
        )),
      ),
      physicsProperties = new PhysicsProperties(
        shape = RectangleBasicShape(halfExtents = V2(30, 6)),
        mass = 100.0,
        materialProperties = new MaterialProperties(java.awt.Color.GRAY)
      )
    )
    
    val sideHull1 = Compartment(
      modules = Seq(
        
      ),
      physicsProperties = new PhysicsProperties(
        shape = RectangleBasicShape(halfExtents = V2(6, 6)),
        position = V2(-27, 9),
        mass = 4000.0,
        materialProperties = new MaterialProperties(java.awt.Color.GRAY)
      )
    )

    val sideHull2 = Compartment(
      modules = Seq(

      ),
      physicsProperties = new PhysicsProperties(
        shape = RectangleBasicShape(halfExtents = V2(6, 6)),
        position = V2(27, 9),
        mass = 4000.0,
        materialProperties = new MaterialProperties(java.awt.Color.GRAY)
      )
    )
    

    val ship = new Ship(
      compartments = Seq(
        hull,
        sideHull1,
        sideHull2,
      )
    )


    ship
  }
  
  def makeShip(): Ship = {
    val hull = Compartment(
      modules = Seq(
        CompartmentModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = -Math.PI / 2,
            position = V2(25, 6),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 15500,
        ),
        CompartmentModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = Math.PI / 2,
            position = V2(25, -6),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 15500,
        ),
        CompartmentModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = -Math.PI,
            position = V2(30, 0),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 19500,
        ),
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
      CompartmentModuleEngine(
        physicsProperties = new PhysicsProperties(
          position = V2(-5, -0.5),
          shape = CircleBasicShape(radius = .5),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        ),
        thrust = 62600,
      ),
      CompartmentModuleEngine(
        physicsProperties = new PhysicsProperties(
          position = V2(-5, 0.5),
          shape = CircleBasicShape(radius = .5),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        ),
        thrust = 62600,
      ),
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

  def makeShipEntity(): WorldEntity[Ship] =
    EntityControlledByRigidBody(ShipMaker.makeShip())
}
