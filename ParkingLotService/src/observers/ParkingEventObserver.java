package observers;

import models.ParkingTicket;
import models.Vehicle;

/**
 * Observer interface for parking events
 * Implements Observer Design Pattern
 */
public interface ParkingEventObserver {
    void onVehicleParked(ParkingTicket ticket);
    void onVehicleUnparked(ParkingTicket ticket, double fee);
    void onParkingFull();
    void onSlotAvailable();
}