# Mini Ragent Proj

Split [Ragent](https://github.com/nageoffer/ragent) into **10 independent, copy-and-accumulate** full-stack mini projects.

Each section is a complete, runnable project. When you move to the next section, copy the previous directory in full and add the new focus — rather than stacking features in a single repo.

## Contents

| Directory | Focus | New dependencies |
|-----------|--------|------------------|
| [section-01](section-01/) | Spring AI `ChatClient` + Ollama SSE chat | Ollama |
| [section-02](section-02/) | Auth skeleton (Sa-Token style) | PostgreSQL + Redis |
| [section-03](section-03/) | Knowledge base + `TokenTextSplitter` chunking | — |
| [section-04](section-04/) | Embedding + Spring AI PgVectorStore | pgvector |
| [section-05](section-05/) | Basic RAG Q&A (`ChatClient` + retrieve) | — |
| [section-06](section-06/) | Rewrite / intent / memory | — |
| [section-07](section-07/) | Hybrid retrieval + RRF | — |
| [section-08](section-08/) | Ingestion pipeline | — |
| [section-09](section-09/) | Agent + Spring AI `@Tool` | — |
| [section-10](section-10/) | Trace / rate limiting / deploy | Docker Compose |

## Intent 
**Ragent is the product reference. Spring AI is the implementation standard.**

Ragent already has a complete RAG/Agent path (chat, embeddings, retrieval, rewrite, hybrid search, ingestion, ReAct tools). It implements that path **by hand**: custom OpenAI-compatible HTTP clients, embedding calls, vector SQL, and JSON-in-text tool loops. 

This series keeps Ragent's **business shape** -- local Ollama, SSE chat, auth, knowledge-base admin, RAG, Q&A, hybrid retrieval, ingestion tasks-- but **does not copy that custom AI stack**. 
Chat, embeddings, chunking, vector store, query rewrite, and tool calling to through **Spring AI 2.0**. Application features that Spring AI does not own (auth, KB CRUD, ingestion task tables, keywords FTS + RRF) stays as ordinary Spring Boot code. 

The point of the rewrite is ** generality and a standard**, not a 1:1 port of Ragent internals. Spring AI still has rough edges; we still use it, so the tutorial matches how a Spring project would call models going forward. 

## Requirements 
- **JDK 26** (OpenJDK 26.0.1 or later)
- Maven 3.9.x (`./mvnw` in each section, or a local Maven 3.9.16+)
- Spring Boot **4.1.1** (official Java 26 support)
- **Spring AI 2.0.0** (Ollama chat/embedding, PgVectorStore, RAG transformers, `@Tool`)

## How to learn

1. Open `section-0N` and read `docs/FOCUS.md`
2. Follow that section's `README.md` to start and verify
3. Compare with the next section (or run `diff -ru section-0N section-0(N+1)` yourself)
4. When you want the original hand-rolled version, read Ragent — Mini Ragent is the Spring AI rewrite of the same story

## AI convention

**Use local Ollama only**, through **Spring AI 2.0** (`ChatClient`, `VectorStore`, `@Tool`). Do not configure cloud API keys. Do not add a second hand-rolled Ollama HTTP client next to Spring AI.


```bash
ollama pull llama3.2:latest
# From section-04 onward you also need an embedding model; see each section README
```

## Source reference

Original project: `[ragent](https://github.com/nageoffer/ragent)` (read-only reference for **business logic and product flow**, not for copying its custom LLM clients).

## Accumulation rule

```
section-N = copy(section-(N-1)) + this section's focus code
```

## CI

GitHub Actions (`.github/workflows/ci.yml`) compiles each `section*` backend with `./mvnw -pl bootstrap -am package -DskipTests` and builds each `frontend` with `npm ci && npm run build`. No Ollama, Postgres, or Redis is required for CI.

