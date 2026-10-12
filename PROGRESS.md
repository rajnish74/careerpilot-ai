# 📈 Progress Tracker — AI-Powered Job Portal

Last updated: **2026-10-12**

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

- [ ] JWT validation filter for protected routes in `job-portal-user-service`
- [ ] Lock down `SecurityConfig` route rules once JWT filter is in place
- [ ] `job-portal-company-service` — DB connected & test controller verified, entity/endpoints pending

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
- [x] User profile endpoints (`GET/PUT /api/users/profile`)
- [x] Admin user-management endpoints (`GET /api/users`, `GET /{id}`, suspend/activate/delete)
- [ ] Global exception handler (`@ControllerAdvice`) — auth still throws raw `Exception`

### Company Service (new module — `job-portal-company-service`)
- [x] Module scaffolded under `services`
- [x] Database connection configured and verified
- [x] Test controller created and confirmed working
- [x] `Company` entity + Postgres schema (name, slug, description, size, type, industry, status, socialLinks, etc.)
- [x] `CompanyRequest` / `CompanyResponse` DTOs with validation
- [x] `CompanyRepository` with filter query and existence checks
- [x] `CompanyService` interface defined (create, get, update, verify, delete, deactivate, list-mine)
- [x] `CompanyServiceImpl` implemented (create, get by id, get mine, filtered list, update, verify, delete, deactivate)
- [x] `CompanyMapper` (entity → `CompanyResponse`, incl. social links)
- [x] Company creation logic with duplicate checks (owner, name, registration number) and slug generation
- [x] One-company-per-owner rule enforced on creation
- [x] `phone` / `email` now persisted on `Company` and returned in the response
- [ ] `CompanyController` — no REST endpoints exposed yet
- [ ] Fix `generateUniqueSlug()` logic (inverted `existsBySlug` check, whitespace regex)
- [ ] Add ownership check (`assertOwner`) to `updateCompany()`
- [ ] Set default `status` (`PENDING_VERIFICATION`) and `active = true` on company creation (`@Builder.Default`)
- [ ] Map `verified` / `verifiedAt` in `CompanyMapper` and confirm entity fields match (`setVerified` is used in service)
- [ ] Restrict `verifyCompany` / `deactivateCompany` to `ROLE_ADMIN`
- [ ] Rename `getMyCompanies()` → `searchCompanies()` / `getAllCompanies()` (it returns filtered companies, not the owner's)
- [ ] Replace raw `Exception` with custom exceptions + `@ControllerAdvice`

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

### 2026-09-20
- Added `UserController` (`/api/users`) — profile get/update, get by ID, list all users, suspend/activate/delete (admin actions).
- Added `UserService`/`UserServiceImpl` — `getUserByEmail`, `getUserById`, `getAllUsers`, `updateProfile`, `suspendUser`, `activateUser`, `deleteUser`.
- Added `UpdateUserRequest` payload (`fullName`, `phone`, `profileImage`) with partial-update support (only non-null fields applied).
- Extended `UserMapper` with `toListResponse()` for bulk user responses.
- **To fix next:**
  - `UserResponse` still exposes `password` — remove it from the response DTO (still not fixed, 3rd time flagged).
  - `getProfile`/`updateProfile` trust an `X-User-Email` header — spoofable until the JWT validation filter reads the email from the token instead.
  - Admin routes (`suspend`, `activate`, `delete`, `getAllUsers`) have no role check yet — need `@PreAuthorize("hasRole('ADMIN')")` once role enforcement is wired.
  - `getAllUsers()` has no pagination — will need `Pageable` before real data volume.
- Created new module `job-portal-company-service` under `services`.
- Connected `job-portal-company-service` to its database.
- Added a test controller in `job-portal-company-service` and verified it works.
- **To fix next:** build out `Company` entity/schema, registration & profile endpoints, and link companies to their owning `ROLE_EMPLOYER` user.

### 2026-09-21
- Added `Company` entity (`name`, `slug`, `description`, `tagline`, `logoUrl`, `coverImageUrl`, `website`, `foundedYear`, `companySize`, `companyType`, `industryType`, `status`, `registrationNumber`, `ownerId`, `socialLinks`, `active`, timestamps).
- Added `CompanySize`, `CompanyType`, `CompanyStatus`, `IndustryType`, `SocialPlatform` enums.
- Added `SocialLink` `@Embeddable` + `SocialLinkResponse` DTO.
- Added `CompanyRequest` (with bean validation) and `CompanyResponse` DTOs.
- Added `CompanyRepository` with `findByOwnerId`, `existsByOwnerId`, `existsByName`, `existsBySlug`, `existsByRegistrationNumber` (typo currently), and a filtered `findByFilters` query.
- Defined `CompanyService` interface (create, get by id, get/list mine, update, verify, delete, deactivate).
- Scaffolded `CompanyServiceImpl` — all methods currently stubbed, no logic yet.
- Fixed `existsBYRegistrationNumber` typo, `findByFilters` JPQL parameter/spacing bugs, and `ownerId`/`getMyCompanies` design inconsistency.
- **To fix next:**
  - `phone`/`email` exist in `CompanyRequest`/`CompanyResponse` but not on the `Company` entity yet — need to add these columns so they actually persist and round-trip in the response, not just sit in the request DTO.
  - Implement `CompanyServiceImpl` methods, add `CompanyMapper`, and add `CompanyController` with REST endpoints.

### 2026-10-12
- Implemented `CompanyServiceImpl`: `createCompany`, `getCompanyById`, `getMyCompany`, filtered company listing, `updateCompany`, `verifyCompany`, `deleteCompany` (owner-checked), `deactivateCompany`, `getCompanyEntityById`.
- Added duplicate validation on create (one company per owner, unique name, unique registration number).
- Added slug generation and social-link mapping helpers.
- Added `CompanyMapper` (`toResponse`, `toSocialLinkResponse`).
- Added `phone` and `email` to the `Company` entity; they are now saved on create/update and returned in the response.
- Updated `CompanyService` interface signatures (`throws Exception`, `deleteCompany(companyId, ownerId)`).
- **To fix next:**
  - `generateUniqueSlug()`: the `existsBySlug(base)` check is inverted (returns `base` when it already exists), and `replaceAll("\\s-", "-")` should be `replaceAll("\\s+", "-")`.
  - `updateCompany()` never calls `assertOwner()`, so any user could update any company.
  - Builder ignores field initializers: set `status` (`PENDING_VERIFICATION`) explicitly and add `@Builder.Default` for `active` and `socialLinks` in `Company`.
  - `setVerified(...)` is used in the service but `verified`/`verifiedAt` aren't mapped in `CompanyMapper`; also check `CompanyResponse` field naming (`companyStatus`) vs the mapper's `.status(...)`.
  - Add `@PreAuthorize`/role checks for `verifyCompany` and `deactivateCompany`.
  - Build `CompanyController` and wire owner id from the authenticated user (not a spoofable header).

<!-- Add new dated entries above this line as you make progress -->