package observers;

import models.ParkingTicket;

/**
 * Concrete implementation of ParkingEventObserver for console notifications
 * Implements Observer Design Pattern
 */
public class ConsoleNotificationObserver implements ParkingEventObserver {
    
    @Override
    public void onVehicleParked(ParkingTicket ticket) {
        System.out.println("Vehicle " + ticket.getVehicle().getLicenseNumber() +
                          " parked successfully. Ticket ID: " + ticket.getTicketId());
        System.out.println("Location: Floor " + getFloorNumber(ticket) +
                          ", Slot " + ticket.getSlot().getSlotNumber());
    }
    
    @Override
    public void onVehicleUnparked(ParkingTicket ticket, double fee) {
        System.out.println("Vehicle " + ticket.getVehicle().getLicenseNumber() +
                          " unparked successfully.");
        System.out.println("Parking fee: Rs." + String.format("%.2f", fee));
        System.out.println("Duration: " + calculateDuration(ticket) + " hour(s)");
    }
    
    @Override
    public void onParkingFull() {
        System.out.println("Parking lot is full! No available slots.");
    }
    
    @Override
    public void onSlotAvailable() {
        System.out.println("Parking slot is now available!");
    }
    
    private int getFloorNumber(ParkingTicket ticket) {
        // This would need to be enhanced to track floor information
        // For now, returning a placeholder
        return 0;
    }
    
    private long calculateDuration(ParkingTicket ticket) {
        if (ticket.getExitTime() == null) {
            return 0;
        }
        return java.time.Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
    }
}