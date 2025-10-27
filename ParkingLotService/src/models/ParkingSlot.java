package models;

import enums.SlotSize;
import enums.VehicleType;

public class ParkingSlot {
    private final int slotNumber;
    private final SlotSize size;
    private boolean isAvailable;
    private Vehicle parkedVehicle;

    public ParkingSlot(int slotNumber, SlotSize size) {
        this.slotNumber = slotNumber;
        this.size = size;
        this.isAvailable = true;
    }

    public boolean canFitVehicle(VehicleType type) {
        switch (type) {
            case BIKE:
                return true;
            case CAR:
                return size == SlotSize.MEDIUM || size == SlotSize.LARGE;
            case TRUCK:
                return size == SlotSize.LARGE;
            default:
                return false;
        }
    }

    public void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.isAvailable = false;
    }

    public void unpark() {
        this.parkedVehicle = null;
        this.isAvailable = true;
    }

    // Getters
    public int getSlotNumber() {
        return slotNumber;
    }

    public SlotSize getSize() {
        return size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}
