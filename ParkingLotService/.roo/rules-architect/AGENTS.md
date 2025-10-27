# Project Architecture Rules (Non-Obvious Only)

- **Service layer separation** - ParkingLotServiceImpl handles business logic, ParkingDisplayService handles presentation (SRP violation avoided)
- **Observer pattern timing** - Notifications sent AFTER state changes to ensure consistency (slot.park() before notifyVehicleParked())
- **Strategy pattern default** - HourlyFeeCalculationStrategy hardcoded as default in constructor (not configurable)
- **Singleton thread safety** - ParkingLotManager uses synchronized getInstance() but internal collections are not thread-safe
- **Factory pattern centralization** - ParkingTicketFactory ensures consistent ticket ID format across system
- **No database layer** - All state maintained in memory using HashMap and ArrayList (not persistent)
- **Floor-slot relationship** - Slots don't know their floor number (tracked separately in Floor class)