# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## Build & Run Commands
```bash
# Compile all Java files (Windows)
javac -d out src\enums\*.java src\models\*.java src\strategies\*.java src\factories\*.java src\observers\*.java src\services\*.java src\managers\*.java src\Main.java

# Run the application
java -cp out Main
```

## Project-Specific Patterns

### Non-Obvious Design Decisions
- **ParkingTicketFactory** generates ticket IDs with format "TICKET-{8-char-UUID}" (not sequential numbers)
- **Observer pattern** notifications happen AFTER state changes, not before (critical for consistency)
- **Floor tracking limitation**: ConsoleNotificationObserver.getFloorNumber() returns hardcoded 0 (incomplete implementation)
- **Strategy pattern default**: ParkingLotServiceImpl defaults to HourlyFeeCalculationStrategy if none provided
- **Minimum fee rule**: All parking sessions charged minimum 1 hour regardless of actual duration

### Critical Implementation Details
- **Vehicle-slot compatibility**: BIKE fits any slot, CAR needs MEDIUM/LARGE, TRUCK needs LARGE only
- **Display service separation**: ParkingDisplayService handles all console output (SRP compliance)
- **Observer registration**: Must manually call addObserver() - no auto-registration
- **Ticket validation**: unparkVehicle() returns 0.0 for invalid tickets (not exception)
- **Thread safety**: ParkingLotManager uses synchronized getInstance() but collections are not thread-safe

### Package Structure Rationale
- **No default package**: Main.java imports from packages despite being in root
- **Strategy/Factory/Observer packages**: Separated by pattern type, not domain
- **Services package**: Contains both interface and implementation plus display service