package service;

import model.Rider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.ArrayList;
import java.util.List;

public class RiderService {

    private static final Logger logger = LoggerFactory.getLogger(RiderService.class);
    private final List<Rider> riders = new ArrayList<>();

    public void registerRider(Rider rider){
        riders.add(rider);
        logger.info("{} registered successfully !!",rider.getName());
    }

    public Rider getRider(String riderId){
        return riders.stream()
                .filter(rider -> rider.getRiderId().equalsIgnoreCase(riderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rider not registered !!"));
    }

}
