# Mini Ragent Proj

Split [Ragent](../ragent) into **10 independent, copy-and-accumulate** full-stack mini projects.

Each section is a complete, runnable project. When you move to the next section, copy the previous directory in full and add the new focus — rather than stacking features in a single repo.

## Contents

| Directory | Focus | New dependencies |
|-----------|--------|------------------|
| [section-01](section-01/) | Ollama SSE streaming chat | Ollama |
| [section-02](section-02/) | Auth skeleton (Sa-Token style) | PostgreSQL + Redis |
| [section-03](section-03/) | Knowledge base + document chunking | — |
| [section-04](section-04/) | Embedding + pgvector | pgvector |
| [section-05](section-05/) | Basic RAG Q&A | — |
| [section-06](section-06/) | Rewrite / intent / memory | — |
| [section-07](section-07/) | Hybrid retrieval + RRF | — |
| [section-08](section-08/) | Ingestion pipeline | — |
| [section-09](section-09/) | ReAct Agent + MCP | MCP Server |
| [section-10](section-10/) | Trace / rate limiting / deploy | Docker Compose |

## How to learn

1. Open `section-0N` and read `docs/FOCUS.md`
2. Follow that section's `README.md` to start and verify
3. Compare with the next section (or run `diff -ru section-0N section-0(N+1)` yourself)

## AI convention

**Use local Ollama only.** Do not configure cloud API keys.

```bash
ollama pull llama3.2:latest
# From section-04 onward you also need an embedding model; see each section README
```

## Source reference

Original project: `/Users/emma/LLM/ragent` (read-only reference).

## Accumulation rule

```
section-N = copy(section-(N-1)) + this section's focus code
```
