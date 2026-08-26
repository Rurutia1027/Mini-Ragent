# Section 03 focus: knowledge base + document chunking

## Relative to the previous section (Section 02)

Section 02 already has: user auth, Redis tokens, protected SSE chat.

**Added in this section:**

- Maven module `rag`
- PostgreSQL tables: `t_knowledge_base`, `t_knowledge_document`, `t_knowledge_chunk`.
- Knowledge-base CRUD API (`/api/knowledge/**`, requires Bearer token)
- Upload `.txt` / `.md` --> Read as UTF-8 --> Spring AI `TokenTextSplitter` (wrapped by `TextChunker`)
- Local file storage: `./data/uploads/{kbId}`
- Frontend knowledge-base admin: create a KB, upload documents, inspect chunks

## Core concepts

- **Knowledge base**: logical container for documents and chunks
- **Document**: original uploaded file path + metadata
- **Chunk**: Spring AI `TokenTextSplitter` with `rag.chunk-size` / `rag.chunk-overlap`
- **JdbcTemplate**: lightweight persistence, no MyBatis

## Mapping to Ragent

- `rag/knowledge/...` knowledge-base CRUD and document management
- `core/chunk/...` chunking strategies (this section simplifies to `TextChunker`)

## How to run 
See this section's `README.md`.