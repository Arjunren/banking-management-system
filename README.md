# Banking Management System

## Overview

A compact production-structured banking REST API for customer registration, accounts, deposits, withdrawals, transfers, transaction history, and dashboards.

## Features

- Customer registration, login, revocable logout, admin/customer roles
- Automatic primary account creation
- Owner-protected account access and paginated transaction history
- Deposits, withdrawals, and atomic transfers with deterministic pessimistic locking
- Overdraft prevention, immutable ledger entries, audit timestamps, health endpoint
- Flyway migrations, PostgreSQL, H2 tests, Docker, Maven Wrapper, CI, and Dependabot

## Technology Stack

Java 25, Spring Boot 4.1.1, Spring Web MVC, Spring Security, Spring Data JPA, Bean Validation, Flyway, PostgreSQL 17, H2 tests, Maven.

## Requirements

Java 25 and Docker/PostgreSQL 15+. Maven installation is optional because `mvnw` and `mvnw.cmd` are included.

## Installation

```powershell
Copy-Item .env.example .env
.\mvnw.cmd clean package
```

## Environment Variables

Set `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`. Optional variables are `TOKEN_TTL_MINUTES`, comma-separated `ALLOWED_ORIGINS`, and `SPRING_PROFILES_ACTIVE`. Never use example database credentials in production.

## Database Setup

Create the PostgreSQL database and start the application; Flyway applies `src/main/resources/db/migration/V1__init.sql` automatically. Docker Compose provisions the database automatically.

## Running the Application

```powershell
$env:SPRING_PROFILES_ACTIVE='dev'
.\mvnw.cmd spring-boot:run
```

Or run `docker compose up --build`. The API listens on `http://localhost:8080`; health is `/actuator/health`.

## Running Tests

```powershell
.\mvnw.cmd verify
```

## Default Development Accounts

Created only when the `dev` profile is active:

- Admin: `admin@example.com` / `AdminPassword123!`
- Customer: `customer@example.com` / `CustomerPassword123!`

These are local-development credentials and must be changed outside a disposable environment.

## API Endpoints

`POST /api/auth/register`, `POST /api/auth/login`, `POST /api/auth/logout`; `GET /api/accounts`, `GET /api/accounts/{id}`; `POST /api/accounts/{id}/deposit`, `POST /api/accounts/{id}/withdraw`; `POST /api/transfers`; `GET /api/accounts/{id}/transactions`; `GET /api/dashboard`; `GET /actuator/health`.

## Folder Structure

`controller/` handles HTTP, `service/` owns transactions and rules, `repository/` contains JPA access, `entity/` maps persistence, `dto/` defines explicit I/O, `security/` authenticates bearer tokens, `config/` configures security and dev seeds, `exception/` returns structured errors, and `db/migration/` owns schema history.

## Security Notes

Passwords use BCrypt cost 12. Bearer tokens are 256-bit random values; only SHA-256 hashes are stored and logout revokes them server-side. Login is rate-limited and performs dummy-hash work for unknown users. RBAC and account ownership are enforced in services. Inputs/page sizes are bounded, SQL is parameterized through JPA, CORS is allow-listed, stack traces are suppressed, and transfers lock accounts in stable ID order to prevent lost updates and deadlocks.

## Known Limitations

This portfolio scope omits interest, fees, statements, scheduled transfers, multi-currency, KYC, MFA, password recovery, and regulatory-grade immutable auditing. The login limiter is per application instance; production should use a shared limiter or gateway.
