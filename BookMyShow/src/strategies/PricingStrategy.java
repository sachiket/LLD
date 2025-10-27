package strategies;

import models.Show;
import models.Seat;
import java.util.List;

/**
 * Strategy interface for calculating booking prices
 * Implements Strategy Design Pattern - similar to FeeCalculationStrategy in parking lot
 */
public interface PricingStrategy {
    double calculatePrice(Show show, List<Seat> seats);
}