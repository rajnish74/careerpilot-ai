# 📈 Progress Tracker — AI-Powered Job Portal

Last updated: **2026-10-17**

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
- [ ] `job-portal-resume-service` — all resume sections built and tested; read-access rules, visibility, completion score and security pending
- [ ] `job-portal-application-service` — apply, status tracking, withdraw, star, notes and company filters built and tested; real job/company/user data, AI screening, Kafka events and fixes pending
- [ ] `job-portal-preferences-service` — module created, saved jobs built; fixes, testing and other preference features pending
- [ ] `job-portal-cloud` — Eureka registry and API gateway (routes + JWT) built and tested; gateway hardening and config-server pending

---

## 🔴 Not Started

### Infra (`job-portal-cloud`)
- [ ] `config-server` submodule (Spring Cloud Config)
- [x] Eureka service registry created in `cloud` (`job-portal-registry-service`, Eureka Server)
- [x] Eureka client setup for all services (user, company, job, resume, application, preferences)
- [ ] Verify every service shows as `UP` in the Eureka dashboard, each with a unique `spring.application.name` and port
- [ ] Use service names instead of hardcoded ids/URLs for service-to-service calls (OpenFeign + Resilience4j): application-service → job / resume / company / user, job-service → company-service, preferences-service → job-service
- [x] `api-gateway` submodule (Spring Cloud Gateway Server MVC) with Eureka load-balanced routes (`RouterConfig`)
- [x] Public route: `/auth/**` → `job-portal-user-service`
- [x] JWT-protected routes: `/api/users/**` (user-service), `/api/companies/**` (company-service), `/api/jobs/**`, `/api/job-categories/**`, `/api/job-skills/**`, `/api/job-tags/**` (job-service), `/api/applications/**` (application-service), `/api/resumes/**` (resume-service), `/api/preferences/**` (preferences-service)
- [x] Admin route `/api/admin/**` → user-service, requires JWT and `ROLE_ADMIN`
- [x] Gateway JWT filter validates the token and forwards `X-User-Id`, `X-User-Email` and `X-User-Role` to the services
- [x] Tested all APIs through the gateway successfully
- [ ] **Gateway header spoofing:** the filter uses `.header(...)`, which adds to headers instead of replacing them, so a client can still send its own `X-User-Id` / `X-User-Email` / `X-User-Role`; remove the incoming copies and set the verified values (`.headers(h -> { h.remove(..); h.set(..); })`)
- [ ] Role checks for admin actions that are reachable under normal routes: `/api/users` (list), `/api/users/{id}/suspend|activate`, `DELETE /api/users/{id}`, `/api/companies/{id}/verify|deactivate`, `/api/jobs/admin`, category / skill / tag create-update-delete; add role rules per route / method at the gateway (and keep `@PreAuthorize` checks in the services)
- [ ] `/api/admin/**` currently routes to user-service but no controller uses that prefix; move the admin endpoints there or remove the route
- [ ] Preferences route uses the service id `job-portal-job-preferences`; make it match the registered `spring.application.name` of the preferences service (the other services use `job-portal-<name>-service`)
- [ ] Make `requireRole` an exact role match (it uses `contains` on a comma-separated string)
- [ ] CORS configuration at the gateway for the React frontend
- [ ] Share the JWT secret between user-service and gateway through configuration / env var, not a hardcoded constant; services must not be reachable directly (only through the gateway) in Docker / production
- [ ] Gateway resilience: Resilience4j circuit breaker / fallbacks and rate limiting

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
- [x] `ResumeServiceImpl` (`@Transactional`): create (defaults for template, visibility, score), get by id (owner-checked), get my resumes, update personal info (partial update), update summary, set default, soft delete
- [x] `ResumeMapper` (null-safe) and a full `ResumeResponse` that includes work experience, education, skills, projects, certifications, awards and languages
- [x] `ResumeController` (`/api/resumes`): create, get by id, my resumes, update personal info, update summary, set default, delete
- [x] Work experience section: entity, `AddWorkExperience` / `WorkExperienceResponse`, mapper, repository, service, `WorkExperienceController`
- [x] Skills section: `ResumeSkills` entity, `AddResumeSkillRequest` / `ResumeSkillResponse`, repository, service, `ResumeSkillController` (`/api/resumes/{resumeId}/skills`)
- [x] Education section: entity, `AddEducationRequest` / `EducationResponse`, repository, service, `EducationController` (`/api/resumes/{resumeId}/educations`)
- [x] Projects section: entity, `AddProjectRequest` / `ProjectResponse`, repository, service, `ProjectController` (`/api/resumes/{resumeId}/projects`)
- [x] Certifications section: entity, `AddCertificationRequest` / `CertificationResponse`, `CertificationRepository`, `CertificationService` / `CertificationServiceImpl` (owner check, resume-match check, expiry date validation, `noExpiration` handling), `CertificationController` (`/api/resumes/{resumeId}/certifications`)
- [x] Awards section: entity, `AddAwardRequest` / `AwardResponse`, `AwardRepository`, `AwardService`, `AwardController` (`/api/resumes/{resumeId}/awards`)
- [x] Languages section: `Language` entity, `LanguageProficiency` enum, `AddLanguageRequest` / `LanguageResponse`, `LanguageRepository`, included in the full resume response
- [x] Tested the resume APIs successfully
- [x] Fixed the resume-service issues reported earlier (mapper null check, create defaults, `@Transactional`, section validations and builder defaults, skills table name, and the other items from the section reviews)
- [ ] Confirm `LanguageService` / `LanguageController` (`/api/resumes/{resumeId}/languages`) are in place (only the request DTO, entity usage and repository were shared)
- [ ] Read access: the `GET` list endpoints (awards and certifications controllers shown) take no `X-User-Id`, so anyone can read any resume's sections; require the owner or enforce `visibility`
- [ ] `getResumeEntity()` still ignores `isActive`, so soft-deleted resumes can be fetched, edited or extended with new sections
- [ ] `updatePersonalInfo` still takes `PersonalInfoResponse`; add a validated `PersonalInfoRequest` (email, URLs)
- [ ] Refactor `assertOwner()` (still duplicated per service) into one `getOwnedResume(resumeId, candidateId)` in `ResumeService`
- [ ] `buildFullResponse()` runs 7 section queries per resume, so `getMyResumes()` repeats that for every resume; fetch sections in bulk or return a lighter list response
- [ ] Completion score calculation (still always `0`)
- [ ] Enforce `visibility` (public / link-only sharing; default is `PUBLIC`) and employer access to a candidate's resume when applying
- [ ] Link resume skills to the job-service skill catalog so they can be matched against job skills
- [ ] PDF export / template rendering (optional: upload existing resume + AI parsing via `ai-service`)
- [ ] Replace `X-User-Id` header with JWT / gateway user id

### Application Service (new module — `job-portal-application-service`)
- [x] Module created (`JobPortalApplicationServiceApplication`)
- [x] `Application` entity (candidate, job, company, employer, resume, status, cover letter, expected salary, available-from, starred flag, AI score / shortlist status, withdrawn info, timestamps)
- [x] `ApplicationNote` entity (internal recruiter notes)
- [x] Enums: `ApplicationStatus` (`PENDING`, `REVIEWING`, `SHORTLISTED`, `INTERVIEW_SCHEDULE`, `REJECTED`, `HIRED`, `WITHDRAWN`), `AiShortlistStatus`
- [x] DTOs: `CreateApplicationRequest`, `UpdateApplicationStatusRequest`, `WithdrawApplicationRequest`, `CompanyApplicationFilterRequest`, `AddApplicationNoteRequest`, `ApplicationResponse`, `ApplicationNoteResponse`
- [x] `ApplicationRepository` (+ `JpaSpecificationExecutor`), `ApplicationNoteRepository`, `ApplicationSpecification` (company filters: job, status, starred, AI shortlist status, min AI score)
- [x] `ApplicationMapper`
- [x] `ApplicationServiceImpl`: apply (duplicate check), get by id, my applications, applications for a job, company applications with filters and AI-score sorting, update status (employer-checked), withdraw, toggle star, delete
- [x] `ApplicationNoteServiceImpl`: add, list, delete notes (employer-checked on add/delete)
- [x] `ApplicationController` (`/api/applications`): apply, get by id, my, company list, update status, withdraw, star, delete
- [x] `ApplicationNoteController` (`/api/applications/{applicationId}/notes`): add (201), list, delete
- [x] Tested the application APIs successfully
- [ ] Replace hardcoded `companyId = 1L` / `employerId = 1L` (create and company list): fetch the job from job-service, reject missing / non-`OPEN` / past-deadline jobs, and verify the resume belongs to the candidate (resume-service) — OpenFeign + Resilience4j
- [ ] `buildFullResponse()` uses placeholders: `JobResponse` is built with the **application id** instead of `jobId`, and company / candidate contain only ids; load real data from job, company and user services
- [ ] Privacy: internal recruiter notes are included in `ApplicationResponse` for everyone (candidate `/my` and `/{id}` too), `GET .../notes` has no employer check, and `GET /api/applications/{id}` has no access check; show notes to employers only and verify the caller is the candidate or the job's employer
- [ ] `ApplicationNoteServiceImpl.deleteNote()` doesn't check the note belongs to the `applicationId` in the URL
- [ ] `ApplicationSpecification` mismatch: parameter is a primitive `boolean isStarred` (NPE when `isStarted` isn't sent) and it filters on `"isStarred"` while the entity field is `isStarted`; rename the field to `isStarred` everywhere and use `Boolean`
- [ ] `ApplicationMapper.toNoteResponse()` sets `id` from `addedByUserId`; use `note.getId()`. Rename `createdAtt` → `createdAt` in `ApplicationNote`
- [ ] `Application` entity: `isStarted` is null on new applications (builder ignores initializers), so `toggleStar` throws NPE; add `@Builder.Default` (also `status`, `aiShortlistStatus` default `NOT_SCREENED`) and `@Enumerated(EnumType.STRING)` on `aiShortlistStatus`
- [ ] `coverLetter` is validated up to 3000 characters but the column is varchar(255); use `TEXT` (also `withdrawnReason`)
- [ ] Status rules: `UpdateApplicationStatusRequest.status` needs `@NotNull`; add valid transitions (no changes after `HIRED` / `REJECTED` / `WITHDRAWN`, employers can't set `WITHDRAWN`)
- [ ] `withdraw()`: set `withdrawnAt`, reject already withdrawn / hired / rejected applications, make the request body optional
- [ ] `deleteApplication` endpoint is mapped as `@PatchMapping("/{id}")` with a request body; use `@DeleteMapping("/{id}")` without a body, and decide whether delete should be allowed (notes reference the application, so deleting one with notes fails on the foreign key)
- [ ] Add a unique constraint on (`candidateId`, `jobId`) so concurrent requests can't create duplicates; decide whether re-applying after withdrawing is allowed
- [ ] Return `201 Created` from `POST /api/applications`; add pagination for list endpoints; avoid per-application note queries (N+1)
- [ ] Add `aiScore` and `aiShortlistStatus` to `ApplicationResponse`; remove the unused `java.awt.print.Pageable` import in `ApplicationRepository`
- [ ] AI screening: score resumes against jobs with Gemini via `ai-service` and set `aiScore` / `aiShortlistStatus`
- [ ] Kafka events: `application.submitted`, `application.status.changed` (consumed by notification-service)
- [ ] Replace `X-User-Id` header with JWT / gateway user id, and role checks (candidate vs employer)

### Preferences Service (new module — `job-portal-preferences-service`)
- [x] Module created (`JobPortalPreferencesServiceApplication`)
- [x] Saved jobs: `SavedJob` entity (candidateId, jobId, savedAt), `SavedJobRequest` / `SavedJobResponse`, `SavedJobRepository`, `PreferenceMapper`, `SavedJobService` / `SavedJobServiceImpl` (save with duplicate check, unsave with ownership check, list, is-saved)
- [x] `SavedJobController` (`/api/preferences/saved-jobs`): save, list, check, delete, plus a `/test` endpoint
- [ ] **Fix argument order in `SavedJobController.unsavedJob`:** the service signature is `(candidateId, savedJobId)` but the controller passes `(savedJobId, candidateId)`, so unsave looks up the wrong record
- [ ] Validate the request: add `@NotNull` on `SavedJobRequest.jobId` and `@Valid` in the controller (a null `jobId` currently causes a DB error)
- [ ] Verify the job exists (job-service) before saving, and return job details with saved jobs instead of only ids (OpenFeign + Resilience4j)
- [ ] Add a unique constraint on (`candidateId`, `jobId`) and an index on `candidateId`; return `201 Created` from the save endpoint; pagination for the list
- [ ] Remove the `/test` endpoint once the service is verified
- [ ] Test the saved-jobs APIs
- [ ] Other preference features (to be decided, e.g. preferred roles / locations / salary, job alerts)
- [ ] Replace `X-User-Id` header with JWT / gateway user id

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
- [ ] Resilience4j (circuit breaker on gateway → services; also on OpenFeign calls between services)
- [ ] Centralized logging/tracing (optional: ELK / Zipkin)
- [ ] Global exception handling in `common-lib`
- [ ] Common DTOs/enums in `common-lib` (currently empty) — `ApiResponse` and enums like `JobType` are now duplicated across job-service and resume-service; move shared ones here

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
- Added the work experience section to `job-portal-resume-service`:
  - `WorkExperience` entity (company, title, employment type, location, dates, current-job flag, description, technologies, display order) linked to `Resume`.
  - `AddWorkExperience`, `WorkExperienceResponse`, `WorkExperienceMapper`, `WorkExperienceRepository` (ordered by `displayOrder`).
  - `WorkExperienceServiceImpl`: add, list, update and delete with ownership checks through the resume.
  - `WorkExperienceController` (`/api/resume/{resumeId}/work-experiences`) with add, list, update and delete endpoints.
- Added the skills section to `job-portal-resume-service`:
  - `ResumeSkills` entity (skill name, proficiency level, years of experience, display order) linked to `Resume`.
  - `AddResumeSkillRequest`, `ResumeSkillResponse`, `ResumeSkillRepository` (ordered by `displayOrder`), mapping in `ResumeMapper.toSkillResponse`.
  - `ResumeSkillServiceImpl`: add, list, update and delete with ownership checks through the resume.
  - `ResumeSkillController` (`/api/resumes/{resumeId}/skills`) with add (201), list, update and delete endpoints.
- Added the education section to `job-portal-resume-service`:
  - `Education` entity (institution, degree, field of study, grade, dates, currently-studying flag, description, display order) linked to `Resume`.
  - `AddEducationRequest`, `EducationResponse`, `EducationRepository` (ordered by `displayOrder`), mapping in `ResumeMapper.toEducationResponse`.
  - `EducationServiceImpl`: add, list, update and delete with ownership checks through the resume.
  - `EducationController` (`/api/resumes/{resumeId}/educations`) with add, list, update and delete endpoints.
- Added the projects section to `job-portal-resume-service`:
  - `Projects` entity (title, description, technologies, project URL, source code URL, dates, ongoing flag, display order) linked to `Resume`.
  - `AddProjectRequest` (with http/https URL validation), `ProjectResponse`, `ProjectRepository` (ordered by `displayOrder`), mapping in `ResumeMapper.toProjectResponse`.
  - `ProjectServiceImpl`: add, list, update and delete with ownership checks through the resume.
  - `ProjectController` (`/api/resumes/{resumeId}/projects`) with add, list, update and delete endpoints.
- **To fix next:**
  - `generateUniqueSlug()` in the category, skill and tag services still has the inverted `existsBySlug` check, so slugs come out as `java-1` instead of `java` and a duplicate slug can slip through (e.g. "C" and "C++"). Fix it once in a shared `SlugUtil`.
  - `JobSpecification` still needs updating for the new relations (`category.id`, `skillIds`, `tagIds`, `keyword`) and the lowercase `location` pattern.
  - `companyId` is still hardcoded to `1L` with an empty `CompanyResponse`; call company-service (OpenFeign + Resilience4j).
  - `JobMapper` should null-check `job.getCategory()`; `getSkillByIds()` / `getTagsByIds()` should fail on unknown ids and `Job` should use a mutable `HashSet` instead of `Collections.emptySet()`.
  - Admin-only checks on category/skill/tag endpoints and `/api/jobs/admin`, `X-User-Id` replaced by JWT / gateway user id, pagination, and tag delete strategy.
  - Duplicated code across services (`ApiResponse`, `JobType`, etc.): move shared pieces into `common-lib`.

### 2026-10-16
- Added the certifications, awards and languages sections to `job-portal-resume-service`:
  - Certifications: `AddCertificationRequest`, `CertificationResponse`, `CertificationRepository`, `CertificationServiceImpl` (owner check, resume-match check, expiry-before-issue validation, `noExpiration` clears the expiry date), `CertificationController` (`/api/resumes/{resumeId}/certifications`, 201 on add).
  - Awards: `AddAwardRequest`, `AwardResponse`, `AwardRepository`, `AwardService`, `AwardController` (`/api/resumes/{resumeId}/awards`, 201 on add).
  - Languages: `Language` entity, `LanguageProficiency` enum, `AddLanguageRequest`, `LanguageResponse`, `LanguageRepository`.
- `ResumeServiceImpl.buildFullResponse()` now loads all seven sections and `ResumeMapper.toResponse()` returns them in `ResumeResponse` (work experience, education, skills, projects, certifications, awards, languages).
- Fixed the resume-service issues reported earlier (null-safe mapper, create defaults, `@Transactional`, section validations and builder defaults, skills table name, etc.).
- Tested the resume APIs successfully.
- **To fix next:**
  - `GET` list endpoints for sections (awards and certifications shown) have no owner check; enforce ownership or `visibility`.
  - `getResumeEntity()` ignores `isActive`, so deleted resumes are still reachable.
  - `updatePersonalInfo` still uses `PersonalInfoResponse` as its request body.
  - `assertOwner()` is still copy-pasted in each service.
  - `buildFullResponse()` does 7 queries per resume, and `getMyResumes()` repeats it per resume.
  - Completion score is always 0; `visibility` is not enforced yet; confirm the language service and controller.

### 2026-10-17
- Built `job-portal-application-service`:
  - `Application` and `ApplicationNote` entities, `ApplicationStatus` and `AiShortlistStatus` enums.
  - `CreateApplicationRequest`, `UpdateApplicationStatusRequest`, `WithdrawApplicationRequest`, `CompanyApplicationFilterRequest`, `AddApplicationNoteRequest`, `ApplicationResponse`, `ApplicationNoteResponse`, `ApplicationMapper`.
  - `ApplicationRepository`, `ApplicationNoteRepository` and `ApplicationSpecification` (filters by job, status, starred, AI shortlist status, minimum AI score; sorting by applied date or AI score).
  - `ApplicationServiceImpl`: apply (duplicate check), get by id, my applications, applications for a job / company, update status, withdraw, toggle star, delete.
  - `ApplicationNoteServiceImpl`: add, list, delete recruiter notes.
  - `ApplicationController` (`/api/applications`) and `ApplicationNoteController` (`/api/applications/{applicationId}/notes`).
- Tested the application APIs successfully.
- Created `job-portal-preferences-service` with saved jobs:
  - `SavedJob` entity, `SavedJobRequest`, `SavedJobResponse`, `SavedJobRepository`, `PreferenceMapper`.
  - `SavedJobServiceImpl`: save (duplicate check), unsave (ownership check), list, is-saved.
  - `SavedJobController` (`/api/preferences/saved-jobs`) with save, list, check, delete and a `/test` endpoint.
- Set up the API gateway (Spring Cloud Gateway Server MVC) in `job-portal-cloud`:
  - `RouterConfig` with Eureka load-balanced routes for user, company, job, application, resume and preferences services.
  - Public `/auth/**` route; JWT-protected routes for all `/api/**` paths; `/api/admin/**` requires `ROLE_ADMIN`.
  - JWT filter validates the token and forwards `X-User-Id`, `X-User-Email`, `X-User-Role` to the services.
  - Tested all APIs through the gateway successfully.
- Set up service discovery:
  - Created the Eureka Server module (`job-portal-registry-service`) in `job-portal-cloud`.
  - Set up Eureka client registration for all services (user, company, job, resume, application, preferences).
- **To fix next:**
  - Gateway: client-supplied `X-User-*` headers are not removed before the verified ones are added (spoofable), admin-only endpoints are reachable by any authenticated user through the normal routes, the preferences route id (`job-portal-job-preferences`) doesn't follow the naming of the other services, no CORS yet, and the JWT secret must be shared via config.
  - Confirm all services register and show as `UP` in the Eureka dashboard; then replace hardcoded ids and placeholder data with service-to-service calls by service name.
  - `SavedJobController.unsavedJob` passes `(savedJobId, candidateId)` but the service expects `(candidateId, savedJobId)`: the arguments are swapped. Also missing `@NotNull` / `@Valid` on `jobId`, no job existence check, no unique constraint on (`candidateId`, `jobId`).
  - Hardcoded `companyId` / `employerId` (`1L`) and placeholder job / company / candidate in responses (job is built from the application id); integrate job, company, user and resume services.
  - Internal notes are returned to candidates and the notes list / application detail have no access checks; `deleteNote` doesn't check the note belongs to the application.
  - `isStarted` / `isStarred` mismatch breaks the starred filter and can NPE when the filter is omitted; `toggleStar` NPEs on new applications (null default); `toNoteResponse` uses the wrong id.
  - `coverLetter` needs a `TEXT` column (validated to 3000 characters, column is 255); status transition rules, `withdrawnAt`, delete endpoint mapping (`@PatchMapping` with a body), unique (`candidateId`, `jobId`) constraint.
  - AI screening and Kafka events are still to do.

<!-- Add new dated entries above this line as you make progress -->