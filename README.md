<div align="center">
  
[![Typing SVG](https://readme-typing-svg.demolab.com?font=JetBrains+Mono&weight=700&size=24&pause=1000&color=00F5A0&center=true&vCenter=true&width=900&lines=🚀+FreelanceHub+Platform;Production-Grade+Microservices+%7C+Java+21;Event-Driven+Architecture+%7C+Kafka+%7C+WebSockets;Secure+%7C+JWT+%7C+OAuth2+%7C+RBAC;Cloud-Native+%7C+Docker+%7C+Microservices;Elasticsearch+%7C+MinIO+%7C+Zipkin+%7C+Observability)](https://git.io/typing-svg)

<br>
# 🚀 FreelanceHub — Freelancing Microservices Platform

### `Java 21` · `Spring Boot 3` · `Kafka` · `JWT` · `Docker` · `Cloud-Native`

**A production-grade distributed backend platform** for managing freelance projects, bidding, contracts, escrow payments, and real-time notifications.

<br/>

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-Search-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)
![Zipkin](https://img.shields.io/badge/Zipkin-Tracing-FE7139?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Production%20Ready-00C851?style=for-the-badge)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [System Architecture](#️-system-architecture)
- [Services Breakdown](#-services-breakdown)
- [Security Model](#-security-model)
- [Event-Driven Flow](#-event-driven-flow)
- [Tech Stack](#️-tech-stack)
- [Design Highlights](#-design-highlights)
- [Observability](#-observability)
- [Getting Started](#-getting-started)
- [Future Enhancements](#-future-enhancements)

---

## 🔍 Overview

FreelanceHub is a **fully distributed, event-driven microservices platform** that simulates real-world freelancing systems like Upwork.

| Capability | Implementation |
|---|---|
| 🔀 **API Routing & Security** | Spring Cloud Gateway |
| 🔍 **Service Discovery** | Netflix Eureka |
| ⚙️ **Centralized Config** | Spring Cloud Config Server |
| 🔐 **Authentication** | JWT + OAuth2 |
| 📨 **Async Messaging** | Apache Kafka |
| 🔗 **Inter-service Calls** | OpenFeign |
| 📊 **Distributed Tracing** | Zipkin |
| 🔎 **Search Engine** | Elasticsearch |
| 📁 **File Storage** | MinIO |
| 🐳 **Containerization** | Docker |

---

## 🏛️ System Architecture

<p align="center">
  <img src="/Freelancing Microservices Platform.jpeg" width="100%" alt="SmartEvent System Architecture"/>
</p>

```text
                    ┌──────────────────────┐
                    │      Client Apps      │
                    └──────────┬───────────┘
                               │
                    ┌──────────▼───────────┐
                    │      API Gateway      │
                    └──────────┬───────────┘
                               │
           ┌───────────────────┼───────────────────┐
           │                   │                   │
┌──────────▼──────┐  ┌─────────▼──────┐  ┌────────▼────────┐
│   User Service  │  │ Project Service │  │ Contract Service│
└──────────┬──────┘  └─────────┬──────┘  └────────┬────────┘
           │                   │                   │
           │          ┌────────▼────────┐          │
           │          │  Kafka Broker   │◄─────────┘
           │          └────────┬────────┘
           │                   │
┌──────────▼──────┐  ┌─────────▼──────┐
│ Payment Service │  │ Notification   │
└──────────┬──────┘  │    Service     │
           │          └────────────────┘
           ▼
┌─────────────────┐     ┌──────────────┐     ┌──────────────┐
│  Config Server  │     │    Eureka    │     │    Zipkin    │
└─────────────────┘     └──────────────┘     └──────────────┘

```
---

## 🧩 Services Breakdown

### 🔧 Infrastructure Services

| Service | Port | Description |
|---|---|---|
| API Gateway | 8080 | Routing & security |
| Eureka Server | 8761 | Service discovery |
| Config Server | 8888 | Centralized config |
| Zipkin | 9411 | Tracing |

---

### 💼 Business Services

| Service | Port | Responsibilities |
|---|---|---|
| User Service | 8081 | Auth, profiles, roles |
| Project Service | 8082 | Projects & bidding |
| Contract Service | 8083 | Contracts & milestones |
| Payment Service | 8084 | Escrow & transactions |
| Review Service | 8085 | Ratings & reviews |
| Notification Service | 8086 | Kafka + WebSocket |

---

## 🔐 Security Model

- JWT Authentication  
- OAuth2 (Google login optional)  
- Stateless architecture  
- Role-based access:

| Role | Permissions |
|---|---|
| CLIENT | Post projects, accept bids |
| FREELANCER | Submit bids, complete work |
| ADMIN | Full control |

---

## 📨 Event-Driven Flow

```text
Bid Created
      │
      ▼
Kafka Topic (BidCreated)
      │
      ▼
Contract Created
      │
      ▼
Payment Escrow Initiated
      │
      ▼
Milestone Completed
      │
      ▼
Payment Released
      │
      ▼
Notification Sent
```

---

## 🛠️ Tech Stack

- Java 21  
- Spring Boot 3  
- Spring Cloud  
- Kafka  
- PostgreSQL  
- Elasticsearch  
- MinIO  
- Docker  
- JWT + OAuth2  

---

## 🎯 Design Highlights

| Area | Benefit |
|---|---|
| Microservices | Scalability |
| Kafka | Async communication |
| Elasticsearch | Advanced search |
| MinIO | File storage |
| JWT | Secure auth |
| Zipkin | Debugging |

---

## 📊 Observability

- Zipkin tracing  
- Logs per service  
- Swagger APIs  

---

## 🚀 Getting Started

```bash
git clone https://github.com/your-username/freelancehub.git
cd freelancehub
docker-compose up --build
```


📈 Future Enhancements

- Redis caching
- CI/CD pipeline
- Kubernetes
- AI recommendations


## 👨‍💻 Author

<div align="center">

**Ahmed Nawar** — Backend Engineer · Java & Spring Boot Specialist

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Ahmed%20Nawar-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/ahmed-nawar-246513243)
[![GitHub](https://img.shields.io/badge/GitHub-AhmedNawar2003-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/AhmedNawar2003)
[![Email](https://img.shields.io/badge/Email-nawarahmed652%40gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:nawarahmed652@gmail.com)

<br/>
