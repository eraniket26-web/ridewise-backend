package util;

import model.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {
    private static final Map<String,Location> locationMap = new HashMap<>();

     static {
        locationMap.put("Hinjewadi",new Location(18.590,73.736));
        locationMap.put("Kalewadi",new Location(13.190,74.756));
        locationMap.put("Hadapsar",new Location(10.150,70.626));
        locationMap.put("Mindspace",new Location(11.200,65.136));
        locationMap.put("Chakan",new Location(15.790,53.295));
    }

    private static List<Location> getLocations(){
        return List.of(new Location(18.5204, 73.8567),
            new Location(19.0760, 72.8777),
            new Location(12.9716, 77.5946),
            new Location(28.7041, 77.1025));
    }


    public static Location getRandomLocation() {
        int randomIndex = ThreadLocalRandom.current().nextInt(getLocations().size());
        return getLocations().get(randomIndex);
    }

    public static Map<String,Location> getLocationMap(){
        return locationMap;
    }
}
