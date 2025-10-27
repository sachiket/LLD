package observers;

import models.Booking;
import models.Show;

/**
 * Console notification implementation of BookingEventObserver
 * Similar to ConsoleNotificationObserver in parking lot
 */
public class ConsoleNotificationObserver implements BookingEventObserver {

    @Override
    public void onBookingCreated(Booking booking) {
        System.out.println("[TICKET] Booking created successfully!");
        System.out.println("   Booking ID: " + booking.getBookingId());
        System.out.println("   Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("   Seats: " + booking.getNumberOfSeats());
        System.out.println("   Total Amount: ₹" + String.format("%.2f", booking.getTotalAmount()));
        System.out.println("   Status: " + booking.getStatus());
    }

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[SUCCESS] Booking confirmed successfully!");
        System.out.println("   Booking ID: " + booking.getBookingId());
        System.out.println("   Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("   Theater: " + booking.getShow().getTheater().getName());
        System.out.println("   Screen: " + booking.getShow().getScreen().getName());
        System.out.println("   Show Time: " + booking.getShow().getStartTime());
        System.out.println("   Seats Booked: " + booking.getNumberOfSeats());
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("[CANCELLED] Booking cancelled!");
        System.out.println("   Booking ID: " + booking.getBookingId());
        System.out.println("   Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("   Seats Released: " + booking.getNumberOfSeats());
    }

    @Override
    public void onShowFullyBooked(Show show) {
        System.out.println("[FULL] Show is now fully booked!");
        System.out.println("   Movie: " + show.getMovie().getTitle());
        System.out.println("   Theater: " + show.getTheater().getName());
        System.out.println("   Screen: " + show.getScreen().getName());
        System.out.println("   Show Time: " + show.getStartTime());
    }

    @Override
    public void onSeatsAvailable(Show show) {
        System.out.println("[AVAILABLE] Seats are now available!");
        System.out.println("   Movie: " + show.getMovie().getTitle());
        System.out.println("   Available Seats: " + show.getScreen().getAvailableSeatsCount());
    }
}