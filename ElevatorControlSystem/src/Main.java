import models.Building;
import models.Elevator;
import services.ElevatorService;
import managers.ElevatorManager;
import factories.BuildingFactory;
import enums.Direction;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Elevator Control System - Starting ===");
        
        // Create a building with 10 floors and 3 elevators
        Building building = BuildingFactory.createBuilding(10, 3, 8);
        
        // Initialize elevator manager with building's elevators
        ElevatorManager elevatorManager = ElevatorManager.getInstance();
        elevatorManager.addElevators(building.getElevators());
        
        // Create elevator service
        ElevatorService elevatorService = new ElevatorService();
        
        System.out.println("Building created with " + building.getFloorCount() + " floors and " + 
                          building.getElevators().size() + " elevators");
        
        // Display initial status
        elevatorService.displayElevatorStatus();
        
        // Demo scenarios
        System.out.println("=== Demo Scenarios ===");
        
        // Scenario 1: Person on floor 3 wants to go to floor 7
        System.out.println("\n--- Scenario 1: Floor 3 to Floor 7 ---");
        elevatorService.request(3, 7);
        elevatorService.displayElevatorStatus();
        
        // Scenario 2: Person on floor 1 wants to go to floor 5
        System.out.println("\n--- Scenario 2: Floor 1 to Floor 5 ---");
        elevatorService.request(1, 5);
        elevatorService.displayElevatorStatus();
        
        // Scenario 3: Person inside elevator wants to go to floor 9
        System.out.println("\n--- Scenario 3: Inside elevator to Floor 9 ---");
        String elevatorId = building.getElevators().get(0).getId();
        elevatorService.move(Direction.UP, 9, elevatorId);
        elevatorService.displayElevatorStatus();
        
        // Scenario 4: Multiple requests
        System.out.println("\n--- Scenario 4: Multiple requests ---");
        elevatorService.request(2, 8);
        elevatorService.request(6, 1);
        elevatorService.displayElevatorStatus();
        
        System.out.println("=== Elevator Control System - Demo Complete ===");
    }
}