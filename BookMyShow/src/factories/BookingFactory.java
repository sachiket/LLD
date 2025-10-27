package factories;

import models.Booking;
import models.Show;
import models.Seat;
import java.util.List;
import java.util.UUID;

/**
 * Factory class for creating Booking instances
 * Implements Factory Design Pattern - similar to ParkingTicketFactory
 */
public class BookingFactory {
    
    /**
     * Creates a new booking with auto-generated ID
     */
    public static Booking createBooking(String userId, Show show, List<Seat> seats, double totalAmount) {
        String bookingId = generateBookingId();
        return new Booking(bookingId, userId, show, seats, totalAmount);
    }
    
    /**
     * Creates a booking with custom ID (for testing purposes)
     */
    public static Booking createBooking(String bookingId, String userId, Show show, List<Seat> seats, double totalAmount) {
        return new Booking(bookingId, userId, show, seats, totalAmount);
    }
    
    private static String generateBookingId() {
        return "BMS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}