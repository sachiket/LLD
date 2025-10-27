package models;

import java.util.List;
import java.util.UUID;

public class Building {
    private final String id = UUID.randomUUID().toString();
    private List<Elevator> elevators;
    private int floorCount;

    // Constructor
    public Building(List<Elevator> elevators, int floorCount) {
        this.elevators = elevators;
        this.floorCount = floorCount;
    }


    // Getters
    public String getId() {
        return id;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public int getFloorCount() {
        return floorCount;
    }

    // Setters
    public void setElevators(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id='" + id + '\'' +
                ", elevators=" + elevators +
                ", floorCount=" + floorCount +
                '}';
    }
}
