# Inventory Management

Spring Boot project for an inventory management system using PostgreSQL, Spring Data JPA, Spring Security, and Thymeleaf.

This branch currently includes project setup, database configuration, and GitHub Actions CD workflow for Render deployment.

## Tech Stack

| Tool | Version |
| --- | --- |
| Spring Boot | 4.0.3 |
| Java | 21 |
| PostgreSQL | 15 |
| Maven Wrapper | 3.9.x |
| Thymeleaf | via Spring Boot starter |
| Spring Security | via Spring Boot starter |

## Prerequisites

- Java 21
- Docker Desktop
- Git

## Environment Variables

Create a `.env` file in the project root:

```dotenv
DB_USER=admin
DB_PASSWORD=adminpassword123
```

## Run Locally

1. Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

2. Run the Spring Boot app:

```bash
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Test

```bash
./mvnw test
```

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## Docker Compose

`compose.yaml` currently runs PostgreSQL service:

- Container name: `inventory_db`
- Database: `inventory_mgmt`
- Port mapping: `5432:5432`
- Persistent volume: `postgres_data`

## CI/CD

### CD Workflow (Render)

This repository includes a GitHub Actions workflow at:

- `.github/workflows/cd.yml`

Behavior:

- Triggers on push to `main`
- Calls Render deploy hook URL with `POST`

Required GitHub secret:

- `RENDER_DEPLOY_HOOK_URL`

If this secret is missing, the workflow fails with a clear error.

## Current Status

- Project skeleton and core dependencies are configured.
- Database connectivity settings are configured in `src/main/resources/application.yaml`.
- Automated CD trigger to Render is configured.

## Next Steps

- Add/merge full application modules (entities, services, controllers) if working from feature branches.
- Add a dedicated CI workflow (`ci.yml`) for build and test on push/PR.
- Add deployment URL and API usage examples after service is live.

