package models;

import enums.SeatType;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a screen in a theater
 * Similar to Floor in the parking lot reference implementation
 */
public class Screen {
    private final String screenId;
    private final String name;
    private final List<Seat> seats;
    private final int totalRows;
    private final int seatsPerRow;

    public Screen(String screenId, String name, int totalRows, int seatsPerRow) {
        this.screenId = screenId;
        this.name = name;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.seats = new ArrayList<>();
        initializeSeats();
    }

    /**
     * Initializes seats for the screen
     * Creates a mix of seat types for realistic theater layout
     */
    private void initializeSeats() {
        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= seatsPerRow; col++) {
                String seatId = String.format("%s-R%d-C%d", screenId, row, col);
                SeatType seatType = determineSeatType(row);
                seats.add(new Seat(seatId, row, col, seatType));
            }
        }
    }

    /**
     * Determines seat type based on row position
     * Front rows: REGULAR, Middle rows: PREMIUM, Back rows: VIP
     */
    private SeatType determineSeatType(int row) {
        if (row <= totalRows / 3) {
            return SeatType.REGULAR;
        } else if (row <= 2 * totalRows / 3) {
            return SeatType.PREMIUM;
        } else {
            return SeatType.VIP;
        }
    }

    /**
     * Gets available seats of a specific type
     */
    public List<Seat> getAvailableSeats(SeatType seatType) {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.isAvailable() && seat.getSeatType() == seatType) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    /**
     * Gets all available seats
     */
    public List<Seat> getAllAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    /**
     * Finds a seat by its ID
     */
    public Seat findSeatById(String seatId) {
        for (Seat seat : seats) {
            if (seat.getSeatId().equals(seatId)) {
                return seat;
            }
        }
        return null;
    }

    /**
     * Gets total available seats count
     */
    public int getAvailableSeatsCount() {
        int count = 0;
        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    // Getters
    public String getScreenId() {
        return screenId;
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getTotalSeats() {
        return seats.size();
    }

    @Override
    public String toString() {
        return String.format("Screen{id='%s', name='%s', totalSeats=%d, available=%d}", 
                           screenId, name, getTotalSeats(), getAvailableSeatsCount());
    }
}