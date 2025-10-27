# Project Coding Rules (Non-Obvious Only)

- **ParkingTicketFactory.createTicket()** must be used instead of direct ParkingTicket constructor (ensures consistent ID format)
- **Observer notifications** happen AFTER state changes in ParkingLotServiceImpl - never call before slot.park()/unpark()
- **HourlyFeeCalculationStrategy** throws IllegalStateException if exitTime is null - always set exitTime before fee calculation
- **Vehicle-slot compatibility logic** in ParkingSlot.canFitVehicle() is the single source of truth (not duplicated elsewhere)
- **ParkingDisplayService** must be instantiated in service constructors - no static methods or singleton pattern used
- **Main.java imports from packages** despite being in root - package structure is intentional for design pattern separation