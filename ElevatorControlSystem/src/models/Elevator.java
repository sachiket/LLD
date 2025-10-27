package models;

import java.util.UUID;
import enums.ElevatorStatus;
import enums.Direction;

public class Elevator {
    private final String id = UUID.randomUUID().toString();
    private ElevatorStatus status;
    private Direction direction;
    private int maximumCapacity;
    private int currentCapacity;
    private int currentFloor;

    // Constructor
    public Elevator(ElevatorStatus status, Direction direction, int maximumCapacity, int currentCapacity, int currentFloor) {
        this.status = status;
        this.direction = direction;
        this.maximumCapacity = maximumCapacity;
        this.currentCapacity = currentCapacity;
        this.currentFloor = currentFloor;
    }


    public void move(){
        if(direction == Direction.UP)
            currentFloor++;
        if(direction == Direction.DOWN && currentFloor > 0)
            currentFloor--;
    }

    // Getters
    public String getId() {
        return id;
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    // Setters
    public void setStatus(ElevatorStatus status) {
        this.status = status;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", direction=" + direction +
                ", maximumCapacity=" + maximumCapacity +
                ", currentCapacity=" + currentCapacity +
                ", currentFloor=" + currentFloor +
                '}';
    }
}
