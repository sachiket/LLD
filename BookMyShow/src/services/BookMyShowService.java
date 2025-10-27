package services;

import models.*;
import enums.SeatType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main service interface for BookMyShow system
 * Similar to ParkingLotService in the reference implementation
 */
public interface BookMyShowService {
    
    // Movie operations
    List<Movie> getAllMovies();
    List<Movie> searchMoviesByTitle(String title);
    
    // Show operations
    List<Show> getShowsForMovie(String movieId);
    List<Show> getShowsForTheater(String theaterId);
    List<Show> getShowsForDate(LocalDateTime date);
    
    // Booking operations
    Booking createBooking(String userId, String showId, List<String> seatIds);
    boolean confirmBooking(String bookingId);
    boolean cancelBooking(String bookingId);
    Booking getBookingById(String bookingId);
    
    // Seat operations
    List<Seat> getAvailableSeats(String showId);
    List<Seat> getAvailableSeatsByType(String showId, SeatType seatType);
    
    // Display operations
    void displayMovies();
    void displayShows(String movieId);
    void displaySeatLayout(String showId);
    void displayBookingDetails(String bookingId);
}