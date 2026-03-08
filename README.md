# Anti-Fraud System (AFS)

A microservices-based anti-fraud system designed for real-time transaction monitoring, suspicious activity detection, and user limit management.

##  System Architecture

The project consists of three core microservices communicating asynchronously via Apache Kafka:

1.  **User Service (Port 8082)**:
    * Manages user profiles, including balance, status (`ACTIVE`, `BLOCKED`), and risk levels (`LOW`, `MEDIUM`, `HIGH`).
    * Synchronizes a "blacklist" of blocked users to Redis for fast cross-service verification.
2.  **Transaction Service (Port 8083)**:
    * Processes incoming transaction requests and validates them against daily limits and rate-limiting rules using Redis.
    * Instantly approves transactions for `LOW` risk users and routes others for manual verification via Kafka.
3.  **Notification Service (Port 8085)**:
    * Consumes events from Kafka and sends confirmation emails for suspicious transactions using MailHog.
    * Provides an API to approve or reject transactions through unique security tokens stored in Redis.

##  Technology Stack

* **Java 22** & **Spring Boot 4.0.2** (Data JPA, Web, Validation, Mail).
* **Apache Kafka**: Distributed messaging system for inter-service communication.
* **Redis**: Distributed cache for blacklists, rate limiting, and temporary tokens.
* **PostgreSQL**: Relational database for persistent storage of users and transactions.
* **Docker & Docker Compose**: Containerization for Kafka, Postgres, Redis, and MailHog.
* **Thymeleaf**: Template engine for generating HTML confirmation emails.

##  Getting Started

### 1. Prerequisites
Ensure you have the following installed:
* Docker & Docker Compose
* JDK 22
* Maven

### 2. Infrastructure Setup
Navigate to the `docker/` directory and start the required services:
```bash
docker-compose up -d
```

* PostgreSQL (Port 5433)
* Redis (Port 6379)
* Kafka (Port 9092)
* MailHog (Port 8025 for Web UI, 1025 for SMTP)

### 3. Running the Services
Run each microservice using Maven:
```bash
mvn spring-boot:run
```

##  Workflow

1.  **Initiation**: Create a transaction via `POST /api/transactions` in the **transaction-service**.
2.  **Validation**:
    * If the daily limit is exceeded, the system returns `Daily limit exhausted`.
    * If a user exceeds the allowed requests per minute, they are automatically blocked via **Redis** rate limiting.
3.  **Verification**:
    * `LOW` risk level transactions are approved automatically.
    * Other risk levels trigger a confirmation email in **MailHog** ([http://localhost:8025](http://localhost:8025)).
4.  **Finalization**: Click the link in the email to trigger a `GET` request to **notification-service**. This sends a result back to **transaction-service** to update the database status and user balance.

##  API Endpoints

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `localhost:8082/api/users` | **POST** | Create a new user |
| `localhost:8083/api/transactions` | **POST** | Initiate a transaction |
| `localhost:8085/api/verify/approve?token=...` | **GET** | Approve transaction via email token |

