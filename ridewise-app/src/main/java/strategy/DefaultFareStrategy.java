package strategy;

import model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CalculateDistance;


public class DefaultFareStrategy implements FareStrategy{

    private static final double DEFAULT_BASE_FARE = 30.0;
    private static final double DEFAULT_RATE_PER_KM = 12.0;
    private static final double DEFAULT_MIN_FARE = 50.0;
    private static final Logger log = LoggerFactory.getLogger(DefaultFareStrategy.class);

    @Override
    public double calculateFare(Ride ride) {

        String vehicleType = ride.getVehicleType() != null ? ride.getVehicleType().toString().toUpperCase() : "CAR";
           log.info("Vehicle type selected by rider is {}", vehicleType);
            double baseFare;
            double ratePerKm;
            double minFare;


            switch (vehicleType){
                 case "BIKE" -> {
                     baseFare = 15.0;
                     ratePerKm = 7.0;
                     minFare = 30.0;
                 }case "AUTO" -> {
                    baseFare = 25.0;
                    ratePerKm = 10.0;
                    minFare = 40.0;
                }
                case "CAR" -> {
                    baseFare =  45.0;
                    ratePerKm =  30.0;
                    minFare = 100.0;
                }
                default -> {
                    baseFare = DEFAULT_BASE_FARE;
                    ratePerKm = DEFAULT_RATE_PER_KM;
                    minFare = DEFAULT_MIN_FARE;
                }
            }

            double distanceInKms = CalculateDistance.calculateDistanceInKm(
                                                ride.getRider().getLocation().getLatitude(),
                                                ride.getRider().getLocation().getLongitude(),
                                                ride.getDriver().getLocation().getLatitude(),
                                                ride.getDriver().getLocation().getLongitude()
                                        );

        double calculatedFare = baseFare + (ratePerKm * distanceInKms);
        return Math.max(calculatedFare, minFare);
    }
}
