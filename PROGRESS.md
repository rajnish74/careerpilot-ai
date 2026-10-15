# 📈 Progress Tracker — AI-Powered Job Portal

Last updated: **2026-10-15**

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
- [ ] `job-portal-company-service` — endpoints done; security, gateway integration and pending fixes remain
- [ ] `job-portal-job-service` — CRUD, search, categories, skills, tags and job linking built and tested; company integration, search filters, slug fix and security pending
- [ ] `job-portal-resume-service` — resume basics built (create, personal info, summary, default, delete); resume sections, fixes and testing pending

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
- [x] `CompanyController` with REST endpoints (`/api/companies`): create, get by id, get my company, list/filter, update, verify, deactivate, delete
- [x] `ApiResponse` DTO for simple success/failure messages
- [ ] Fix `generateUniqueSlug()` logic (inverted `existsBySlug` check, whitespace regex)
- [ ] Add ownership check (`assertOwner`) to `updateCompany()`
- [ ] Set default `status` (`PENDING_VERIFICATION`) and `active = true` on company creation (`@Builder.Default`)
- [ ] Map `verified` / `verifiedAt` in `CompanyMapper` and confirm entity fields match (`setVerified` is used in service)
- [ ] Restrict `verifyCompany` / `deactivateCompany` / verify & deactivate endpoints to `ROLE_ADMIN`
- [ ] Fix swapped argument order in `CompanyController` (`updateCompany`, `deleteCompany` pass `ownerId` and `id` the wrong way round)
- [ ] Replace `X-User-id` header with the authenticated user's id from JWT / API Gateway
- [x] Renamed `getMyCompanies()` → `getAllCompanies()` (filtered list of all companies)
- [ ] Replace raw `Exception` with custom exceptions + `@ControllerAdvice`

### Job Service (new module — `job-portal-job-service`)
- [x] Module created under `services`
- [x] Dependencies configured and downloaded
- [x] `Job` entity (title, description, requirements, responsibilities, benefits, companyId, employerId, location, salary range, job type, work mode, experience level, status, openings, deadlines, timestamps)
- [x] Embeddables: `JobLocation`, `SalaryRange`
- [x] Enums: `JobStatus`, `JobType`, `WorkMode`, `ExperienceLevel`
- [x] `JobRequest` / `JobResponse` / `JobSearchRequest` DTOs
- [x] `JobRepository` (+ `JpaSpecificationExecutor`) and `JobSpecification` for dynamic filters
- [x] `JobService` / `JobServiceImpl`: create, get by id, search, get by company, update, publish, close, delete, admin list
- [x] `JobMapper` (entity → `JobResponse`)
- [x] `JobController` (`/api/jobs`): create, get by id, search, by company, admin list, update, publish, close, delete
- [x] Default `status = DRAFT`, `openings = 1`, `active = true` now set explicitly on job creation
- [ ] Add `@Builder.Default` to `Job` entity initializers (Lombok still warns that the builder ignores them; the service sets them manually as a workaround)
- [ ] Resolve real `companyId` (currently hardcoded `1L`) and fetch `CompanyResponse` from company-service (OpenFeign + Resilience4j) instead of an empty object
- [ ] Update `JobSpecification` now that `Job` has relations: filter on `category.id` instead of `categoryId`, implement `skillIds`, `tagIds` and `keyword` filters, and lowercase the `location` search pattern
- [x] `JobCategory` entity (name, slug, description, icon, parent/subcategories, active, timestamps)
- [x] `JobCategoryRequest` / `JobCategoryResponse` DTOs, `JobCategoryMapper`
- [x] `JobCategoryRepository`, `JobCategoryService` / `JobCategoryServiceImpl` (create, list active, get by id, update, soft delete)
- [x] `JobCategoryController` (`/api/job-categories`): create, list, get by id, update, delete
- [x] Fixed `JobCategory` issues: `@Id`, `active` now set to `true` on create, and the mapper null-checks `subcategories` (no more NPE on create response)
- [ ] Fix `generateUniqueSlug()` in `JobCategoryServiceImpl`: `existsBySlug(base)` check is inverted (same bug as company service)
- [ ] Fix self-parent error message in `updateJobCategory` and add a check against deeper parent cycles
- [ ] Admin-only access for category create/update/delete
- [x] Linked category to `Job` (`@ManyToOne JobCategory`); `JobRequest.categoryId` is now used on create/update and returned in `JobResponse`
- [x] `JobSkill` entity (name, slug, category, active, timestamps) and `SkillCategory` enum
- [x] `JobSkillRequest` / `JobSkillResponse` DTOs, `JobSkillMapper`
- [x] `JobSkillRepository`, `JobSkillService` / `JobSkillServiceImpl` (create, list active, get by id, update, soft delete, `getSkillByIds`)
- [x] `JobSkillController` (`/api/job-skills`): create, list, get by id, update, delete
- [x] Fixed `JobSkill` issues: `@Id` and `active` now set to `true` on create
- [ ] Confirm `@Enumerated(EnumType.STRING)` is on `JobSkill.category` (otherwise it is stored as an ordinal)
- [ ] Fix `generateUniqueSlug()` in `JobSkillServiceImpl`: `existsBySlug(base)` check is inverted
- [ ] Make skill name uniqueness case-insensitive (`existsByNameIgnoreCase`) so "Java" and "java" can't both exist
- [ ] `getSkillByIds()`: handle null ids, ignore inactive skills, and fail when some ids aren't found
- [ ] Admin-only access for skill create/update/delete
- [x] Linked skills to `Job` (`@ManyToMany Set<JobSkill>`) using `JobRequest.skillIds`; returned in `JobResponse`
- [x] `JobTag` entity (name, slug, createdAt), `JobTagRequest` / `JobTagResponse` DTOs, `JobTagMapper`
- [x] `JobTagRepository`, `JobTagService` / `JobTagServiceImpl` (create, list, get by id, update, delete)
- [x] `JobTagController` (`/api/job-tags`): create, list, get by id, update, delete
- [ ] Fix `generateUniqueSlug()` in `JobTagServiceImpl`: `existsBySlug(base)` check is inverted
- [ ] Make tag name uniqueness case-insensitive (`existsByNameIgnoreCase`)
- [ ] Decide delete strategy for tags (hard delete will break once tags are linked to jobs; consider an `active` flag like categories/skills)
- [ ] Admin-only access for tag create/update/delete
- [x] Linked tags to `Job` (`@ManyToMany Set<JobTag>`) using `JobRequest.tagIds`, with new `getTagsByIds()` in `JobTagService`; returned in `JobResponse`
- [ ] `getSkillByIds()` / `getTagsByIds()`: fail when some ids aren't found; use a mutable `new HashSet<>()` instead of `Collections.emptySet()` when no ids are sent
- [ ] `JobMapper`: null-check `job.getCategory()` (jobs created before the category link will throw NPE)
- [ ] Extract a shared `SlugUtil` (same slug logic is duplicated in company, category, skill and tag services)
- [ ] Validation fixes: `requirements` is `nullable=false` but not validated in `JobRequest`; `@NotNull` message on `categoryId` says "Company ID"; use `TEXT` columns for long descriptions
- [ ] Remove `REMOTE` from `JobType` (duplicates `WorkMode.REMOTE`)
- [ ] Pagination for `getJobs` / `getAllJobsAdmin`
- [ ] Admin-only access for `/api/jobs/admin`; employer role check on create
- [ ] Replace `X-User-Id` header with JWT / gateway-provided user id
- [x] Tested job-service APIs successfully (jobs with category, skills and tags, plus category / skill / tag endpoints)
- [ ] Recruiter dashboard endpoints

### Resume Service (new module — `job-portal-resume-service`)
Resume builder: candidates create multiple structured resumes (template, visibility, personal info, summary, sections).
- [x] Module created under `services`, dependencies set up, database configured
- [x] Enums: `ResumeTemplate` (`CLASSIC`, `MODERN`, `CREATIVE`, `MINIMAL`, `PROFESSIONAL`), `ResumeVisibility` (`PUBLIC`, `PRIVATE`, `LINK_ONLY`)
- [x] `Resume` entity + `PersonalInfo` embeddable
- [x] DTOs: `CreateResumeRequest`, `ResumeResponse`, `PersonalInfoResponse`
- [x] `ResumeRepository` (`findByCandidateIdAndIsActiveTrue`, `findByCandidateIdAndIsDefaultTrue`)
- [x] `ResumeService` / `ResumeServiceImpl`: create, get by id (owner-checked), get my resumes, update personal info (partial update), update summary, set default, soft delete
- [x] `ResumeMapper`
- [x] `ResumeController` (`/api/resumes`): create, get by id, my resumes, update personal info, update summary, set default, delete
- [ ] Fix `ResumeMapper.toPersonalInfoResponse()`: null-check `personalInfo` (a new resume has none, so create throws NPE)
- [ ] Default `template` (`PROFESSIONAL`), `visibility` and `completionScore` on create when the request omits them (columns are `nullable=false`); add `@Builder.Default` to `Resume` initializers
- [ ] Block access to soft-deleted resumes (`getResumeEntity` ignores `isActive`) and prevent setting an inactive resume as default
- [ ] Add `@Transactional` to `ResumeServiceImpl` (default switch is two writes); make the first resume a candidate creates the default automatically
- [ ] Use a proper `PersonalInfoRequest` DTO with validation (email, URLs) instead of reusing `PersonalInfoResponse`
- [ ] Send `summary` in the request body (not `@RequestParam`) and store it as `TEXT`
- [ ] Return `201 Created` from `POST /api/resumes`
- [ ] Resume sections: work experience, education, skills, projects, certifications, awards, languages (CRUD each; already sketched in `ResumeResponse`)
- [ ] Completion score calculation
- [ ] Enforce `visibility` (public / link-only sharing) and employer access to a candidate's resume when applying
- [ ] PDF export / template rendering (optional: upload existing resume + AI parsing via `ai-service`)
- [ ] Replace `X-User-Id` header with JWT / gateway user id
- [ ] Test the resume APIs

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

### 2026-10-13
- Added `CompanyController` (`/api/companies`) with all endpoints:
  - `POST /api/companies` — create company
  - `GET /api/companies/{id}` — get company by id
  - `GET /api/companies/my` — get the owner's company
  - `GET /api/companies` — list companies with optional `companyType`, `status`, `industryType` filters
  - `PUT /api/companies/{id}` — update company
  - `PATCH /api/companies/{id}/verify` — verify company (admin)
  - `PATCH /api/companies/{id}/deactivate` — deactivate company (admin)
  - `DELETE /api/companies/{id}` — delete company (owner)
- Added `ApiResponse` DTO (`message`, `status`).
- Company service endpoints are now complete.
- Created new module `job-portal-job-service` under `services` and started setting up / downloading its dependencies.
- **To fix next:**
  - `updateCompany` and `deleteCompany` in the controller pass `(ownerId, id)` but the service expects `(companyId, ownerId)`, so the arguments are swapped.
  - `X-User-id` header is spoofable; read the user id from the validated JWT (or have the API Gateway inject it after validation).
  - `verify` / `deactivate` endpoints still have no admin-only check.
  - Earlier items still open: `generateUniqueSlug()` logic, `assertOwner()` in `updateCompany()`, `@Builder.Default` / default `status`, `verified`/`verifiedAt` mapping, global exception handler.
  - Finish `job-portal-job-service` setup: confirm dependencies resolve, connect its database, and add a test controller.

### 2026-10-14
- Built out `job-portal-job-service`:
  - `Job` entity with `JobLocation` and `SalaryRange` embeddables.
  - `JobStatus`, `JobType`, `WorkMode`, `ExperienceLevel` enums.
  - `JobRequest`, `JobResponse`, `JobSearchRequest` DTOs.
  - `JobRepository` with `JpaSpecificationExecutor` and `JobSpecification` (filters: job type, work mode, experience level, company, location, salary range, openings; defaults to active + `OPEN` jobs).
  - `JobServiceImpl`: create, get by id, search, get by company, update (employer-checked), publish, close, delete (employer-checked), admin list.
  - `JobMapper` and `JobController` (`/api/jobs`) with all endpoints, including `/company/{companyId}` and `/admin`.
- Added job categories to `job-portal-job-service`:
  - `JobCategory` entity with self-referencing parent/subcategories.
  - `JobCategoryRequest`, `JobCategoryResponse`, `JobCategoryMapper`.
  - `JobCategoryRepository` (`existsByName`, `existsBySlug`, `findByActiveTrue`).
  - `JobCategoryServiceImpl`: create (with optional parent and unique slug), list active, get by id, update, soft delete (`active = false`).
  - `JobCategoryController` (`/api/job-categories`) with create, list, get, update, delete.
- Added job skills to `job-portal-job-service`:
  - `SkillCategory` enum and `JobSkill` entity.
  - `JobSkillRequest`, `JobSkillResponse`, `JobSkillMapper`.
  - `JobSkillRepository` (`findByActiveTrue`, `existsByName`, `existsBySlug`).
  - `JobSkillServiceImpl`: create (with unique slug), list active, get by id, update, soft delete, `getSkillByIds` (for linking to jobs).
  - `JobSkillController` (`/api/job-skills`) with create, list, get, update, delete.
- **To fix next:**
  - `Job` builder ignores field initializers, so `status` is null on create and the insert will fail (`status` is `nullable=false`). Add `@Builder.Default` or set `.status(JobStatus.DRAFT)` explicitly (same for `openings` and `active`).
  - `companyId` is hardcoded to `1L` and `CompanyResponse` is always empty; call company-service to resolve the employer's company and its details.
  - `JobSpecification` filters on `categoryId`, which doesn't exist on `Job`; this throws at runtime when the param is sent. `keyword`, `skillIds`, `tagIds` aren't implemented, and the `location` pattern isn't lowercased.
  - `requirements` is `nullable=false` but not validated in `JobRequest`; descriptions need a `TEXT` column.
  - `JobType` contains `REMOTE`, which duplicates `WorkMode.REMOTE`.
  - `X-User-Id` is spoofable, `/api/jobs/admin` has no admin check, and listings have no pagination.
  - Job categories: `@id` should be `@Id` (compile error); `active` and `subcategories` need `@Builder.Default` (otherwise create throws NPE in the mapper and `findByActiveTrue()` returns nothing); `generateUniqueSlug()` has the inverted `existsBySlug` check; no admin check on category endpoints.
  - `Job` isn't linked to `JobCategory` yet, so the `categoryId` filter and `JobRequest.categoryId` still do nothing.
  - Job skills: `@id` should be `@Id` (compile error); `active` needs `@Builder.Default` (otherwise `findByActiveTrue()` returns nothing); `category` needs `@Enumerated(EnumType.STRING)`; `generateUniqueSlug()` has the inverted `existsBySlug` check; name check is case-sensitive; no admin check on skill endpoints.
  - `Job` isn't linked to skills yet, so `JobRequest.skillIds` and the `skillIds` filter still do nothing.

### 2026-10-15
- Added job tags to `job-portal-job-service`:
  - `JobTag` entity (`name`, `slug`, `createdAt`).
  - `JobTagRequest`, `JobTagResponse`, `JobTagMapper`.
  - `JobTagRepository` (`existsByName`, `existsBySlug`).
  - `JobTagServiceImpl`: create (with unique slug), list, get by id, update, delete, `getTagEntityById`.
  - `JobTagController` (`/api/job-tags`) with create, list, get, update, delete.
- Linked jobs to categories, skills and tags:
  - `Job` entity now has `@ManyToOne JobCategory category`, `@ManyToMany Set<JobSkill> skills` and `@ManyToMany Set<JobTag> tags`.
  - `JobServiceImpl` resolves category, skills and tags on create/update (class is `@Transactional`), and sets `status = DRAFT`, `active = true` and default `openings` explicitly.
  - Added `getTagsByIds()` to `JobTagService`.
  - `JobMapper` / `JobResponse` now return `company`, `category`, `skills` and `tags`.
- Fixed earlier issues: `@Id` on category/skill entities, `active` set on create (categories and skills now show up in listings), NPE in `JobCategoryMapper` for missing `subcategories`, and builder defaults for job creation.
- Tested the job-service APIs successfully (jobs with category, skills and tags, plus category / skill / tag endpoints).
- Created new module `job-portal-resume-service` under `services`, set up its dependencies and configured its database.
- Built the resume basics in `job-portal-resume-service`:
  - `Resume` entity (`candidateId`, `title`, `template`, `visibility`, `isDefault`, `personalInfo`, `summary`, `isActive`, `completionScore`, timestamps) and `PersonalInfo` embeddable.
  - `ResumeTemplate` and `ResumeVisibility` enums.
  - `CreateResumeRequest`, `ResumeResponse`, `PersonalInfoResponse`, `ResumeMapper`.
  - `ResumeRepository` and `ResumeServiceImpl`: create (with default switching), get by id (owner-checked), list my resumes, partial personal-info update, summary update, set default, soft delete.
  - `ResumeController` (`/api/resumes`) with create, get, my, personal-info, summary, set-default and delete endpoints.
- **To fix next:**
  - `generateUniqueSlug()` in the category, skill and tag services still has the inverted `existsBySlug` check, so slugs come out as `java-1` instead of `java` and a duplicate slug can slip through (e.g. "C" and "C++"). Fix it once in a shared `SlugUtil`.
  - `JobSpecification` still needs updating for the new relations (`category.id`, `skillIds`, `tagIds`, `keyword`) and the lowercase `location` pattern.
  - `companyId` is still hardcoded to `1L` with an empty `CompanyResponse`; call company-service (OpenFeign + Resilience4j).
  - `JobMapper` should null-check `job.getCategory()`; `getSkillByIds()` / `getTagsByIds()` should fail on unknown ids and `Job` should use a mutable `HashSet` instead of `Collections.emptySet()`.
  - Admin-only checks on category/skill/tag endpoints and `/api/jobs/admin`, `X-User-Id` replaced by JWT / gateway user id, pagination, and tag delete strategy.
  - Resume service bugs: `ResumeMapper` throws NPE when `personalInfo` is null (every new resume), `template` / `visibility` can be null on create (columns are `nullable=false`), soft-deleted resumes can still be fetched and edited, service isn't `@Transactional`.
  - Resume service gaps: validation on personal info, `summary` as request body, resume sections (experience, education, skills, projects, etc.), completion score, visibility rules, then test the APIs.

<!-- Add new dated entries above this line as you make progress -->