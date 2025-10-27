package main.com.ps.strategies.pricing;

import main.com.ps.models.Ride;

public class NormalPricing implements PricingStrategy {
    private static final double BASE_FARE = 50.0;
    private static final double COST_PER_KM = 10.0;

    @Override
    public double calculatePrice(Ride ride) {
        double distance = ride.getFrom().distanceTo(ride.getTo());
        return BASE_FARE + (COST_PER_KM * distance);
    }
}

