# Backend for Event Ticketing System

## Overview
This backend is a Spring Boot application that serves as the core of the real-time event ticketing system. It implements the producer-consumer pattern for ticket management and exposes REST APIs for communication with the Angular-based frontend.

## Features
- Multithreaded ticket management using the producer-consumer pattern.
- Support for multiple vendors and customers, including VIP customers.
- REST APIs for managing tickets, configuration, and system logs.
- Integration with the frontend for real-time updates on ticket status and control panel operations.

## How to Run
1. Navigate to the `OOP coursework` folder, then to the `CourseworkBackend` subfolder.
2. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
