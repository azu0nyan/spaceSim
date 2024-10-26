package game.ship

import game.entity.WorldEntity
import game.physics.{EntityControlledByRigidBody, MassData}
import utils.math.planar.V2
import utils.Shapes.*

object DebugShipMaker {

  def makeShipw(): Ship = {
    val hull = Section(
      modules = Seq(
        SectionModuleEngine(
          physicsProperties = new PhysicsProperties(
            position = V2(10, -9),
            rotation = Math.PI / 2.0,
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 49600,
        ),  
        SectionModuleFiller(physicsProperties = new PhysicsProperties(
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
    
    val sideHull1 = Section(
      modules = Seq(
        
      ),
      physicsProperties = new PhysicsProperties(
        shape = RectangleBasicShape(halfExtents = V2(6, 6)),
        position = V2(-27, 9),
        mass = 4000.0,
        materialProperties = new MaterialProperties(java.awt.Color.GRAY)
      )
    )

    val sideHull2 = Section(
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
      sections = Seq(
        hull,
        sideHull1,
        sideHull2,
      )
    )


    ship
  }
  
  def makeShip(): Ship = {
    val hull = Section(
      modules = Seq(
        SectionModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = -Math.PI / 2,
            position = V2(25, 6),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 15500,
        ),
        SectionModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = Math.PI / 2,
            position = V2(25, -6),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 15500,
        ),
        SectionModuleEngine(
          physicsProperties = new PhysicsProperties(
            rotation = -Math.PI,
            position = V2(30, 0),
            shape = CircleBasicShape(radius = .5),
            materialProperties = new MaterialProperties(java.awt.Color.WHITE)
          ),
          thrust = 19500,
        ),
        SectionModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(-20, 0),
          shape = RectangleBasicShape(halfExtents = V2(1, 3)),
          materialProperties = new MaterialProperties(java.awt.Color.RED)
        )),
        SectionModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(20, 0),
          shape = RectangleBasicShape(halfExtents = V2(1, 3)),
          materialProperties = new MaterialProperties(java.awt.Color.RED)
        )),
        SectionModuleFiller(physicsProperties = new PhysicsProperties(
          position = V2(-15, -2),
          shape = RectangleBasicShape(halfExtents = V2(2, 1)),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        )),
        SectionModuleFiller(physicsProperties = new PhysicsProperties(
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
      SectionModuleEngine(
        physicsProperties = new PhysicsProperties(
          position = V2(-5, -0.5),
          shape = CircleBasicShape(radius = .5),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        ),
        thrust = 62600,
      ),
      SectionModuleEngine(
        physicsProperties = new PhysicsProperties(
          position = V2(-5, 0.5),
          shape = CircleBasicShape(radius = .5),
          materialProperties = new MaterialProperties(java.awt.Color.WHITE)
        ),
        thrust = 62600,
      ),
    )
    val engine1 = Section(
      modules = engineModules(),
      physicsProperties = new PhysicsProperties(
        position = V2(-10, 8),
        shape = RectangleBasicShape(halfExtents = V2(5, 2)),
        materialProperties = new MaterialProperties(java.awt.Color.GRAY.darker())
      )
    )

    val engine2 = Section(
      modules = engineModules(),
      physicsProperties = new PhysicsProperties(
        position = V2(-10, -8),
        shape = RectangleBasicShape(halfExtents = V2(5, 2)),
        materialProperties = new MaterialProperties(java.awt.Color.GRAY.darker())
      )
    )

    val ship = new Ship(
      sections = Seq(
        hull,
        engine1,
        engine2
      )
    )


    ship
  }

  def makeShipEntity(): WorldEntity[Ship] =
    EntityControlledByRigidBody(DebugShipMaker.makeShip())
}
