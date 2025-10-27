import enums.SlotSize;
import enums.VehicleType;
import models.*;
import services.ParkingLotService;
import services.ParkingLotServiceImpl;
import observers.ConsoleNotificationObserver;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Step 1: Setup parking lot
        ParkingLot parkingLot = new ParkingLot("MyLot");

        // Floor 0 with 3 slots
        Floor floor0 = new Floor(0);
        floor0.addSlot(new ParkingSlot(1, SlotSize.SMALL));
        floor0.addSlot(new ParkingSlot(2, SlotSize.MEDIUM));
        floor0.addSlot(new ParkingSlot(3, SlotSize.LARGE));
        parkingLot.addFloor(floor0);

        // Floor 1 with 2 slots
        Floor floor1 = new Floor(1);
        floor1.addSlot(new ParkingSlot(1, SlotSize.MEDIUM));
        floor1.addSlot(new ParkingSlot(2, SlotSize.LARGE));
        parkingLot.addFloor(floor1);

        // Step 2: Create service with observer
        ParkingLotServiceImpl parkingService = new ParkingLotServiceImpl(parkingLot);
        ConsoleNotificationObserver observer = new ConsoleNotificationObserver();
        parkingService.addObserver(observer);

        // Step 3: Park vehicles
        Vehicle bike = new Vehicle("KA-01-BIKE", VehicleType.BIKE);
        Vehicle car = new Vehicle("KA-02-CAR", VehicleType.CAR);
        Vehicle truck = new Vehicle("KA-03-TRUCK", VehicleType.TRUCK);

        ParkingTicket ticket1 = parkingService.parkVehicle(bike);
        ParkingTicket ticket2 = parkingService.parkVehicle(car);
        ParkingTicket ticket3 = parkingService.parkVehicle(truck);

        // Step 4: Show parking lot status
        System.out.println("\n" + "=".repeat(50));
        parkingService.showParkingLotStatus();
        System.out.println("\n" + "=".repeat(50));
        parkingService.showAvailableSlots();

        // Wait a bit for time difference (simulate time passed)
        Thread.sleep(1000); // 1 second

        // Step 5: Unpark and show fee
        if (ticket1 != null)
            parkingService.unparkVehicle(ticket1.getTicketId());

        if (ticket2 != null)
            parkingService.unparkVehicle(ticket2.getTicketId());

        // Step 6: Show final status
        System.out.println("\n" + "=".repeat(50));
        System.out.println("FINAL STATUS:");
        parkingService.showParkingLotStatus();
        System.out.println("\n" + "=".repeat(50));
        parkingService.showAvailableSlots();
    }
}
