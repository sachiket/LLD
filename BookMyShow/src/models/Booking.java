package models;

import enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a booking in the BookMyShow system
 * Similar to ParkingTicket in the reference implementation
 */
public class Booking {
    private final String bookingId;
    private final String userId;
    private final Show show;
    private final List<Seat> bookedSeats;
    private final LocalDateTime bookingTime;
    private final double totalAmount;
    private BookingStatus status;

    public Booking(String bookingId, String userId, Show show, List<Seat> bookedSeats, double totalAmount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.bookingTime = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
    }

    /**
     * Confirms the booking
     */
    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
        // Book all seats
        for (Seat seat : bookedSeats) {
            seat.book();
        }
    }

    /**
     * Cancels the booking
     */
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        // Release all seats
        for (Seat seat : bookedSeats) {
            seat.release();
        }
    }

    /**
     * Expires the booking (if not confirmed within time limit)
     */
    public void expire() {
        this.status = BookingStatus.EXPIRED;
        // Release all seats
        for (Seat seat : bookedSeats) {
            seat.release();
        }
    }

    /**
     * Checks if booking is expired (15 minutes timeout)
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(bookingTime.plusMinutes(15));
    }

    // Getters
    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getBookedSeats() {
        return bookedSeats;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public int getNumberOfSeats() {
        return bookedSeats.size();
    }

    @Override
    public String toString() {
        return String.format("Booking{id='%s', userId='%s', show='%s', seats=%d, amount=%.2f, status=%s}", 
                           bookingId, userId, show.getShowId(), bookedSeats.size(), totalAmount, status);
    }
}