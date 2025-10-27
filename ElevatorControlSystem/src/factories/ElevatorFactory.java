package factories;

import models.Elevator;
import enums.ElevatorStatus;
import enums.Direction;

public class ElevatorFactory {
    
    public static Elevator createElevator() {
        return new Elevator(
            ElevatorStatus.NOT_MOVING, 
            Direction.IDLE, 
            10, // default maximum capacity
            0,  // default current capacity
            0   // default starting floor
        );
    }
    
    public static Elevator createElevator(int maxCapacity, int startingFloor) {
        return new Elevator(
            ElevatorStatus.NOT_MOVING, 
            Direction.IDLE, 
            maxCapacity, 
            0, 
            startingFloor
        );
    }
    
    public static Elevator createElevator(ElevatorStatus status, Direction direction, 
                                        int maxCapacity, int currentCapacity, int currentFloor) {
        return new Elevator(status, direction, maxCapacity, currentCapacity, currentFloor);
    }
}