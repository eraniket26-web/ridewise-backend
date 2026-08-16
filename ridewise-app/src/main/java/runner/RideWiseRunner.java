package runner;


import exception.NoDriverAvailableException;
import model.Driver;
import model.Location;
import model.Ride;
import model.Rider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DriverService;
import service.RiderService;
import util.LocationUtil;
import util.PersonValidator;

import java.util.*;

public class RideWiseRunner {

    private static final Logger logger = LoggerFactory.getLogger(RideWiseRunner.class);
    private final RiderService riderService = new RiderService();
    private final DriverService driverService = new DriverService();
    private final PersonValidator validator = new PersonValidator();
    private final Scanner scanner;

    public RideWiseRunner() {
        this.scanner = new Scanner(System.in);
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

//                 case 5:
//                     completeRide();
//                     break;
//
//                 case 6:
//                     viewRides();
//                     break;

                 case 7:
                     running = false;
                     logger.info("Thank you for using ridewise app ..bye !!");
                     break;


             }


         }

     } catch (InputMismatchException e) {
          logger.error("Data mismatched entered by the user");
       }
    }

    private void requestRide() {
        logger.info(" \n Enter rider id ");
        Ride ride = new Ride();
        String riderId = scanner.nextLine();
        Rider rider = riderService.getRider((riderId!=null && !riderId.isEmpty()) ? riderId : "");
        if(rider != null){
          ride.setRider(rider);
        }

        logger.info("\n Enter destination");
        Map<String,Location> locationMap = LocationUtil.getLocationMap();
        logger.info("locationMap.keySet() {}", locationMap.keySet());
        List<String> destinationNames = new ArrayList<>(LocationUtil.getLocationMap().keySet());
        logger.info("---- Choose drop point --- \n");

        for(int i = 0 ; i < destinationNames.size() ; i++){
            logger.info("{}. {}", i + 1, destinationNames.get(i));
        }
        Location location = showLocationChoice(destinationNames,LocationUtil.getLocationMap());
        ride.setDestination(location);

        logger.info("\n Enter Vehicle Type");
        logger.info("=== 1. Bike ======");
        logger.info("=== 2. Auto ======");
        logger.info("=== 3. Car =======");



    }

    private void viewAvailableDrivers() throws NoDriverAvailableException {
          List<Driver> availableDriversList = driverService.getAvailableDrivers();
          if(!availableDriversList.isEmpty()){
              logger.info("----- Available drivers ------- \n");
              availableDriversList.forEach(data -> {
                   logger.info(" Drivers {}", data);
              });
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

            logger.info("Rider {}", riderService.getRider(rider.getRiderId()));

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


    public static void main(String[] args) {
          new RideWiseRunner().start();
    }
}
