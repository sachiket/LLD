package services;

import models.Floor;
import models.ParkingLot;
import models.ParkingSlot;

/**
 * Service class responsible for displaying parking lot information
 * Follows Single Responsibility Principle
 */
public class ParkingDisplayService {
    
    public void showAvailableSlots(ParkingLot parkingLot) {
        System.out.println("Available Parking Slots in " + parkingLot.getName() + ":");
        for (Floor floor : parkingLot.getFloors()) {
            System.out.println("Floor " + floor.getFloorNumber() + ":");
            boolean hasAvailable = false;
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.isAvailable()) {
                    hasAvailable = true;
                    System.out.println("  -> Slot #" + slot.getSlotNumber() + " [" + slot.getSize() + "]");
                }
            }
            if (!hasAvailable) {
                System.out.println("  No available slots on this floor.");
            }
        }
    }
    
    public void showParkingLotStatus(ParkingLot parkingLot) {
        System.out.println("Parking Lot: " + parkingLot.getName());
        System.out.println("Status Summary:");
        
        int totalSlots = 0;
        int availableSlots = 0;
        
        for (Floor floor : parkingLot.getFloors()) {
            for (ParkingSlot slot : floor.getSlots()) {
                totalSlots++;
                if (slot.isAvailable()) {
                    availableSlots++;
                }
            }
        }
        
        int occupiedSlots = totalSlots - availableSlots;
        double occupancyRate = totalSlots > 0 ? (double) occupiedSlots / totalSlots * 100 : 0;
        
        System.out.println("  Total Slots: " + totalSlots);
        System.out.println("  Available: " + availableSlots);
        System.out.println("  Occupied: " + occupiedSlots);
        System.out.println("  Occupancy Rate: " + String.format("%.1f%%", occupancyRate));
    }
    
    public void showFloorStatus(Floor floor) {
        System.out.println("Floor " + floor.getFloorNumber() + " Status:");
        for (ParkingSlot slot : floor.getSlots()) {
            String status = slot.isAvailable() ? "Available" : "Occupied";
            String vehicleInfo = slot.isAvailable() ? "" :
                " (" + slot.getParkedVehicle().getLicenseNumber() + ")";
            System.out.println("  Slot #" + slot.getSlotNumber() +
                             " [" + slot.getSize() + "] - " + status + vehicleInfo);
        }
    }
}