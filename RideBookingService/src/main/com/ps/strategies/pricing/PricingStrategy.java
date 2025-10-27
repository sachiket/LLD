package main.com.ps.strategies.pricing;

import main.com.ps.models.Ride;

public interface PricingStrategy {
    double calculatePrice(Ride ride);
}
