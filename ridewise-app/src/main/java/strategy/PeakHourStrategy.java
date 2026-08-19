package strategy;

import model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CalculateDistance;



public class PeakHourStrategy implements FareStrategy{

    private static final double SURGE_MULTIPLIER = 1.25;
    private static final Logger log = LoggerFactory.getLogger(PeakHourStrategy.class);

    @Override
    public double calculateFare(Ride ride) {
        String vehicleType = ride.getVehicleType() != null ? ride.getVehicleType().toString().toUpperCase() : "CAR";
        log.info("Calculating peak hour fare for vehicle type: {}", vehicleType);

        // 1. Assign peak rates dynamically per vehicle type
        double baseFare;
        double ratePerKm;
        double minFare;

        switch (vehicleType) {
            case "BIKE" -> {
                baseFare = 20.0;
                ratePerKm = 10.0;
                minFare = 40.0;
            }
            case "AUTO" -> {
                baseFare = 35.0;
                ratePerKm = 14.0;
                minFare = 50.0;
            }
            case "CAR", "SEDAN" -> {
                baseFare = 45.0;
                ratePerKm = 20.0;
                minFare = 90.0;
            }
            default -> {
                baseFare = 40.0;
                ratePerKm = 18.0;
                minFare = 70.0;
            }
        }

        // 2. Calculate trip distance (Pickup -> Destination)
        double distanceInKm = CalculateDistance.calculateDistanceInKm(
                ride.getRider().getLocation().getLatitude(),
                ride.getRider().getLocation().getLongitude(),
                ride.getDriver().getLocation().getLatitude(),
                ride.getDriver().getLocation().getLongitude()
        );

        // 3. Compute base fare and apply surge multiplier
        double baseCalculatedFare = baseFare + (distanceInKm * ratePerKm);
        double totalFare = baseCalculatedFare * SURGE_MULTIPLIER;

        return Math.max(totalFare, minFare);
    }
}
