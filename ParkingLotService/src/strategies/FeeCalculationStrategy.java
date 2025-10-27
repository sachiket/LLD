package strategies;

import models.ParkingTicket;

/**
 * Strategy interface for calculating parking fees
 * Implements Strategy Design Pattern
 */
public interface FeeCalculationStrategy {
    double calculateFee(ParkingTicket ticket);
}