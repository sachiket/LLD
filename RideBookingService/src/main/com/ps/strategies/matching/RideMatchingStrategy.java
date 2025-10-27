package main.com.ps.strategies.matching;

import main.com.ps.models.Driver;
import main.com.ps.models.Location;
import main.com.ps.models.Rider;

import java.util.List;

public interface RideMatchingStrategy {
    Driver findDriver(Rider rider, Location from, List<Driver> allDrivers);
}