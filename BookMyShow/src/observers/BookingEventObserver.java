package observers;

import models.Booking;
import models.Show;

/**
 * Observer interface for booking events
 * Implements Observer Design Pattern - similar to ParkingEventObserver
 */
public interface BookingEventObserver {
    void onBookingCreated(Booking booking);
    void onBookingConfirmed(Booking booking);
    void onBookingCancelled(Booking booking);
    void onShowFullyBooked(Show show);
    void onSeatsAvailable(Show show);
}