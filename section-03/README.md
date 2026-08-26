# Section 03 - Knowledge base + document chunking

On top of Section 02 auth, add knowledge-base management, document upload, and text chunking.

## Prerequisites

```bash 
cd section-03
docker compose up -d 
ollama serve 
```

## Start

```bash
# Terminal 1 - backend :9090
./mvnw -pl bootstrap -am package -DskipTests 
./mvnw -pl bootstrap spring-boot:run 


# Terminal 2 - frontend :5173 
cd frontend && npm install && npm run dev  
```

Open http://127.0.0.1:5173 ; default account `admin` / `admin123`

## Layout

| Module       | Responsibility                        |
|--------------|---------------------------------------|
| `framework/` | Unified response, exception handling  |
| `infra-ai/`  | Spring AI Ollama `ChatClient`         |
| `idm/`       | Auth & Identification                 |
| `rag/`       | Knowledge-base CRUD, upload, chunking |
| `bootstrap/` | Entry point, schema                   |
| `frontend/`  | Login + knowledge-base admin + chat   |

## API

- `GET/POST/PUT/DELETE /api/knowledge/bases` — knowledge-base CRUD
- `GET /api/knowledge/bases/{kbId}/documents` — document list
- `POST /api/knowledge/bases/{kbId}/documents` — upload `.txt`/`.md` (multipart `file`)
- `GET /api/knowledge/documents/{documentId}/chunks` — chunk list

Uploaded files are stored at `./data/uploads/{kbId}/`.

## Chunking strategy

`TextChunker` wraps Spring AI `TokenTextSplitter` (`rag.chunk-size` default 500, `rag.chunk-overlap` default 50). 