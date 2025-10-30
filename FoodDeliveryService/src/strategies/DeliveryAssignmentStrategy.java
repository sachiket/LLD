package strategies;

import models.DeliveryPartner;
import models.Restaurant;
import java.util.List;

public interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(Restaurant restaurant, List<DeliveryPartner> availablePartners);
}