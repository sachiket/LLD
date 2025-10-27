# Project Debug Rules (Non-Obvious Only)

- **Console output encoding issues** - Emoji characters cause compilation errors on Windows (use plain text instead)
- **Observer pattern debugging** - ConsoleNotificationObserver.getFloorNumber() always returns 0 (hardcoded placeholder)
- **Fee calculation edge case** - Duration of 0 hours gets converted to 1 hour minimum charge (not visible in logs)
- **Ticket ID format** - Generated as "TICKET-{8-char-UUID}" not sequential numbers (affects debugging searches)
- **Thread.sleep(1000)** in Main.java simulates time passage for fee calculation testing
- **No test framework** - Application must be run manually to verify functionality