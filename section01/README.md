# Section 01 - Ollama SSE streaming chat 

Standalone mini project: the smallest Spring Boot + React + local Ollama stack. 

## Start 

```bash
# Terminal 1 
ollama serve # if not already running 
# Confirm the model: ollama list (default llama3.2:latest; change in application.yaml)

# Terminal 2 
cd /section-01
./mvnw -pl bootstrap -am install -DskipTests
./mvnw -pl bootstrap spring-boot:run 

# Terminal 3 
cd frontend && npm install && npm run dev 
```

Then, open http://127.0.0.1:5173

## Layout 
- `infra-ai` -- OllamaChatService
- `bootstrap/` -- ChatController SSE 
- `frontend/` -- chat UI

