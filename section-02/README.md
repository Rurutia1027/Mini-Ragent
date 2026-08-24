# Section 02 - User auth + protected SSE chat

Standalone mini project: add registration/login, a PostgreSQL user table, Redis tokens, and API interception on top of
Section 01 streaming chat.

## Prerequisites

```bash
# Start Postgres + Redis 
cd /Users/emma/LLM/mini-proj/Mini-Ragent/section-02/ 
docker compose up -d 

# Ollama (same as section-01)
ollama serve 
```

## Start

```bash
# Terminal 1 - backend :9090
cd section-02/
./mvnw -pl bootstrap -am package -DskipTests
./mvnw -pl bootstrap spring-boot:run 
```

# Terminal 2 — frontend :5173

```
cd frontend && npm install && npm run dev
```

Open http://127.0.0.1:5173

Default account: `admin` / `admin123` (seeded on startup)

## Layout

| Module       | Responsibility                                                  |
|--------------|-----------------------------------------------------------------|
| `framework/` | `Result`, `ClientException`, global exception handling          |
| `infra-ai/`  | Spring AI Ollama `ChatClient` (same as section-01)              |
| `idm/`       | Register/login, BCrypt, TokenStore, auth interceptor            |
| `bootstrap/` | Entry point, `ChatController`, `application.yaml`, `schema.sql` |
| `frontend/`  | Login page + localStorage token + protected chat                |

## API

- `POST /api/auth/register` `{username, password}`
- `POST /api/auth/login` → `{code, data:{token, username}}`
- `POST /api/auth/logout` (Bearer)
- `POST /api/chat/stream` (requires `Authorization: Bearer <token>`)

Without Redis, set `auth.token-store: memory` in `application.yaml`.
