package strategies;

import models.Elevator;
import enums.Direction;
import java.util.List;

public interface SchedulingStrategy {
    Elevator selectElevator(List<Elevator> availableElevators, int requestFloor, Direction direction);
}