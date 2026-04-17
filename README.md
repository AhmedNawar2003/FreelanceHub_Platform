<div align="center">

[![Typing SVG](https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=22&pause=1000&color=6C63FF&center=true&vCenter=true&width=1000&lines=🚀+FreelanceHub+Platform;Production-Grade+Microservices+%7C+Java+21+%7C+Spring+Boot+3;Event-Driven+Architecture+%7C+Kafka+%7C+WebSockets;Secure+%7C+JWT+%7C+OAuth2+%7C+RBAC;Cloud-Native+%7C+Docker+%7C+Redis+%7C+Zipkin;Saga+Pattern+%7C+Circuit+Breaker+%7C+Rate+Limiting)](https://git.io/typing-svg)

<br/>

# 🚀 FreelanceHub — Freelancing Microservices Platform

### A Production-Grade Distributed Backend Built with Java 21 & Spring Boot 3

**FreelanceHub** is a fully distributed, event-driven microservices platform that simulates real-world freelancing ecosystems like Upwork — built from scratch with enterprise-grade patterns.

<br/>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-Google-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Zipkin](https://img.shields.io/badge/Zipkin-Tracing-FE7139?style=for-the-badge)
![Resilience4j](https://img.shields.io/badge/Resilience4j-Circuit%20Breaker-6DB33F?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Status](https://img.shields.io/badge/Status-Production%20Ready-00C851?style=for-the-badge)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [System Architecture](#️-system-architecture)
- [Services Breakdown](#-services-breakdown)
- [Security Model](#-security-model)
- [Event-Driven & Saga Pattern](#-event-driven--saga-pattern)
- [Tech Stack](#️-tech-stack)
- [Advanced Features](#-advanced-features)
- [API Documentation](#-api-documentation)
- [Observability & Monitoring](#-observability--monitoring)
- [Getting Started](#-getting-started)
- [Future Enhancements](#-future-enhancements)
- [Author](#-author)

---

## 🔍 Overview

FreelanceHub is a **fully distributed, event-driven microservices platform** built to simulate production-level freelancing systems. Every service is independently deployable, fault-tolerant, and communicates through well-defined APIs and Kafka events.

| Capability | Implementation |
|---|---|
| 🔀 **API Routing & Security** | Spring Cloud Gateway + JWT Filter |
| 🔍 **Service Discovery** | Netflix Eureka |
| ⚙️ **Centralized Config** | Spring Cloud Config Server (native) |
| 🔐 **Authentication** | JWT + Google OAuth2 |
| 📨 **Async Messaging** | Apache Kafka |
| 🔗 **Inter-service Calls** | OpenFeign |
| 💾 **Caching** | Redis (per-service cache) |
| 🛡️ **Resilience** | Resilience4j Circuit Breaker + Retry |
| ⏱️ **Rate Limiting** | Redis-based via Spring Cloud Gateway |
| 🔄 **Distributed Transactions** | Saga Pattern (Choreography) |
| 📊 **Distributed Tracing** | Zipkin + Micrometer |
| 📡 **Real-time Notifications** | WebSocket (STOMP) + Kafka Consumer |
| 📝 **API Docs** | SpringDoc OpenAPI 3 / Swagger UI |
| 🐳 **Containerization** | Docker + Docker Compose |

---

## 🏛️ System Architecture

<p align="center">
  <img src="/Freelancing Microservices Platform.png" width="100%" alt="SmartEvent System Architecture"/>
</p>

```text
                         ┌───────────────────────┐
                         │      Client Apps       │
                         └──────────┬────────────┘
                                    │
                    ┌───────────────▼───────────────┐
                    │          API Gateway           │
                    │  JWT Filter │ Rate Limiting    │
                    │  Circuit Breaker │ Routing      │
                    └───────────────┬───────────────┘
                                    │
        ┌──────────────────┬────────┴────────┬──────────────────┐
        │                  │                 │                  │
┌───────▼──────┐  ┌────────▼──────┐  ┌──────▼──────┐  ┌───────▼──────┐
│ User Service │  │Project Service│  │  Contract   │  │   Payment    │
│  JWT + OAuth │  │  Bid System   │  │  Milestones │  │    Escrow    │
│  Redis Cache │  │  Redis Cache  │  │    Saga     │  │  Saga Consmr │
└──────────────┘  └───────────────┘  └──────┬──────┘  └──────────────┘
                                             │
                              ┌──────────────▼──────────────┐
                              │        Apache Kafka          │
                              │  contract-created            │
                              │  payment-funded              │
                              │  saga-rollback               │
                              │  freelancehub-notifications  │
                              └──────────────┬──────────────┘
                                             │
                    ┌────────────────────────▼────────────────────┐
                    │            Notification Service              │
                    │       Kafka Consumer + WebSocket STOMP       │
                    └─────────────────────────────────────────────┘

        ┌─────────────────┐    ┌──────────────┐    ┌──────────────┐
        │  Config Server  │    │    Eureka     │    │    Zipkin    │
        │     :8888       │    │    :8761      │    │    :9411     │
        └─────────────────┘    └──────────────┘    └──────────────┘

        ┌─────────────────┐    ┌──────────────┐
        │   PostgreSQL    │    │    Redis      │
        │  (per-service)  │    │    :6379      │
        └─────────────────┘    └──────────────┘
```

---

## 🧩 Services Breakdown

### 🔧 Infrastructure Services

| Service | Port | Description |
|---|---|---|
| **API Gateway** | `8080` | Routing, JWT validation, Rate Limiting, Circuit Breaker |
| **Eureka Server** | `8761` | Service discovery & registration |
| **Config Server** | `8888` | Centralized configuration (native profile) |
| **Zipkin** | `9411` | Distributed tracing & performance monitoring |
| **Redis** | `6379` | Caching + Rate Limiting store |
| **Kafka** | `9092` | Async event streaming |
| **PostgreSQL** | `5432` | Relational database (one DB per service) |

---

### 💼 Business Services

| Service | Port | Key Responsibilities |
|---|---|---|
| **User Service** | `8081` | Registration, Login (JWT + OAuth2), Profiles, Role management, Redis cache |
| **Project Service** | `8082` | Project CRUD, Bidding system, Status workflow, Redis cache |
| **Contract Service** | `8083` | Contract creation, Milestone management, Saga Orchestrator |
| **Payment Service** | `8084` | Escrow creation, Milestone releases, Refunds, Saga Consumer |
| **Review Service** | `8085` | Ratings, Reviews, Stats aggregation, Redis cache |
| **Notification Service** | `8086` | Kafka consumer, WebSocket STOMP push, Notification persistence |

---

## 🔐 Security Model

### Authentication Flow

```text
User Login ──► API Gateway ──► User Service ──► JWT Token
                                                    │
                         ┌──────────────────────────▼──────────────────────────┐
                         │              JWT Contains                             │
                         │  userId · email · role · issuedAt · expiration       │
                         └─────────────────────────────────────────────────────┘
                                                    │
All Private Requests ──► API Gateway JWT Filter ──► Validated ──► Service
```

### Role-Based Access Control

| Role | Permissions |
|---|---|
| `CLIENT` | Post projects, accept bids, create contracts, fund escrow, release payments |
| `FREELANCER` | Browse projects, place bids, submit milestones, receive payments |
| `ADMIN` | Full platform control |

### Security Features

- ✅ Stateless JWT (no sessions)
- ✅ Google OAuth2 login with custom success handler
- ✅ JWT validation at Gateway level (before reaching services)
- ✅ `userId`, `email`, `role` forwarded via request headers to downstream services
- ✅ Password validation (uppercase + lowercase + digits)
- ✅ Global Exception Handler with structured error responses
- ✅ Input validation via Jakarta Validation on all DTOs

---

## 📨 Event-Driven & Saga Pattern

### Kafka Topics

| Topic | Producer | Consumer | Purpose |
|---|---|---|---|
| `contract-created` | Contract Service | Payment Service | Trigger auto escrow funding |
| `payment-funded` | Payment Service | Contract Service | Confirm saga success |
| `saga-rollback` | Payment Service | Contract Service | Rollback contract on failure |
| `freelancehub-notifications` | All Services | Notification Service | Push real-time notifications |

### Saga Flow (Choreography)

```text
1. Client creates Contract
         │
         ▼
2. ContractService saves & publishes [contract-created] ──► Kafka
         │
         ▼
3. PaymentService consumes event ──► auto-funds Escrow
         │
    ┌────┴────┐
    │         │
 Success    Failure
    │         │
    ▼         ▼
[payment-  [saga-
 funded]   rollback]
    │         │
    ▼         ▼
Saga      Contract
Complete  CANCELLED
    │
    ▼
Notification sent to Client & Freelancer via WebSocket
```

### WebSocket Real-time Notifications

```text
Frontend connects ──► ws://localhost:8086/ws (SockJS + STOMP)
                              │
Subscribe to ──► /user/{email}/queue/notifications
                              │
Notification arrives via Kafka ──► Saved to DB ──► Pushed via WebSocket
```

---

## 🛠️ Tech Stack

### Core

| Technology | Version | Usage |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 3.3.x | Framework |
| Spring Cloud | 2023.0.x | Microservices infrastructure |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | ORM & database access |
| Hibernate | 6.x | JPA implementation |

### Infrastructure

| Technology | Usage |
|---|---|
| Apache Kafka | Async messaging & Saga events |
| PostgreSQL 15 | Relational data (per-service DB) |
| Redis 7 | Caching + Rate limiting |
| Docker | Containerization |
| Docker Compose | Multi-service orchestration |

### Security & Auth

| Technology | Usage |
|---|---|
| JWT (JJWT 0.11.5) | Stateless authentication |
| Google OAuth2 | Social login |
| BCrypt | Password hashing |

### Observability

| Technology | Usage |
|---|---|
| Zipkin | Distributed tracing |
| Micrometer Brave | Trace instrumentation |
| SpringDoc OpenAPI 3 | API documentation |
| Swagger UI | Interactive API explorer |
| Spring Actuator | Health & metrics endpoints |

### Resilience

| Technology | Usage |
|---|---|
| Resilience4j | Circuit Breaker per service |
| Spring Cloud Gateway | Rate Limiting (Redis) |
| Saga Pattern | Distributed transaction management |

---

## ⚡ Advanced Features

### 🔴 Redis Caching

| Service | Cache Name | TTL | Cached Data |
|---|---|---|---|
| User Service | `users` | 15 min | User profiles by ID |
| Project Service | `projects` | 5 min | Project details |
| Project Service | `open-projects` | 2 min | All open projects list |
| Review Service | `reviews` | 10 min | Reviews by user/contract |
| Review Service | `review-stats` | 15 min | Rating aggregations |

Cache is automatically evicted on write operations (`@CacheEvict`) and updated on reads (`@Cacheable`).

### 🛡️ Circuit Breaker (Resilience4j)

Every service has a circuit breaker configured:

```yaml
slidingWindowSize: 10
failureRateThreshold: 50%
waitDurationInOpenState: 5s
permittedCallsInHalfOpenState: 3
```

The API Gateway uses **Reactive Resilience4j** with fallback endpoints returning graceful `503 Service Unavailable` responses.

### ⏱️ Rate Limiting (Redis-based)

| Route Type | Requests/sec | Burst |
|---|---|---|
| Public (auth endpoints) | 20 | 40 |
| Private (authenticated) | 50 | 100 |

Rate limiting is applied per **IP** for public routes and per **JWT token** for private routes.

### 🔄 Saga Pattern (Choreography)

Ensures data consistency across Contract + Payment services without distributed locks. On failure, compensation transactions (rollback) are automatically triggered via Kafka.

---

## 📝 API Documentation

Swagger UI is available via API Gateway aggregating all services:

```
http://localhost:8080/swagger-ui/index.html
```

Or access each service directly:

| Service | Swagger URL |
|---|---|
| User Service | `http://localhost:8081/swagger-ui/index.html` |
| Project Service | `http://localhost:8082/swagger-ui/index.html` |
| Contract Service | `http://localhost:8083/swagger-ui/index.html` |
| Payment Service | `http://localhost:8084/swagger-ui/index.html` |
| Review Service | `http://localhost:8085/swagger-ui/index.html` |
| Notification Service | `http://localhost:8086/swagger-ui/index.html` |

### Key Endpoints

<details>
<summary><b>🔐 Authentication</b></summary>

```
POST /api/users/auth/register     Register new user (CLIENT or FREELANCER)
POST /api/users/auth/login        Login and receive JWT token
GET  /oauth2/authorization/google Google OAuth2 login
```
</details>

<details>
<summary><b>👤 User Profiles</b></summary>

```
GET  /api/users/me                Get authenticated user's profile
PUT  /api/users/me                Update profile (bio, skills, portfolio)
GET  /api/users/{id}              Get user public profile by ID
```
</details>

<details>
<summary><b>📁 Projects & Bidding</b></summary>

```
POST /api/projects                Create a project (CLIENT only)
GET  /api/projects                List all open projects
GET  /api/projects/{id}           Get project details with bids
GET  /api/projects/my             Get my projects
PUT  /api/projects/{id}           Update project (owner only)
DELETE /api/projects/{id}         Delete project (owner only)
POST /api/projects/{id}/bids      Place a bid (FREELANCER only)
PUT  /api/projects/{id}/bids/{bidId}/accept   Accept a bid (CLIENT only)
```
</details>

<details>
<summary><b>📄 Contracts & Milestones</b></summary>

```
POST /api/contracts                       Create contract (triggers Saga)
GET  /api/contracts/{id}                  Get contract details
GET  /api/contracts/project/{projectId}   Get contract by project
GET  /api/contracts/my                    Get my contracts
PUT  /api/contracts/{id}/milestones/{mid}/submit    Submit milestone (FREELANCER)
PUT  /api/contracts/{id}/milestones/{mid}/approve   Approve milestone (CLIENT)
PUT  /api/contracts/{id}/milestones/{mid}/reject    Reject milestone (CLIENT)
```
</details>

<details>
<summary><b>💳 Payments & Escrow</b></summary>

```
POST /api/payments/escrow                         Create & fund escrow
GET  /api/payments/escrow/{id}                    Get escrow details
GET  /api/payments/escrow/contract/{contractId}   Get escrow by contract
GET  /api/payments/my                             Get my payments
PUT  /api/payments/escrow/{id}/release-milestone  Release partial payment
PUT  /api/payments/escrow/{id}/release-full       Release full payment
PUT  /api/payments/escrow/{id}/refund             Refund escrow to client
```
</details>

<details>
<summary><b>⭐ Reviews & Ratings</b></summary>

```
POST /api/reviews                 Submit a review
GET  /api/reviews/user/{userId}   Get reviews received by user
GET  /api/reviews/contract/{id}   Get reviews for a contract
GET  /api/reviews/my              Get my submitted reviews
GET  /api/reviews/stats/{userId}  Get rating stats (avg + breakdown)
```
</details>

<details>
<summary><b>🔔 Notifications</b></summary>

```
GET  /api/notifications           Get all my notifications
GET  /api/notifications/unread    Get unread notifications only
GET  /api/notifications/count     Get unread count
PUT  /api/notifications/{id}/read      Mark notification as read
PUT  /api/notifications/read-all       Mark all as read
WS   ws://localhost:8086/ws            WebSocket connection (STOMP)
SUB  /user/{email}/queue/notifications Subscribe to real-time notifications
```
</details>

---

## 📊 Observability & Monitoring

### Zipkin Distributed Tracing

```
http://localhost:9411
```

Every request is traced across services with `traceId` and `spanId` in logs:

```
INFO [user-service,7f3a2b1c4d5e6f7a,2b1c4d5e] Request completed in 45ms
```

### Eureka Dashboard

```
http://localhost:8761
```

View all registered service instances with health status.

### Actuator Health Endpoints

Each service exposes:
```
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose

### Option 1: Run with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/AhmedNawar2003/freelancehub.git
cd freelancehub

# Build all services
chmod +x build-all.sh
./build-all.sh

# Start everything
docker-compose up -d

# Check status
docker-compose ps
```

### Option 2: Run Locally

```bash
# 1. Start infrastructure
docker-compose up -d postgres redis zookeeper kafka zipkin

# 2. Start services in order
# Config Server → Eureka → API Gateway → Business Services

# Config Server
cd config-server && mvn spring-boot:run

# Eureka Server
cd eureka-server && mvn spring-boot:run

# API Gateway
cd api-gateway && mvn spring-boot:run

# Business Services (any order)
cd user-service && mvn spring-boot:run
cd project-service && mvn spring-boot:run
cd contract-service && mvn spring-boot:run
cd payment-service && mvn spring-boot:run
cd review-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### Service URLs

| Service | URL |
|---|---|
| API Gateway | `http://localhost:8080` |
| Swagger UI (Aggregated) | `http://localhost:8080/swagger-ui/index.html` |
| Eureka Dashboard | `http://localhost:8761` |
| Zipkin Tracing | `http://localhost:9411` |
| Config Server | `http://localhost:8888` |

### Quick Test

```bash
# Register a client
curl -X POST http://localhost:8080/api/users/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ahmed Nawar",
    "email": "ahmed@test.com",
    "password": "Password123",
    "role": "CLIENT"
  }'

# Login and get token
curl -X POST http://localhost:8080/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "ahmed@test.com", "password": "Password123"}'

# Create a project (use token from above)
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer {YOUR_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Build REST API for E-commerce",
    "description": "Looking for a backend developer to build a scalable REST API using Spring Boot with full CRUD operations",
    "budget": 500,
    "skills": "Java, Spring Boot, PostgreSQL"
  }'
```

---

## 🗂️ Project Structure

```
freelancehub/
├── config-server/           # Centralized config
│   └── src/main/resources/
│       └── configurations/  # Per-service yml files
├── eureka-server/           # Service discovery
├── api-gateway/             # Gateway + filters
│   ├── filter/
│   │   └── AuthenticationFilter.java
│   ├── config/
│   │   ├── RateLimitConfig.java
│   │   └── CircuitBreakerConfig.java
│   └── controller/
│       └── FallbackController.java
├── user-service/            # Auth + profiles + OAuth2
├── project-service/         # Projects + bidding
├── contract-service/        # Contracts + milestones + Saga
│   └── saga/
│       └── ContractSagaOrchestrator.java
├── payment-service/         # Escrow + transactions + Saga
│   └── saga/
│       └── PaymentSagaConsumer.java
├── review-service/          # Ratings + reviews
├── notification-service/    # Kafka + WebSocket
│   ├── kafka/
│   │   ├── NotificationProducer.java
│   │   └── NotificationConsumer.java
│   └── config/
│       └── WebSocketConfig.java
├── docker-compose.yml
├── init-db.sql
└── build-all.sh
```

---

## 🔮 Future Enhancements

| Feature | Description |
|---|---|
| 🔍 **Elasticsearch** | Full-text project search with filters |
| 📁 **MinIO** | File uploads for proposals & portfolios |
| 🤖 **AI Recommendations** | Smart project-freelancer matching |
| 🔄 **CI/CD Pipeline** | GitHub Actions + automated testing |
| ☸️ **Kubernetes** | K8s deployment with HPA |
| 📱 **Mobile Push** | FCM push notifications |
| 💬 **Real-time Chat** | WebSocket chat between client & freelancer |
| 🧪 **Integration Tests** | Testcontainers-based test suite |

---

## 👨‍💻 Author

<div align="center">

**Ahmed Nawar** — Backend Engineer · Java & Spring Boot Specialist

*Passionate about building scalable, production-grade microservices systems*

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Ahmed%20Nawar-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/ahmed-nawar-246513243)
[![GitHub](https://img.shields.io/badge/GitHub-AhmedNawar2003-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/AhmedNawar2003)
[![Email](https://img.shields.io/badge/Email-nawarahmed652%40gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:nawarahmed652@gmail.com)

<br/>

⭐ **If this project helped you, please give it a star!** ⭐

</div>
