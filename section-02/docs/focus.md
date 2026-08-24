# Section 02 focus: user auth

## Relative to the previous section (Section 01)

Section 01 already has: Spring AI `ChatClient` SSE streaming chat + React typewriter UI.

**Added in this section:**

- Maven modules `framework`, `idm`
- PostgreSQL `t_user` + spring-jdbc
- BCrypt (`spring-security-crypto` only)
- Redis token (TTL) or in-memory ConcurrentHashMap fallback
- `AuthInterceptor` protecting `/api/chat/**`
- Frontend login page + `localStorage` token
- `docker-compose.yml`: postgres:16 + redis:7
- Seed user `admin` / `admin123` on startup

## Core concepts

- Unified response `Result` + `ClientException` + `@RestControllerAdvice`
- Session token: login issues a UUID → Redis `SET` + TTL
- Bearer intercept: reject chat APIs when the token is missing or invalid
- Password hashing with BCrypt, without pulling in full Spring Security

## Mapping to Ragent

- `framework/.../Result`, `ClientException`, `GlobalExceptionHandler`
- `idm/.../AuthController`, Sa-Token sessions (this section uses a simpler homemade token to reduce dependencies)
- `framework/.../UserContext` (this section uses request attribute `auth.username`)

## How to run

See this section's `README.md`.
