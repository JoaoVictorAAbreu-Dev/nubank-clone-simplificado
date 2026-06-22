# Nubank Clone Simplificado

Fullstack banking demo built for portfolio purposes.

## Stack

- Java 21
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Redis
- React + TypeScript + Vite
- Docker + Docker Compose

## Features

- User registration and login
- JWT-protected account access
- Balance control
- Transfers and PIX fictício flow
- Statement persistence
- Redis-backed summary cache
- Swagger/OpenAPI documentation

## Structure

- `backend/` Spring Boot API
- `frontend/` React UI
- `docker-compose.yml` local infrastructure
- `.env.example` required environment variables

## Local setup

1. Copy `.env.example` to `.env`.
2. Start PostgreSQL and Redis:
   ```powershell
   docker compose up -d
   ```
3. Run the backend from `backend/`.
4. Run the frontend from `frontend/`.

## Notes

- The banking domain is fictional.
- Redis is used only for lightweight summary caching.
- The frontend is intentionally simple and focused on product flow.
