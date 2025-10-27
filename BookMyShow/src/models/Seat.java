package models;

import enums.SeatType;

/**
 * Represents a seat in a screen
 * Similar to ParkingSlot in the reference implementation
 */
public class Seat {
    private final String seatId;
    private final int row;
    private final int column;
    private final SeatType seatType;
    private boolean isAvailable;
    private boolean isBooked;

    public Seat(String seatId, int row, int column, SeatType seatType) {
        this.seatId = seatId;
        this.row = row;
        this.column = column;
        this.seatType = seatType;
        this.isAvailable = true;
        this.isBooked = false;
    }

    /**
     * Books the seat
     */
    public void book() {
        this.isBooked = true;
        this.isAvailable = false;
    }

    /**
     * Releases the seat (cancellation)
     */
    public void release() {
        this.isBooked = false;
        this.isAvailable = true;
    }

    // Getters
    public String getSeatId() {
        return seatId;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isBooked() {
        return isBooked;
    }

    @Override
    public String toString() {
        return String.format("Seat{id='%s', row=%d, col=%d, type=%s, available=%s}", 
                           seatId, row, column, seatType, isAvailable);
    }
}