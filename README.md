<div align="center">

# 🧠 AI-Powered Job Portal — Microservices System

**An intelligent, distributed job portal platform built on Spring Boot microservices, powered by Google Gemini AI.**

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=white)](https://react.dev/)
[![Redux Toolkit](https://img.shields.io/badge/Redux%20Toolkit-764ABC?style=for-the-badge&logo=redux&logoColor=white)](https://redux-toolkit.js.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![Shadcn UI](https://img.shields.io/badge/Shadcn%20UI-000000?style=for-the-badge&logo=shadcnui&logoColor=white)](https://ui.shadcn.com/)
[![Gemini AI](https://img.shields.io/badge/Google%20Gemini%20AI-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white)](https://ai.google.dev/)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](./LICENSE)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg?style=for-the-badge)](#)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=for-the-badge)](#)

</div>

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Architecture](#️-architecture)
- [Tech Stack](#️-tech-stack)
- [Modules](#-modules)
- [Getting Started](#-getting-started)
- [Roadmap](#️-roadmap)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)

---

## 📌 Overview

This project is a full-scale, production-style **Job Portal** system designed using modern distributed systems patterns — service discovery, API gateway routing, event-driven communication, resilience patterns, and AI-assisted features (resume parsing, job-matching, chatbot assistance, etc.).

The system is built as a **multi-module Maven project**, cleanly separating cloud infrastructure, shared libraries, and business services — making it scalable, maintainable, and easy to extend with new microservices.

---

## 🏗️ Architecture

```
job-portal-system (parent pom)
│
├── cloud [job-portal-cloud]              → Infrastructure services
│   ├── config-server                     → Centralized configuration (planned)
│   ├── discovery-server (Eureka)         → Service Registry (planned)
│   └── api-gateway                       → Spring Cloud Gateway (planned)
│
├── common-lib [job-portal-common]        → Shared DTOs, enums, exceptions, utils
│
└── services [job-portal-service]         → Business microservices
    └── job-portal-user-service           → Auth, user profile, JWT (in progress)
        ├── job-service                   → Job postings & search (planned)
        ├── application-service           → Applications & tracking (planned)
        ├── ai-service                    → Gemini AI integration (planned)
        └── notification-service          → Kafka-driven notifications (planned)
```

**Communication patterns:**
- **Synchronous** → REST via API Gateway, service-to-service calls via Eureka + OpenFeign
- **Asynchronous / Event-driven** → Kafka topics for decoupled workflows (e.g., new application → notification, resume upload → AI parsing)
- **Resilience** → Resilience4j (circuit breaker, retry, rate limiter, bulkhead) on all inter-service calls

---

## 🛠️ Tech Stack

### Backend
| Category | Technology |
|---|---|
| Core Framework | Spring Boot 4 |
| Microservices | Spring Cloud (2025.1.0) |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Messaging / Events | Apache Kafka |
| Caching | Redis |
| Database | PostgreSQL |
| Auth | JWT (jjwt) |
| Resilience | Resilience4j |
| Build Tool | Maven (multi-module) |
| Language | Java 21 |

### Frontend
| Category | Technology |
|---|---|
| Framework | React 19 |
| State Management | Redux Toolkit |
| Styling | Tailwind CSS |
| UI Components | Shadcn UI |

### AI
| Category | Technology |
|---|---|
| Generative AI | Google Gemini AI / GenAI |
| Use cases | Resume parsing, job matching, chatbot, smart recommendations |

### DevOps & Infrastructure
- Docker (containerization per microservice)
- Docker Compose (local orchestration)
- Distributed Microservices Architecture
- Event-Driven Architecture (Kafka)

---

## 📂 Modules

| Module | Description | Status |
|---|---|---|
| `job-portal-cloud` | Infra module (config server, Eureka, gateway) | 🔴 Not started |
| `job-portal-common` | Shared DTOs, enums, exceptions | 🟡 Scaffolded |
| `job-portal-user-service` | Auth, JWT, user profile | 🟡 In progress (signup + login + JWT done; validation filter pending) |
| `job-service` | Job postings, search, filters | 🔴 Not started |
| `application-service` | Job applications, tracking | 🔴 Not started |
| `ai-service` | Gemini AI integration | 🔴 Not started |
| `notification-service` | Kafka-based notifications | 🔴 Not started |
| Frontend (React) | User/recruiter dashboards | 🔴 Not started |

Full task-by-task tracking lives in [`progress.md`](./progress.md).

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL, Redis, Kafka (via Docker Compose)
- Node.js 20+ (for frontend)

### Build the backend
```bash
cd job-portal-system
mvn clean install
```

### Run a service
```bash
cd services/job-portal-user-service
mvn spring-boot:run
```

### Frontend (once scaffolded)
```bash
cd frontend
npm install
npm run dev
```

---

## 🗺️ Roadmap

- [ ] Fix pom.xml dependency issues (`spring-boot-starter-web`, `spring-boot-starter-test`, `jib-maven-plugin`)
- [ ] Set up `config-server` + `discovery-server` (Eureka) in `job-portal-cloud`
- [ ] Set up `api-gateway` with routing to user-service
- [x] Implement JWT auth in `job-portal-user-service`
- [ ] Add PostgreSQL entities: User, Profile, Role
- [ ] Build `job-service` (CRUD + search)
- [ ] Build `application-service` + Kafka events
- [ ] Integrate Redis caching
- [ ] Add Resilience4j to inter-service calls
- [ ] Integrate Gemini AI for resume parsing & job matching
- [ ] Dockerize all services + docker-compose.yml
- [ ] Build React 19 frontend (Redux Toolkit + Tailwind + Shadcn)
- [ ] Deploy (CI/CD pipeline)

---

## 🤝 Contributing

This is currently a solo learning/portfolio project, but suggestions and issue reports are welcome.

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](./LICENSE) file for details.

---

## 👤 Author

**Rajnish**
Backend-focused developer building distributed systems with Spring Boot & AI.

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](#)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](#)

<div align="center">

⭐ **If you find this project interesting, consider giving it a star!** ⭐

</div>