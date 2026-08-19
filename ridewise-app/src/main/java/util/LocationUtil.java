package util;

import model.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

    private LocationUtil() { }

    private static final Map<String,Location> locationMap = new HashMap<>();

     static {
         locationMap.put("Hinjewadi Phase 1", new Location(18.5912, 73.7389));
         locationMap.put("Wakad",             new Location(18.5993, 73.7625));
         locationMap.put("Pimple Saudagar",   new Location(18.5987, 73.7931));
         locationMap.put("Kalewadi",          new Location(18.6081, 73.7915));
         locationMap.put("Baner",             new Location(18.5590, 73.7868));
         locationMap.put("Shivajinagar",      new Location(18.5308, 73.8474));
         locationMap.put("Hadapsar",          new Location(18.5089, 73.9259));
         locationMap.put("Chakan",            new Location(18.7606, 73.8626));
    }

    private static List<Location> getLocations() {
        return List.of(
                new Location(18.5626, 73.8087),
                new Location(18.9074, 72.8077),
                new Location(18.8679, 74.9143),
                new Location(18.3590, 75.8508)
        );
    }


    public static Location getRandomLocation() {
        int randomIndex = ThreadLocalRandom.current().nextInt(getLocations().size());
        return getLocations().get(randomIndex);
    }

    public static Map<String,Location> getLocationMap(){
        return locationMap;
    }
}
