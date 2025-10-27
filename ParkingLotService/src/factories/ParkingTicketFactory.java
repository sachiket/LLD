package factories;

import models.ParkingSlot;
import models.ParkingTicket;
import models.Vehicle;
import java.util.UUID;

/**
 * Factory class for creating ParkingTicket instances
 * Implements Factory Design Pattern
 */
public class ParkingTicketFactory {
    
    /**
     * Creates a new parking ticket with auto-generated ID
     * @param vehicle The vehicle to park
     * @param slot The parking slot assigned
     * @return A new ParkingTicket instance
     */
    public static ParkingTicket createTicket(Vehicle vehicle, ParkingSlot slot) {
        String ticketId = generateTicketId();
        return new ParkingTicket(ticketId, vehicle, slot);
    }
    
    /**
     * Creates a parking ticket with custom ID (for testing purposes)
     * @param ticketId Custom ticket ID
     * @param vehicle The vehicle to park
     * @param slot The parking slot assigned
     * @return A new ParkingTicket instance
     */
    public static ParkingTicket createTicket(String ticketId, Vehicle vehicle, ParkingSlot slot) {
        return new ParkingTicket(ticketId, vehicle, slot);
    }
    
    private static String generateTicketId() {
        return "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}