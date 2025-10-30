package strategies;

import models.DeliveryPartner;
import models.Restaurant;
import java.util.List;

public class NearestPartnerStrategy implements DeliveryAssignmentStrategy {
    
    @Override
    public DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners) {
        if (availablePartners == null || availablePartners.isEmpty()) {
            return null;
        }
        
        DeliveryPartner nearestPartner = null;
        double minDistance = Double.MAX_VALUE;
        
        for (DeliveryPartner partner : availablePartners) {
            if (partner.isAvailable()) {
                double distance = restaurant.getLocation().calculateDistance(partner.getLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPartner = partner;
                }
            }
        }
        
        return nearestPartner;
    }
}