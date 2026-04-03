# BridgeSkill API

BridgeSkill API is the backend service for the BridgeSkill platform, a full-stack job and internship matching system for **students**, **clients**, and **admins**.

It provides authentication, role-based access control, profile management, job posting, job applications, and admin monitoring.

## Project Overview

BridgeSkill solves a practical problem: students need a place to find opportunities and apply easily, while companies need a simple way to post jobs and review applicants.

This backend handles:
- user registration and login
- JWT authentication
- role-based authorization
- student profile management
- client company profile management
- job CRUD
- job application flow
- admin dashboard summary

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT (JJWT)
- Maven
- Lombok

## Roles

The system uses 3 roles:
- **ADMIN**
- **STUDENT**
- **CLIENT**

Seeded role IDs in the database:
- `1 = ADMIN`
- `2 = STUDENT`
- `3 = CLIENT`

## Main Features

### Authentication
- Register new student or client account
- Login with email and password
- Get current authenticated user with `/api/auth/me`
- Passwords are hashed before storing
- JWT is required for protected routes

### Student Features
- Create student profile
- View own profile
- Update own profile
- Browse jobs
- Apply to jobs
- View own applications

### Client Features
- Create client profile
- View own company profile
- Update own company profile
- Create job posts
- Update own jobs
- Close own jobs
- View applications for own jobs
- Update application status

### Admin Features
- View admin monitoring summary
- View total users, jobs, and applications
- View recent jobs and recent applications

## Database Design

Main tables:
- `roles`
- `users`
- `student_profiles`
- `client_profiles`
- `jobs`
- `applications`

Important relationships:
- `users.role_id -> roles.id`
- `student_profiles.user_id -> users.id`
- `client_profiles.user_id -> users.id`
- `jobs.client_id -> users.id`
- `applications.job_id -> jobs.id`
- `applications.student_id -> users.id`

Important constraint:
- one student can apply to the same job only once through unique constraint:
  - `(job_id, student_id)`

## Project Structure

```text
src/main/java/com/aditi_final/bridgeskill_api
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
└── service
```

## API Modules

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

### Jobs
- `GET /api/jobs`
- `GET /api/jobs/{id}`
- `POST /api/jobs`
- `PUT /api/jobs/{id}`
- `DELETE /api/jobs/{id}`

### Student Profile
- `POST /api/student-profile`
- `GET /api/student-profile/me`
- `PUT /api/student-profile`

### Client Profile
- `POST /api/client-profile`
- `GET /api/client-profile/me`
- `PUT /api/client-profile`

### Applications
- `POST /api/applications`
- `GET /api/applications/me`
- `GET /api/applications/{id}`
- `GET /api/applications/client`
- `GET /api/applications/client/jobs/{jobId}`
- `PUT /api/applications/{id}/status`

### Admin Monitoring
- `GET /api/admin/monitoring/summary`

## Environment Variables

The backend uses the following environment variables:

```env
SERVER_PORT=8085
DB_URL=jdbc:postgresql://localhost:5434/bridgeskill
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your_secret_key_here
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

## Local Setup

### 1. Clone the project
```bash
git clone <your-backend-repository-url>
cd bridgeskill-api
```

### 2. Start PostgreSQL
You can use your local PostgreSQL or Docker.

Example Docker command:
```bash
docker run --name bridgeskill-postgres \
  -e POSTGRES_DB=bridgeskill \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5434:5432 \
  -d postgres:16
```

### 3. Configure environment
Set the required environment variables in your IDE or terminal.

### 4. Run the project
Using Maven wrapper:
```bash
./mvnw spring-boot:run
```

Or on Windows:
```bash
mvnw.cmd spring-boot:run
```

### 5. API base URL
```text
http://localhost:8085
```

## Flyway Migrations

Database schema is managed by Flyway migrations inside:

```text
src/main/resources/db/migration
```

Current migrations include:
- create roles table
- create users table
- create student profiles table
- create client profiles table
- create jobs table
- create applications table
- seed roles
- add foreign keys

## Security

This project uses JWT-based authentication with Spring Security.

Security features:
- stateless authentication
- protected routes by role
- BCrypt password hashing
- custom authentication entry point
- custom access denied handler
- global exception handling

## Default Admin Seeder

The project contains an admin seeder:
- `config/AdminSeeder.java`

Make sure its configured admin email and password match your local testing needs.

## Suggested Test Flow

### Student
1. Register as student
2. Login
3. Create student profile
4. View job list
5. Apply to a job
6. View own applications

### Client
1. Register as client
2. Login
3. Create client profile
4. Create a job
5. View own jobs
6. Review job applications
7. Update application status

### Admin
1. Login as admin
2. Open monitoring summary endpoint
3. Verify totals and recent activity

## Deployment Notes

Recommended deployment:
- Backend: Render, Railway, or Docker-based VPS
- Database: PostgreSQL
- Frontend CORS origin must be added to `CORS_ALLOWED_ORIGINS`

Before deployment, update:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ALLOWED_ORIGINS`

## Author Notes

This project was built as an individual full-stack development project with separate frontend and backend repositories.

The backend follows a standard layered architecture:
- controller for HTTP endpoints
- service for business logic
- repository for database access
- dto for request/response models
- security for JWT and RBAC

