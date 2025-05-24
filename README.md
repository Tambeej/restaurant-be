# RestAuRant - Backend

This is the backend of the RestAuRant app, built with Spring Boot and MySQL.

## Features
- User management (OAuth2 Google Sign-In)
- Role-based authorization: Client, Waiter, Kitchen, Manager
- Restaurant table and dish management
- Order tracking with real-time status (Ordered → Cooking → Ready)
- Waiter call system (Bill, Water, Help, etc.)

## Technologies
- Java 17
- Spring Boot 3
- MySQL
- JPA/Hibernate
- JWT Security

## Setup
1. Clone the repo:  
   `git clone https://github.com/Tambeej/restaurant-be.git`
2. Configure MySQL DB in `application.yml`
3. Run the project:  
   `./gradlew bootRun`

## Author
Developed as part of my Computer Science degree at the Open University of Israel.
Enter file contents here
