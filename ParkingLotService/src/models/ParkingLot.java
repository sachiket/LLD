package models;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private final String name;
    private final List<Floor> floors;

    public ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
    }

    public void addFloor(Floor floor) {
        this.floors.add(floor);
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public String getName() {
        return name;
    }

    /**
     * Finds an available slot for the given vehicle type across all floors
     * @param vehicleType The type of vehicle to find a slot for
     * @return The first available slot that can accommodate the vehicle, or null if none found
     */
    public ParkingSlot findAvailableSlot(enums.VehicleType vehicleType) {
        for (Floor floor : floors) {
            ParkingSlot slot = floor.getAvailableSlotFor(vehicleType);
            if (slot != null) {
                return slot;
            }
        }
        return null;
    }
    
    /**
     * Gets the total number of slots in the parking lot
     * @return Total number of parking slots
     */
    public int getTotalSlots() {
        int total = 0;
        for (Floor floor : floors) {
            total += floor.getSlots().size();
        }
        return total;
    }
    
    /**
     * Gets the number of available slots in the parking lot
     * @return Number of available parking slots
     */
    public int getAvailableSlots() {
        int available = 0;
        for (Floor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.isAvailable()) {
                    available++;
                }
            }
        }
        return available;
    }
    
    /**
     * Checks if the parking lot is full
     * @return true if no slots are available, false otherwise
     */
    public boolean isFull() {
        return getAvailableSlots() == 0;
    }
}
