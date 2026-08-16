package service;

import model.Driver;

import java.util.ArrayList;
import java.util.List;


public class DriverService {

    private final List<Driver> drivers = new ArrayList<>();

    public void registerDriver(Driver driver){
        if(driver == null){
            throw new IllegalArgumentException("Driver cannot be null");
        }
        drivers.add(driver);
    }


    public List<Driver> getAvailableDrivers(){
        return drivers.stream()
                .filter(Driver::isAvailable)
                .toList();
    }


    public void updateAvailability(Driver driver, boolean isAvailable){
        if(driver != null) {
            driver.setAvailable(isAvailable);
        }
    }
}
