package strategy;

import exception.NoDriverAvailableException;
import model.Driver;
import model.Location;

import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    private static Integer MINIMUM_RIDE_COUNT = 5;

    @Override
    public Driver findDriver(Location riderLocation, List<Driver> availableDrivers) throws NoDriverAvailableException {

        if (availableDrivers == null || availableDrivers.isEmpty()) {
            throw new IllegalArgumentException("No available drivers");
        }

        Driver leastActiveDriver = availableDrivers.get(0);
        for (Driver driver : availableDrivers) {
            if (driver.getCompletedRideCount() < leastActiveDriver.getCompletedRideCount()) {
                leastActiveDriver = driver;
            }
        }
        return leastActiveDriver;
    }
}
