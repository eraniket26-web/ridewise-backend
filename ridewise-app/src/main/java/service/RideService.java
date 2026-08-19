package service;

import exception.NoDriverAvailableException;
import model.Driver;
import model.FareReceipt;
import model.Ride;
import model.RideStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import strategy.FareStrategy;
import strategy.RideMatchingStrategy;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RideService {

    private final DriverService driverService;
    private static final Logger logger = LoggerFactory.getLogger(RideService.class);
    private final List<Ride> rideList = new ArrayList<>();

    public RideService(DriverService driverService){
        this.driverService = driverService;
    }


    public Ride requestRide(Ride ride,
                            RideMatchingStrategy rideMatchingStrategy,
                            FareStrategy fareStrategy) throws NoDriverAvailableException {

         // step 1:- Get the list of available drivers for the ride
        List<Driver> availableDriversList = driverService.getAvailableDrivers();
          if(availableDriversList.isEmpty()){
              throw new NoDriverAvailableException("No driver available to take your ride");
          }

         // step 2:- Get the driver using ride matching strategy
         Driver driver = rideMatchingStrategy.findDriver(ride.getRider().getLocation(), availableDriversList);
          logger.info("Driver assigned {}", driver);
          ride.setDriver(driver);

          // step 3:- Change status to assigned
          ride.setStatus(RideStatus.ASSIGNED);
          driver.setAvailable(false);
          driver.addRide(ride);
          rideList.add(ride);

          // step 4:- Calculate the fare for the ride
           double amount =  fareStrategy.calculateFare(ride);
           logger.info("Fare for this ride is {}", amount);

           //step 5:- Generate fare receipt
        FareReceipt fareReceipt = new FareReceipt(ride.getRideId(), amount , Instant.now());
        ride.setFareReceipt(fareReceipt);

        return ride;
    }

    public Ride getRideById(String rideId){
        return rideList.stream()
                .filter(ride -> ride.getRideId().equalsIgnoreCase(rideId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ride id not found !!"));
    }

    public List<Ride> getAllRides() {
        return Collections.unmodifiableList(rideList);
    }


}
