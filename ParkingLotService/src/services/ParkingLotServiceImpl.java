package services;

import models.*;
import strategies.FeeCalculationStrategy;
import strategies.HourlyFeeCalculationStrategy;
import factories.ParkingTicketFactory;
import observers.ParkingEventObserver;
import java.time.LocalDateTime;
import java.util.*;

public class ParkingLotServiceImpl implements ParkingLotService {
    private final ParkingLot parkingLot;
    private final Map<String, ParkingTicket> activeTickets;
    private final FeeCalculationStrategy feeCalculationStrategy;
    private final List<ParkingEventObserver> observers;
    private final ParkingDisplayService displayService;

    public ParkingLotServiceImpl(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        this.activeTickets = new HashMap<>();
        this.feeCalculationStrategy = new HourlyFeeCalculationStrategy();
        this.observers = new ArrayList<>();
        this.displayService = new ParkingDisplayService();
    }
    
    public ParkingLotServiceImpl(ParkingLot parkingLot, FeeCalculationStrategy feeCalculationStrategy) {
        this.parkingLot = parkingLot;
        this.activeTickets = new HashMap<>();
        this.feeCalculationStrategy = feeCalculationStrategy;
        this.observers = new ArrayList<>();
        this.displayService = new ParkingDisplayService();
    }
    
    public void addObserver(ParkingEventObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ParkingEventObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyVehicleParked(ParkingTicket ticket) {
        for (ParkingEventObserver observer : observers) {
            observer.onVehicleParked(ticket);
        }
    }
    
    private void notifyVehicleUnparked(ParkingTicket ticket, double fee) {
        for (ParkingEventObserver observer : observers) {
            observer.onVehicleUnparked(ticket, fee);
        }
    }
    
    private void notifyParkingFull() {
        for (ParkingEventObserver observer : observers) {
            observer.onParkingFull();
        }
    }
    
    private void notifySlotAvailable() {
        for (ParkingEventObserver observer : observers) {
            observer.onSlotAvailable();
        }
    }

    @Override
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        for (Floor floor : parkingLot.getFloors()) {
            ParkingSlot slot = floor.getAvailableSlotFor(vehicle.getType());
            if (slot != null) {
                slot.park(vehicle);
                ParkingTicket ticket = ParkingTicketFactory.createTicket(vehicle, slot);
                activeTickets.put(ticket.getTicketId(), ticket);
                notifyVehicleParked(ticket);
                return ticket;
            }
        }
        notifyParkingFull();
        return null;
    }

    @Override
    public double unparkVehicle(String ticketId) {
        if (!activeTickets.containsKey(ticketId)) {
            System.out.println("Invalid ticket ID.");
            return 0.0;
        }

        ParkingTicket ticket = activeTickets.get(ticketId);
        ticket.setExitTime(LocalDateTime.now());
        ParkingSlot slot = ticket.getSlot();
        slot.unpark();
        activeTickets.remove(ticketId);

        // Calculate fee using strategy pattern
        double fee = feeCalculationStrategy.calculateFee(ticket);
        
        notifyVehicleUnparked(ticket, fee);
        notifySlotAvailable();
        
        return fee;
    }

    @Override
    public void showAvailableSlots() {
        displayService.showAvailableSlots(parkingLot);
    }
    
    public void showParkingLotStatus() {
        displayService.showParkingLotStatus(parkingLot);
    }
    
    public void showFloorStatus(int floorNumber) {
        for (Floor floor : parkingLot.getFloors()) {
            if (floor.getFloorNumber() == floorNumber) {
                displayService.showFloorStatus(floor);
                return;
            }
        }
        System.out.println("Floor " + floorNumber + " not found.");
    }
}
