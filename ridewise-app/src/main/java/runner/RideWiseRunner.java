package runner;


import exception.NoDriverAvailableException;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DriverService;
import service.RideService;
import service.RiderService;
import strategy.*;
import util.LocationUtil;
import util.PersonValidator;

import java.util.*;

public class RideWiseRunner {

    private static final Logger logger = LoggerFactory.getLogger(RideWiseRunner.class);
    private final RiderService riderService = new RiderService();
    private final DriverService driverService = new DriverService();
    private final PersonValidator validator = new PersonValidator();
    private final Scanner scanner;
    private final Map<Integer,String> vehicleTypeMap = new HashMap<>();
    private final RideService rideService;

    public RideWiseRunner() {
        this.scanner = new Scanner(System.in);
        this.rideService = new RideService(driverService);
        vehicleTypeMap.put(1,"Bike");
        vehicleTypeMap.put(2,"Auto");
        vehicleTypeMap.put(3,"Car");
    }

    public void start() {
     try {
         boolean running = true;

         while (running) {
             showMenu();

             int choice = scanner.nextInt();
             scanner.nextLine();

             switch (choice) {
                 case 1:
                     addRider();
                     break;

                 case 2:
                      addDriver();
                      break;

                 case 3:
                     try {
                         viewAvailableDrivers();
                     }catch (NoDriverAvailableException e){
                          logger.error("Driver not available", e);
                     }
                     break;

                 case 4:
                     requestRide();
                     break;

                 case 5:
                     logger.info(" Enter ride id:");
                     String rideId = scanner.nextLine();
                     Ride ride = completeRide(rideId);
                     logger.info("Completed Ride is {}", ride.getFareReceipt());
                     break;

                 case 6:
                     viewRides();
                     break;

                 case 7:
                     running = false;
                     logger.info("Thank you for using ridewise app ..bye !!");
                     break;

                 default:
                     logger.info("Invalid choice");
                     break;
             }


         }

     } catch (InputMismatchException e) {
          logger.error("Data mismatched entered by the user");
       } catch (NoDriverAvailableException e) {
         throw new RuntimeException(e);
     }
    }

    private void viewRides() {

        List<Ride> rides = rideService.getAllRides();

        if (rides.isEmpty()) {
            logger.info("No rides available.");
            return;
        }

        logger.info("========== Ride History ==========");

        rides.forEach(ride -> {

            logger.info("Ride ID       : {}", ride.getRideId());
            logger.info("Rider         : {}", ride.getRider().getName());

            if (ride.getDriver() != null) {
                logger.info("Driver        : {}", ride.getDriver().getName());
            } else {
                logger.info("Driver        : Not assigned");
            }

            logger.info("Vehicle       : {}", ride.getVehicleType());
            logger.info("Distance      : {}", ride.getDistance());
            logger.info("Destination   : {}", ride.getDestination());
            logger.info("Status        : {}", ride.getStatus());

            if (ride.getFareReceipt() != null) {
                logger.info("Fare          : {}", ride.getFareReceipt().getAmount());
                logger.info("Generated At  : {}", ride.getFareReceipt().getGeneratedAt());
            }

            logger.info("----------------------------------");
        });
    }

    public Ride completeRide(String rideId) {

        if (rideId == null || rideId.isBlank()) {
            throw new IllegalArgumentException("Ride id cannot be empty");
        }
        Ride ride = rideService.getRideById(rideId);
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Ride cannot be completed. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.COMPLETED);

        Driver driver = ride.getDriver();
        driverService.updateAvailability(driver, true);
        logger.info("Ride {} completed successfully", rideId);
        logger.info("Handed over fare receipt to rider {}", ride.getFareReceipt());
        logger.info("Driver {} is now available", driver.getDriverId());

        return ride;
    }


    private void requestRide() throws NoDriverAvailableException {
        logger.info(" \n Enter rider id ");
        Ride ride = new Ride();
        String riderId = scanner.nextLine();
        Rider rider = riderService.getRider((riderId!=null && !riderId.isEmpty()) ? riderId : "");
        if(rider != null){
          ride.setRider(rider);
        }

        logger.info("\n Enter destination");
        List<String> destinationNames = new ArrayList<>(LocationUtil.getLocationMap().keySet());
        logger.info("---- Choose drop point --- \n");

        if(!destinationNames.isEmpty()) {
            for (int i = 0; i < destinationNames.size(); i++) {
                logger.info("{}. {}", i + 1, destinationNames.get(i));
            }
            Location location = showLocationChoice(destinationNames, LocationUtil.getLocationMap());
            ride.setDestination(location);
        }

        logger.info("\n Enter Vehicle Type");
        logger.info("=== 1. Bike ======");
        logger.info("=== 2. Auto ======");
        logger.info("=== 3. Car =======");

        /**** Accept  vehicle type *****/
        int choice = Integer.parseInt(scanner.nextLine().trim());
        String vehicle = getVehicleByChoice(choice,ride);
        logger.info("Vehicle selected by rider is {}",vehicle);

       ride.setStatus(RideStatus.REQUESTED);

       /**** Choose ride matching strategy   ****/

       logger.info("\n Choose suitable ride");
       logger.info("=== 1. Nearest Driver ===");
       logger.info("=== 2. Least Driver ===");

        int strategyChoice = Integer.parseInt(scanner.nextLine().trim());
        RideMatchingStrategy matchingStrategy = getRideMatchingStrategy(strategyChoice);
        logger.info("Matching Strategy {}", matchingStrategy);


        /**** Choose Fare strategy   ****/
        logger.info("\n Choose suitable fare");
        logger.info("=== 1. Default fare ===");
        logger.info("=== 2. Peak hour Driver ===");

        int fareStrategyChoice = Integer.parseInt(scanner.nextLine().trim());
        FareStrategy fareStrategy = getFareMatchingStrategy(fareStrategyChoice);
        logger.info("Matching Strategy {}", fareStrategy);
        Ride assignedRide =  rideService.requestRide(ride,matchingStrategy,fareStrategy);
        logger.info("Your ride has been booked , Driver on the way {} and contact number is {} and your ride id is {}",
                               assignedRide.getDriver().getName(),
                               assignedRide.getDriver().getContactNo(),
                               assignedRide.getRideId());

    }

    private FareStrategy getFareMatchingStrategy(int fareStrategyChoice) {
        if(fareStrategyChoice < 1 || fareStrategyChoice > 2){
            throw  new IllegalArgumentException("Choice should be between 1 and 2");
        }
        return (fareStrategyChoice == 1) ? new DefaultFareStrategy() : new PeakHourStrategy();
    }

    private RideMatchingStrategy getRideMatchingStrategy(int choice) {
        if(choice < 1 || choice > 2){
            throw  new IllegalArgumentException("Choice should be between 1 and 2");
        }
        return (choice == 1) ? new NearestDriverStrategy() : new LeastActiveDriverStrategy();
    }



    private void viewAvailableDrivers() throws NoDriverAvailableException {
          List<Driver> availableDriversList = driverService.getAvailableDrivers();
          if(!availableDriversList.isEmpty()){
              logger.info("----- Available drivers ------- \n");
              availableDriversList.forEach(data -> logger.info(" Drivers {}", data));
          }else {
              logger.info(" Currently no driver is available now ... !!");
              throw new NoDriverAvailableException("No driver available at the moment !!");
          }
    }

    private void addDriver() {
        try {

            logger.info("\n ---Add Driver ----");
            Driver driver = new Driver();
            logger.info("\n Enter driver's  name: ");
            String name = scanner.nextLine().trim();
            validator.validateName(name);
            driver.setName(name);
            logger.info("\n Enter driver contact number ");
            String contactInput = scanner.nextLine().trim();
            long contactNo = Long.parseLong(contactInput);
            validator.validateContactNo(contactNo);
            driver.setContactNo(contactNo);
            driver.setLocation(LocationUtil.getRandomLocation());
            logger.info("Current location for driver is {}", driver.getLocation());

            driverService.registerDriver(driver);
            logger.info(" Driver Registered Successfully with id {} ", driver.getDriverId());

            driverService.updateAvailability(driver,true);

        }catch (Exception e){
            logger.error("Exception occurs during driver registration {}", e.getMessage());
        }
    }

    private void addRider() {
        try {

            logger.info("\n ---Add Rider ----");
            Rider rider = new Rider();
            logger.info("\n Enter your name: ");
            String name = scanner.nextLine().trim();
            validator.validateName(name);
            rider.setName(name);
            logger.info("\n Enter your contact number ");
            String contactInput = scanner.nextLine().trim();
            long contactNo = Long.parseLong(contactInput);
            validator.validateContactNo(contactNo);
            rider.setContactNo(contactNo);
            rider.setLocation(LocationUtil.getRandomLocation());
            logger.info("Current location for rider is {}", rider.getLocation());

            riderService.registerRider(rider);

//            logger.info("Rider {}", riderService.getRider(rider.getRiderId()));

        }catch (Exception e){
            logger.error("Exception occurs during rider registration {}", e.getMessage());
        }

    }

    private void showMenu() {
        logger.info("""
                
                ========== RideWise ==========
                1. Add Rider
                2. Add Driver
                3. View Available Drivers
                4. Request Ride
                5. Complete Ride
                6. View Rides
                7. Exit
                ==============================
                Enter your choice:""");
    }

    private Location showLocationChoice(List<String> destinationNames,
                                        Map<String,Location> locationOptionMap){
        Location selectedLocation = null;
        while (selectedLocation == null) {
            logger.info("Enter your choice (1-{}): ", destinationNames.size());
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice >= 1 && choice <= destinationNames.size()) {
                    String selectedName = destinationNames.get(choice - 1);
                    selectedLocation = locationOptionMap.get(selectedName);
                    logger.info("Selected destination: {}", selectedName);
                } else {
                    logger.warn("Invalid choice. Please pick between 1 and {}.", destinationNames.size());
                }
            } catch (NumberFormatException e) {
                logger.warn("Please enter a valid numeric choice.");
            }
        }

        return selectedLocation;
    }


    private String getVehicleByChoice(int choice, Ride ride){
        String vehicle =  vehicleTypeMap.getOrDefault(choice,"");
        if(!vehicle.isEmpty()){
            for(VehicleType vehicleType : VehicleType.values()){
                if(vehicle.equalsIgnoreCase(vehicleType.name())){
                    ride.setVehicleType(vehicleType);
                    break;
                }
            }
        }
       return vehicleTypeMap.getOrDefault(choice,"");
    }


    public static void main(String[] args) {
          new RideWiseRunner().start();
    }
}
