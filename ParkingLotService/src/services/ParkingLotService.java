package services;

import models.ParkingTicket;
import models.Vehicle;

public interface ParkingLotService {
    ParkingTicket parkVehicle(Vehicle vehicle);
    double unparkVehicle(String ticketId);
    void showAvailableSlots();
}
