package strategies;

import models.Elevator;
import enums.Direction;
import java.util.List;

public class NearestElevatorStrategy implements SchedulingStrategy {
    
    @Override
    public Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction) {
        if (availableElevators.isEmpty()) {
            return null;
        }
        
        Elevator nearest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : availableElevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - requestFloor);
            
            // Prefer elevators moving in the same direction or idle
            if (elevator.getDirection() == direction || elevator.getDirection() == Direction.IDLE) {
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = elevator;
                }
            }
        }
        
        // If no elevator found moving in same direction, pick the nearest one
        if (nearest == null) {
            for (Elevator elevator : availableElevators) {
                int distance = Math.abs(elevator.getCurrentFloor() - requestFloor);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = elevator;
                }
            }
        }
        
        return nearest;
    }
}