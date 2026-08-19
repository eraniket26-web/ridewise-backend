package strategy;

import exception.NoDriverAvailableException;
import model.Driver;
import model.Location;

import java.util.List;

public interface RideMatchingStrategy {
    Driver findDriver(Location riderLocation, List<Driver> driverList) throws NoDriverAvailableException;
}
