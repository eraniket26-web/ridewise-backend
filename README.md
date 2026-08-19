# RideWise — Console-Based Ride-Sharing System

RideWise is a simplified, console-based ride-sharing system inspired by applications such as Uber/Ola.

The project is designed primarily as an **LLD and OOP exercise**, focusing on clean object-oriented design, separation of responsibilities, extensibility, and the practical application of SOLID principles and design patterns.

The goal is **not** to build a production-ready ride-sharing application. The goal is to demonstrate how a small system can be designed so that changing business rules does not require rewriting the core logic.

---

## Features

RideWise supports the following core use cases:

* Register Riders
* Register Drivers
* Show available Drivers
* Request a Ride
* Match a Driver using a configurable strategy
* Calculate fare using a configurable pricing strategy
* Track Ride lifecycle
* Complete a Ride
* Cancel a Ride

### Ride Lifecycle

```text
REQUESTED
    │
    ├──────────────► CANCELLED
    │
    ▼
ASSIGNED
    │
    ├──────────────► CANCELLED
    │
    ▼
COMPLETED
```

Invalid transitions such as `COMPLETED → CANCELLED` are not allowed.

---

## Design Goals

The project demonstrates:

* Object-Oriented Programming
* SOLID principles
* Composition over inheritance
* Strategy Pattern
* Programming to interfaces
* Low coupling
* High cohesion
* Law of Demeter
* DRY
* KISS
* YAGNI
* Separation of domain entities and business services

---

## High-Level Architecture

```text
                    ┌─────────────────┐
                    │      Rider      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  RiderService   │
                    └─────────────────┘


                    ┌─────────────────┐
                    │     Driver      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ DriverService   │
                    └────────┬────────┘
                             │
                             │ available drivers
                             ▼
                    ┌─────────────────┐
                    │  RideService    │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
    RideMatchingStrategy              FareStrategy
              │                             │
       ┌──────┴──────┐              ┌───────┴────────┐
       ▼             ▼              ▼                ▼
    Nearest       Least          Default           Peak
    Driver        Active          Fare              Hour
    Strategy      Strategy        Strategy          Strategy
```

---

## Domain Model

### Rider

Represents a customer using the platform.

Responsibilities:

* Store rider information
* Maintain current location

```text
Rider
├── id
├── name
├── contactNo
└── location
```

---

### Driver

Represents a driver registered on the platform.

Responsibilities:

* Store driver information
* Maintain current location
* Maintain availability

```text
Driver
├── id
├── name
├── currentLocation
├── vehicleType
└── available
```

---

### Ride

Represents a single trip and its lifecycle.

```text
Ride
├── id
├── rider
├── driver
├── distance
├── vehicleType
└── status
```

A Ride has associations with a Rider and Driver. Both exist independently of the Ride.

---

### Location

`Location` is modeled as a **Value Object**.

```text
Location
├── latitude
└── longitude
```

It represents geographic information rather than an independently identifiable domain entity.

---

### FareReceipt

Represents the fare generated for a completed/requested trip.

```text
FareReceipt
├── rideId
├── amount
└── generatedAt
```

---

## Enums

### RideStatus

```text
REQUESTED
ASSIGNED
COMPLETED
CANCELLED
```

### VehicleType

```text
BIKE
AUTO
CAR
```

---

# Services

## RiderService

Responsible for Rider-related operations and in-memory Rider storage.

Responsibilities include:

* Register Rider
* Retrieve Rider when required by the application

The service owns the collection of Riders rather than the Rider entity itself.

```text
RiderService
    └── List<Rider>
```

---

## DriverService

Responsible for Driver-related operations and Driver storage.

Responsibilities include:

* Register Driver
* Retrieve Driver
* Find available Drivers
* Manage Driver availability

```text
DriverService
    └── List<Driver>
```

DriverService finds the **candidate Drivers**.

It does not decide which Driver should be selected.

---

## RideService

Responsible for orchestrating the Ride lifecycle.

Responsibilities include:

* Create Ride
* Request Ride
* Find available Drivers through DriverService
* Select Driver through RideMatchingStrategy
* Assign Driver
* Calculate fare through FareStrategy
* Complete Ride
* Cancel Ride
* Maintain valid Ride lifecycle transitions

```text
RideService
    └── List<Ride>
```

RideService acts as the orchestrator rather than implementing matching or pricing algorithms itself.

---

# Strategy Pattern

The system uses the Strategy Pattern to make frequently changing business algorithms replaceable.

## Driver Matching

```text
                 RideMatchingStrategy
                         ▲
                         │
          ┌──────────────┴──────────────┐
          │                             │
          ▼                             ▼
NearestDriverStrategy       LeastActiveDriverStrategy
```

### NearestDriverStrategy

Selects the available Driver closest to the Rider's location.

### LeastActiveDriverStrategy

Selects the eligible Driver with the lowest number of completed rides.

New matching strategies can be added without modifying the core RideService workflow.

For example:

```text
HighestRatedDriverStrategy
```

could be added later by implementing `RideMatchingStrategy`.

---

# Fare Strategy

```text
                    FareStrategy
                         ▲
                         │
             ┌───────────┴───────────┐
             │                       │
             ▼                       ▼
    DefaultFareStrategy      PeakHourFareStrategy
```

### DefaultFareStrategy

Calculates fare using the normal pricing rules.

### PeakHourFareStrategy

Calculates fare using peak-hour pricing rules.

Additional pricing strategies can be added without changing RideService.

---

# Ride Booking Flow

The main happy-path flow is:

```text
Rider
  │
  │ request ride
  ▼
RideService
  │
  │ create Ride
  ▼
Ride
  │
  │ REQUESTED
  ▼
DriverService
  │
  │ find available drivers
  ▼
List<Driver>
  │
  ▼
RideMatchingStrategy
  │
  │ select driver
  ▼
Driver
  │
  ▼
Ride
  │
  │ ASSIGNED
  ▼
FareStrategy
  │
  │ calculate fare
  ▼
FareReceipt
```

The Ride can subsequently transition to:

```text
ASSIGNED
    │
    ▼
COMPLETED
```

or:

```text
REQUESTED / ASSIGNED
          │
          ▼
      CANCELLED
```

---

# Class Relationship Overview

```text
Rider ──────────────── Ride ─────────────── Driver
  │                     │                      │
  │                     │                      │
  ▼                     ▼                      ▼
Location           VehicleType             Location


Ride ─────────────── FareReceipt


RiderService ─────── manages ───────► Rider

DriverService ────── manages ───────► Driver

RideService ──────── manages ───────► Ride

RideService ──────── uses ──────────► DriverService

RideService ──────── uses ──────────► RideMatchingStrategy

RideService ──────── uses ──────────► FareStrategy
```

---

# SOLID Principles

## Single Responsibility Principle — SRP

Each major component has a focused responsibility:

```text
RiderService
    → Rider management

DriverService
    → Driver management

RideService
    → Ride lifecycle orchestration

RideMatchingStrategy
    → Driver selection algorithm

FareStrategy
    → Fare calculation algorithm
```

---

## Open/Closed Principle — OCP

The system should be open for extension but closed for modification.

For example, adding:

```text
HighestRatedDriverStrategy
```

should require a new Strategy implementation rather than modifying the RideService matching logic.

Similarly, a new fare algorithm can be introduced by adding another `FareStrategy` implementation.

---

## Liskov Substitution Principle — LSP

Every implementation of:

```text
RideMatchingStrategy
```

must be usable wherever a `RideMatchingStrategy` is expected.

Likewise, every `FareStrategy` implementation must remain interchangeable.

---

## Interface Segregation Principle — ISP

The project uses small, focused interfaces:

```text
RideMatchingStrategy
FareStrategy
```

rather than creating one large interface containing unrelated operations.

---

## Dependency Inversion Principle — DIP

`RideService` should depend on abstractions:

```text
RideMatchingStrategy
FareStrategy
```

rather than directly depending on:

```text
NearestDriverStrategy
DefaultFareStrategy
```

This allows the concrete strategy to be selected or changed independently.

---

# Other Design Principles

## Composition over Inheritance

The system does not require an inheritance hierarchy for different matching or fare behaviors.

Instead:

```text
RideService
    ├── has/use → RideMatchingStrategy
    └── has/use → FareStrategy
```

Behavior is composed through interfaces.

---

## DRY

Driver-selection logic is not duplicated inside RideService.

Instead:

```text
RideService
      ↓
RideMatchingStrategy
      ↓
specific matching algorithm
```

---

## KISS

The project intentionally uses simple domain objects and in-memory collections rather than introducing unnecessary infrastructure.

---

## YAGNI

The project avoids features that are not required by the MVP.

For example:

* No database
* No payment gateway
* No authentication
* No notification system
* No real GPS service
* No unnecessary repository abstraction
* No unnecessary `DataStore<T>` abstraction

The goal is to introduce abstractions only when they solve an actual design problem.

---

# In-Memory Storage

Since this is a console-based MVP, entities are stored in memory.

```text
RiderService
    └── List<Rider>

DriverService
    └── List<Driver>

RideService
    └── List<Ride>
```

This intentionally avoids introducing a generic `DataStore<?>` or repository layer until there is an actual requirement for persistence.

Each service remains the single source of truth for the objects it manages.

---

# Suggested Package Structure

```text
src/
└── main/
    └── java/
        ├── model/
        │   ├── Rider
        │   ├── Driver
        │   ├── Ride
        │   ├── Location
        │   ├── FareReceipt
        │   ├── RideStatus
        │   └── VehicleType
        │
        ├── service/
        │   ├── RiderService
        │   ├── DriverService
        │   └── RideService
        │
        ├── strategy/
        │   ├── RideMatchingStrategy
        │   ├── NearestDriverStrategy
        │   ├── LeastActiveDriverStrategy
        │   ├── FareStrategy
        │   ├── DefaultFareStrategy
        │   └── PeakHourFareStrategy
        │
        ├── util/
        │   └── ID generation utilities
        │
        └── Main
```

---

# Development Approach

The project is intentionally developed incrementally:

```text
1. Identify domain nouns
        ↓
2. Identify responsibilities
        ↓
3. Identify relationships
        ↓
4. Decide HAS-A vs IS-A
        ↓
5. Decide association / aggregation / composition
        ↓
6. Check ownership and lifecycle
        ↓
7. Identify what can change
        ↓
8. Apply SOLID principles
        ↓
9. Apply Strategy Pattern
        ↓
10. Draw class diagram
        ↓
11. Implement Java classes
        ↓
12. Test individual use cases
```

---

# Future Extensions

The architecture is intentionally designed so that additional behavior can be introduced without changing the core RideService workflow.

Potential future strategies:

```text
Ride Matching
├── NearestDriverStrategy
├── LeastActiveDriverStrategy
└── HighestRatedDriverStrategy

Fare
├── DefaultFareStrategy
├── PeakHourFareStrategy
└── DiscountFareStrategy
```

Possible future features:

* Driver ratings
* Ride history
* Multiple vehicle categories
* Cancellation fees
* Discounts
* Scheduled rides
* Persistent storage
* Payment processing
* Notifications

These are intentionally outside the current MVP.

---

## Purpose of the Project

RideWise is primarily a **learning and LLD practice project**.

The important outcome is not the number of features, but the ability to explain:

* Why each class exists
* Why each responsibility belongs where it does
* Why interfaces are used
* Why Strategy Pattern is appropriate
* How new matching/pricing algorithms can be added
* How coupling is minimized
* How SOLID principles influence the design

The project should remain simple enough to understand while providing clear extension points for future requirements.
