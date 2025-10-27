package managers;

import models.Elevator;
import enums.Direction;
import enums.ElevatorStatus;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorManager {
    private static ElevatorManager instance;
    private final Map<String, Elevator> db;
    
    private ElevatorManager() {
        this.db = new ConcurrentHashMap<>();
    }
    
    public static synchronized ElevatorManager getInstance() {
        if (instance == null) {
            instance = new ElevatorManager();
        }
        return instance;
    }
    
    public void addElevator(Elevator elevator) {
        db.put(elevator.getId(), elevator);
    }
    
    public void addElevators(List<Elevator> elevators) {
        for (Elevator elevator : elevators) {
            db.put(elevator.getId(), elevator);
        }
    }
    
    public List<Elevator> getElevators(Direction dirStatus) {
        List<Elevator> result = new ArrayList<>();
        for (Elevator elevator : db.values()) {
            if (elevator.getDirection() == dirStatus) {
                result.add(elevator);
            }
        }
        return result;
    }
    
    public List<Elevator> getAllElevators() {
        return new ArrayList<>(db.values());
    }
    
    public Elevator getElevator(String elevatorId) {
        return db.get(elevatorId);
    }
    
    public synchronized void moveElevator(String elevatorId, Direction dir) {
        Elevator elevator = db.get(elevatorId);
        if (elevator != null) {
            elevator.setDirection(dir);
            elevator.setStatus(ElevatorStatus.MOVING);
            elevator.move();
            
            // Stop elevator if it reaches boundaries
            if (elevator.getCurrentFloor() <= 0 && dir == Direction.DOWN) {
                elevator.setDirection(Direction.IDLE);
                elevator.setStatus(ElevatorStatus.NOT_MOVING);
            }
        }
    }
    
    public void stopElevator(String elevatorId) {
        Elevator elevator = db.get(elevatorId);
        if (elevator != null) {
            elevator.setDirection(Direction.IDLE);
            elevator.setStatus(ElevatorStatus.NOT_MOVING);
        }
    }
    
    public List<Elevator> getAvailableElevators() {
        List<Elevator> result = new ArrayList<>();
        for (Elevator elevator : db.values()) {
            if (elevator.getStatus() == ElevatorStatus.NOT_MOVING || 
                elevator.getDirection() == Direction.IDLE) {
                result.add(elevator);
            }
        }
        return result;
    }
}