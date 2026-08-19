package strategy;

import exception.NoDriverAvailableException;
import model.Driver;
import model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CalculateDistance;


import java.util.List;


public class NearestDriverStrategy implements RideMatchingStrategy{

    private static final Logger logger = LoggerFactory.getLogger(NearestDriverStrategy.class);


    @Override
    public Driver findDriver(Location riderLocation, List<Driver> driverList) throws NoDriverAvailableException {

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        if (driverList.isEmpty()){
            throw new NoDriverAvailableException("No driver available in your surrounding area !!");
        }

        for (Driver driver : driverList){
            logger.info("Driver locations are {}", driver.getLocation());
            double distInKms = CalculateDistance.calculateDistanceInKm(
                    riderLocation.getLatitude(),
                    riderLocation.getLongitude(),
                    driver.getLocation().getLatitude(),
                    driver.getLocation().getLongitude());

            if (distInKms < minDistance) {
                minDistance = distInKms;
                nearestDriver = driver;
                logger.info("Nearest driver location is {}", minDistance);
            }
        }

       logger.info("Nearest driver {}", nearestDriver);
        return nearestDriver;
    }

}
