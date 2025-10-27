package strategies;

import models.Show;
import models.Seat;
import enums.SeatType;
import java.util.List;

/**
 * Standard pricing strategy implementation
 * Different prices for different seat types
 */
public class StandardPricingStrategy implements PricingStrategy {
    
    private static final double REGULAR_MULTIPLIER = 1.0;
    private static final double PREMIUM_MULTIPLIER = 1.5;
    private static final double VIP_MULTIPLIER = 2.0;

    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        double totalPrice = 0.0;
        double basePrice = show.getBasePrice();
        
        for (Seat seat : seats) {
            double seatPrice = basePrice * getSeatMultiplier(seat.getSeatType());
            totalPrice += seatPrice;
        }
        
        return totalPrice;
    }
    
    private double getSeatMultiplier(SeatType seatType) {
        switch (seatType) {
            case REGULAR:
                return REGULAR_MULTIPLIER;
            case PREMIUM:
                return PREMIUM_MULTIPLIER;
            case VIP:
                return VIP_MULTIPLIER;
            default:
                return REGULAR_MULTIPLIER;
        }
    }
}