# 📈 Progress Tracker — AI-Powered Job Portal

Last updated: **2026-09-04**

This file tracks day-to-day / milestone progress on the project. Update it as you go so it doubles as a dev log.

---

## ✅ Done

- [x] Multi-module Maven project scaffolded (`job-portal-system` parent pom)
  - Modules: `cloud`, `common-lib`, `services`
- [x] Parent POM configured: Java 21, Spring Boot 4.1.1, Spring Cloud 2025.1.0
- [x] `job-portal-cloud` module created (pom only, no services yet)
- [x] `job-portal-common` module created with validation, lombok, jackson-annotations deps
- [x] `job-portal-service` (services) parent module created
- [x] `job-portal-user-service` module created with JPA, Web, Postgres, Lombok deps
- [x] `HomeController` created with a working `GET /` → returns `"Hello World"`
- [x] `application.yaml` present in `job-portal-user-service`

---

## 🟡 In Progress

- [ ] Fixing dependency issues in `job-portal-user-service/pom.xml`:
  - `spring-boot-starter-webmvc` → should be `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa-test` / `spring-boot-starter-webmvc-test` → should be `spring-boot-starter-test`
- [ ] Fixing `jib.maven.plugin` artifactId → should be `jib-maven-plugin` (in parent pom pluginManagement)

---

## 🔴 Not Started

### Infra (`job-portal-cloud`)
- [ ] `config-server` submodule (Spring Cloud Config)
- [ ] `discovery-server` submodule (Eureka)
- [ ] `api-gateway` submodule (Spring Cloud Gateway)

### User Service
- [x] User entity + Postgres schema (`User`, `UserRole`, `UserStatus`)
- [x] Signup endpoint (`POST /auth/signup`) with validation (`SignupRequest`)
- [x] Login endpoint (`POST /auth/login`) with email/password authentication
- [x] Password hashing (BCrypt via `PasswordEncoder`)
- [x] JWT generation (`JwtProvider`) on signup & login
- [x] `CustomUserDetailsService` for Spring Security authentication
- [x] Basic `SecurityConfig` (stateless session, CSRF disabled)
- [ ] JWT validation filter for protected routes (tokens are issued but not yet verified on incoming requests)
- [ ] Lock down `SecurityConfig` — currently `anyRequest().permitAll()`, needs route-level rules once JWT filter is in place
- [ ] Role-based access (JOB_SEEKER / EMPLOYER / ADMIN) — roles are in the JWT claims but not yet enforced
- [ ] Profile management (resume upload, skills, experience)
- [ ] Global exception handler (`@ControllerAdvice`) — auth still throws raw `Exception`

### Job Service (new module)
- [ ] Job posting CRUD
- [ ] Search & filter (by location, skill, salary)
- [ ] Recruiter dashboard endpoints

### Application Service (new module)
- [ ] Apply-to-job flow
- [ ] Application status tracking
- [ ] Kafka producer: `application.submitted` event

### Notification Service (new module)
- [ ] Kafka consumer for `application.submitted`, `job.posted`, etc.
- [ ] Email/notification dispatch

### AI Service (new module)
- [ ] Gemini AI client setup
- [ ] Resume parsing (extract skills/experience from uploaded resume)
- [ ] Job-to-candidate matching score
- [ ] AI chatbot for candidate queries

### Cross-cutting
- [ ] Redis caching (job listings, user sessions)
- [ ] Resilience4j (circuit breaker on gateway → services)
- [ ] Centralized logging/tracing (optional: ELK / Zipkin)
- [ ] Global exception handling in `common-lib`
- [ ] Common DTOs/enums in `common-lib` (currently empty)

### DevOps
- [ ] Dockerfile per service
- [ ] `docker-compose.yml` (Postgres, Redis, Kafka, Zookeeper, all services)
- [ ] Environment-based config (dev/prod profiles)
- [ ] CI/CD pipeline (GitHub Actions)

### Frontend
- [ ] React 19 project scaffold
- [ ] Redux Toolkit store setup
- [ ] Tailwind + Shadcn UI setup
- [ ] Auth pages (login/register)
- [ ] Job listing + search page
- [ ] Recruiter dashboard
- [ ] Candidate profile & application tracking page

---

## 🗒️ Dev Log

### 2026-09-04
- Reviewed initial project structure (screenshot shared).
- Confirmed `HomeController` returns "Hello World" successfully — base Spring Boot app runs.
- Identified and fixed pom.xml dependency naming issues across modules (`spring-boot-starter-web`, `spring-boot-starter-test`, `jib-maven-plugin`).
- Fixed folder/artifactId naming consistency, added `.gitignore`.
- Created `README.md` and `progress.md` to track architecture & tasks going forward.

### 2026-09-05
- Added `User` entity (`id`, `fullName`, `email`, `password`, `phone`, `profileImage`, `role`, `status`, timestamps, `lastLogin`, `suspendedAt`, `deletedAt`).
- Added `UserRole` enum (`ROLE_ADMIN`, `ROLE_JOB_SEEKER`, `ROLE_EMPLOYER`) and `UserStatus` enum (`ACTIVE`, `INACTIVE`, `SUSPENDED`, `DELETED`).
- Added `UserRepository` (`findByEmail`, `existsByEmail`).
- Added `SignupRequest` / `LoginRequest` payloads with bean validation.
- Added `AuthResponse` and `UserResponse` DTOs + `UserMapper`.
- Implemented `AuthServiceImpl.signup()` — checks duplicate email, blocks self-registration as `ROLE_ADMIN`, saves user, returns `AuthResponse`.
- Added `AuthController` with `POST /auth/signup`.
- `login()` stubbed out (returns `null`) — pending implementation.
- **To fix next:** remove `password` from `UserResponse` (currently leaked in API response), hash passwords with BCrypt, replace `"dummy jwt"` with real JWT issuance, implement `login()`, add a global `@ControllerAdvice` for exceptions.

### 2026-09-06
- Added Spring Security: `SecurityConfig` (stateless sessions, CSRF disabled, temporary `permitAll()` on all routes).
- Added `CustomUserDetailsService` implementing `UserDetailsService`, loading user by email with role as `GrantedAuthority`.
- Added `JwtProvider` — generates JWT with `email`, `authorities`, and `userId` claims, 10-day expiry.
- Wired `PasswordEncoder` (BCrypt) into `AuthServiceImpl` — passwords are now hashed on signup.
- Implemented `AuthServiceImpl.login()` — authenticates via `CustomUserDetailsService` + `PasswordEncoder.matches()`, updates `lastLogin`, returns JWT.
- Wired up `POST /auth/login` in `AuthController`.
- **To fix next:**
  - `JwtConstant.SECRET_KEY` is a short hardcoded string — move to config/env var and use a proper 256-bit+ secret.
  - No JWT validation filter yet — issued tokens aren't verified on subsequent requests.
  - `SecurityConfig` currently allows all requests — needs to require auth on protected routes once the JWT filter exists.
  - `UserResponse` still exposes `password` — remove it from the response DTO.

<!-- Add new dated entries above this line as you make progress -->