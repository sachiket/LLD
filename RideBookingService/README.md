# 🚖 Ride Booking System (Uber/Ola LLD)

This project simulates a simplified ride-booking platform similar to Uber or Ola.  
It demonstrates object-oriented design principles, modular architecture, and the application of common design patterns.

---

## 📌 Problem Statement

Design a ride-booking system where:
- Riders can request rides.
- Drivers can be matched to rides.
- Prices are calculated dynamically using different strategies.
- The system supports scalability and clean code practices.

---

## 🧠 Design Approach

1. **Understand the Actors and Flows**
    - Actors: `Rider`, `Driver`, `System`
    - Flows: Request → Match → Accept → Price → Start → End

2. **Break into Components**
    - Models: Entities like `Ride`, `Driver`, `Rider`, `Location`
    - Managers: Handle coordination (`RideManager`)
    - Strategies: Behavior flexibility (matching, pricing)

3. **Use Design Patterns**
    - Strategy Pattern → `PricingStrategy`, `MatchingStrategy`
    - Singleton Pattern → `RideManager` (shared instance)
    - (Optional) Decorator, Observer → for future extensibility

4. **Build from Bottom Up**
    - Start with simple POJOs (`Location`, `Rider`, `Driver`)
    - Add `Ride` and its states
    - Add `RideManager` to control logic
    - Plug in strategy implementations
    - Test via a `Main` entry point

---

## 📦 Folder Structure

```plaintext
src/
├── models/          # Core data models: Rider, Driver, Ride, Location
├── managers/        # RideManager (singleton)
├── strategies/
│   ├── pricing/     # PricingStrategy, NormalPricing, SurgePricing
│   └── matching/    # RideMatchingStrategy, NearestDriverStrategy
├── enums/           # RideStatus enum
├── utils/           # Helpers like distance calculations
├── Main.java        # Entry point to simulate flow
```

## 📚 Class Diagram (UML Style)

```sql
+---------------------+           +--------------------+            +------------------+
|       Rider         |           |       Driver       |            |     Location     |
+---------------------+           +--------------------+            +------------------+
| - id: String        |           | - id: String       |            | - lat: double    |
| - name: String      |           | - name: String     |            | - lon: double    |
+---------------------+           | - currentLocation  |<>----------|                  |
| + requestRide(...)  |           | - isAvailable: bool|            | +distanceTo(...) |
+----------+----------+           +--------------------+            +------------------+
           |                                     
           |   uses
           v
+-------------------------+
|         Ride            |
+-------------------------+
| - id: String            |
| - rider: Rider          |
| - driver: Driver        |
| - from: Location        |
| - to: Location          |
| - status: RideStatus    |
| - price: double         |
+-------------------------+
| +getId(), +getPrice()   |
+-------------------------+

      ▲
      | composed by
      |
+-------------------------+                      +-----------------------------+
|      RideManager        |◄---------singleton--►|     DriverManager (opt)     |
+-------------------------+                      +-----------------------------+
| - allDrivers: List<Driver>                     | - allDrivers                |
| - pricingStrategy: PricingStrategy             |                             |
| - matchingStrategy: RideMatchingStrategy       |                             |
+-------------------------+                      +-----------------------------+
| +registerDriver()       |
| +createRide()           |
+-------------------------+

                     ▲      ▲
                     |      |
+--------------------+      +--------------------------+
|  PricingStrategy   |      |  RideMatchingStrategy    |
+--------------------+      +--------------------------+
| +calculatePrice()  |      | +findDriver()            |
+--------------------+      +--------------------------+
     ▲       ▲                        ▲
     |       |                        |
+---------+ +------------+    +---------------------+
| Normal  | |  Surge     |    | NearestDriverStrategy|
| Pricing | |  Pricing   |    +---------------------+
+---------+ +------------+



```

## ✅ Sample Flow: Requesting a Ride

1. Rider requests a ride.
2. `RideManager` uses `RideMatchingStrategy` to find a driver.
3. Driver accepts the ride.
4. `PricingStrategy` calculates the price.
5. Ride is confirmed and returned.

---

## 💡 Extensibility Ideas

- Add **ride history** per rider
- Add **driver ratings**
- Use **Observer** pattern to notify driver on ride request
- Use **Decorator** pattern for applying promo codes or discounts
- Switch to **real-time location tracking** via event polling

---

## 🗣️ Interview Pitch (When Asked "How Did You Design This?")

> “I began by identifying the core entities and user flows. I separated concerns by modeling entities as simple classes, and logic in managers. I used the Strategy pattern for pricing and matching, and Singleton for central coordination. The design allows extension like surge pricing, new matching algorithms, or decorators for discounts without changing core code.”

---

## ✅ Technologies Used

- Java 8+
- OOP, SOLID principles
- Strategy and Singleton Patterns

---