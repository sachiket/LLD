package models;

import enums.VehicleType;
import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public ParkingSlot getAvailableSlotFor(VehicleType vehicleType) {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable() && slot.canFitVehicle(vehicleType)) {
                return slot;
            }
        }
        return null; // No slot found
    }

    public void displayAvailableSlots() {
        System.out.println("Available slots on Floor " + floorNumber + ":");
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                System.out.println("  Slot #" + slot.getSlotNumber() + " (" + slot.getSize() + ")");
            }
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }
}
