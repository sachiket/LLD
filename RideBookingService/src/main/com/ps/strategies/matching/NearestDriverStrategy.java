package main.com.ps.strategies.matching;

import main.com.ps.models.Driver;
import main.com.ps.models.Location;
import main.com.ps.models.Rider;

import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy{

    @Override
    public Driver findDriver(Rider rider, Location from, List<Driver> allDrivers) {
        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver d : allDrivers) {
            if (!d.isAvailable()) continue;
            double dist = d.getCurrentLocation().distanceTo(from);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = d;
            }
        }
        return nearest;
    }
}
