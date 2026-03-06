# Evently - Event Management System API

Evently is a event management web application. It aims to provide a platform for event organisers to host events and attendees to register for those events.

---

## Technologies Used
- Java (17+)
- Spring Boot (4.0.3)
- Spring Data JPA
- PostgreSQL (for production)
- H2 Database (for testing)
- Gradle

---

## How to Run the Project
### Prerequisites
Make sure you have these installed before starting:
- Java
- PostgreSQL
- pgAdmin

### 1. Clone the Repository

```bash
git clone https://github.com/shantanuk7/evently-api.git
cd evently-api
```

### 2. Database Setup

#### Step 1: Install PostgreSQL
Download and install PostgreSQL from (https://www.postgresql.org/download/)

#### Step 2: Create Database using pgAdmin
1. Open pgAdmin
2. Connect to your PostgreSQL server
3. Create database name: evently-db

#### Step 3: Configure application.properties
- Main
```env
spring.config.import=optional:secrets.properties
spring.application.name=event-management-system
spring.datasource.url=jdbc:postgresql://localhost:5432/evently-db
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true
management.endpoints.web.exposure.include=health
spring.security.user.name=user
spring.security.user.password=password
```
- secrets.properties
```env
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

- Test
```
spring.application.name=evently-db
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```
### 3. Start the Server
```bash
./gradlew run
```

### 4. The server will start on:
```
http://localhost:8080
```

### 5. Check Health endpoint:
```
http://localhost:8080/actuator/health
```