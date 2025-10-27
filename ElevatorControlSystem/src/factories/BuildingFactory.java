package factories;

import models.Building;
import models.Elevator;
import java.util.ArrayList;
import java.util.List;

public class BuildingFactory {
    
    public static Building createBuilding(int floorCount, int elevatorCount) {
        List<Elevator> elevators = new ArrayList<>();
        
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(ElevatorFactory.createElevator());
        }
        
        return new Building(elevators, floorCount);
    }
    
    public static Building createBuilding(int floorCount, int elevatorCount, int elevatorCapacity) {
        List<Elevator> elevators = new ArrayList<>();
        
        for (int i = 0; i < elevatorCount; i++) {
            elevators.add(ElevatorFactory.createElevator(elevatorCapacity, 0));
        }
        
        return new Building(elevators, floorCount);
    }
    
    public static Building createBuilding(List<Elevator> elevators, int floorCount) {
        return new Building(elevators, floorCount);
    }
}