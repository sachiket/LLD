package services;

import managers.ElevatorManager;
import models.Elevator;
import enums.Direction;
import java.util.List;

public class ElevatorService {
    private final ElevatorManager elevatorManager;
    
    public ElevatorService() {
        this.elevatorManager = ElevatorManager.getInstance();
    }
    
    // People inside the lift
    public synchronized void move(Direction dir, int targetFloor, String elevatorId) {
        Elevator elevator = elevatorManager.getElevator(elevatorId);
        if (elevator == null) {
            System.out.println("Elevator not found: " + elevatorId);
            return;
        }
        
        System.out.println("Moving elevator " + elevatorId + " " + dir + " to floor " + targetFloor);
        
        while (elevator.getCurrentFloor() != targetFloor) {
            if (targetFloor > elevator.getCurrentFloor()) {
                elevatorManager.moveElevator(elevatorId, Direction.UP);
            } else if (targetFloor < elevator.getCurrentFloor()) {
                elevatorManager.moveElevator(elevatorId, Direction.DOWN);
            }
            
            System.out.println("Elevator " + elevatorId + " is now at floor " + elevator.getCurrentFloor());
            
            // Simulate movement time
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        elevatorManager.stopElevator(elevatorId);
        System.out.println("Elevator " + elevatorId + " reached target floor " + targetFloor);
    }
    
    // People outside lift
    public synchronized void request(int currFloor, int targetFloor) {
        Direction requestDirection = targetFloor > currFloor ? Direction.UP : Direction.DOWN;
        
        System.out.println("Request received: From floor " + currFloor + " to floor " + targetFloor + " (" + requestDirection + ")");
        
        // Find the best elevator using simple nearest strategy
        Elevator bestElevator = findNearestElevator(currFloor, requestDirection);
        
        if (bestElevator == null) {
            System.out.println("No available elevator found");
            return;
        }
        
        System.out.println("Assigned elevator: " + bestElevator.getId());
        
        // Move elevator to pickup floor first
        if (bestElevator.getCurrentFloor() != currFloor) {
            move(bestElevator.getCurrentFloor() < currFloor ? Direction.UP : Direction.DOWN,
                 currFloor, bestElevator.getId());
        }
        
        // Then move to target floor
        move(requestDirection, targetFloor, bestElevator.getId());
    }
    
    private Elevator findNearestElevator(int floor, Direction direction) {
        List<Elevator> availableElevators = elevatorManager.getAvailableElevators();
        
        if (availableElevators.isEmpty()) {
            return null;
        }
        
        Elevator nearest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Elevator elevator : availableElevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - floor);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = elevator;
            }
        }
        
        return nearest;
    }
    
    public void displayElevatorStatus() {
        List<Elevator> elevators = elevatorManager.getAllElevators();
        System.out.println("\n=== Elevator Status ===");
        for (Elevator elevator : elevators) {
            System.out.println(elevator);
        }
        System.out.println("=====================\n");
    }
}
