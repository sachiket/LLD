package strategies;

import enums.VehicleType;
import models.ParkingTicket;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Concrete implementation of FeeCalculationStrategy using hourly rates
 * Implements Strategy Design Pattern
 */
public class HourlyFeeCalculationStrategy implements FeeCalculationStrategy {
    
    private static final double BIKE_RATE_PER_HOUR = 10.0;
    private static final double CAR_RATE_PER_HOUR = 20.0;
    private static final double TRUCK_RATE_PER_HOUR = 40.0;
    
    @Override
    public double calculateFee(ParkingTicket ticket) {
        if (ticket.getExitTime() == null) {
            throw new IllegalStateException("Exit time must be set before calculating fee");
        }
        
        long durationInHours = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        // Minimum 1 hour charge
        if (durationInHours == 0) {
            durationInHours = 1;
        }
        
        double ratePerHour = getRateForVehicleType(ticket.getVehicle().getType());
        return durationInHours * ratePerHour;
    }
    
    private double getRateForVehicleType(VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                return BIKE_RATE_PER_HOUR;
            case CAR:
                return CAR_RATE_PER_HOUR;
            case TRUCK:
                return TRUCK_RATE_PER_HOUR;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}