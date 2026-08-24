# Section 1: Local LLM calls and SSE streaming chat

## Learning goals

1. Use Spring AI as the standard way to talk to a chat model
2. Call Ollama for local inference without a hand-rolled HTTP client
3. Implement SSE (Server-Sent Events) streaming
4. Build a minimal Spring Boot + React full-stack chat app

## Core concepts

### 1. Spring AI ChatClient

`infra-ai` exposes a `ChatClient` bean. Spring AI's Ollama starter binds `spring.ai.ollama.*` and implements the chat
model. The HTTP protocol is an implementation detail of the starter, not application code.

```java
chatClient.prompt()
        .messages(history)
        .user(message)
        .stream()
        .content()
        .subscribe(...);
```

Configure the model in `application.yaml`:

```yaml
spring:
  ai:
    model:
      chat: ollama
      embedding: none
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3.2:latest
```

### 2. SSE streaming

The model generates tokens one by one; the server pushes them to the frontend via SSE:

```
event: message
data: Hello

event: message
data: , I am

event: done
data: [DONE]
```

Spring Boot uses `SseEmitter`; the frontend parses with `fetch` + `ReadableStream`.

### 3. Module layering

| Module      | Responsibility                                                          |
|-------------|-------------------------------------------------------------------------|
| `infra-ai`  | Spring AI Ollama starter + `ChatClient` bean                            |
| `bootstrap` | HTTP entry point; maps history to Spring AI `Message`s and forwards SSE |

## Mapping to Ragent source

- [
  `OllamaChatClient.java`](/Users/emma/LLM/ragent/infra-ai/src/main/java/com/nageoffer/ai/ragent/infra/chat/OllamaChatClient.java)
- Mini Ragent replaces that custom client with Spring AI 2.0.

## Acceptance criteria

- [ ] Ollama health check succeeds
- [ ] Browser chat streams correctly
- [ ] Disconnect / timeout is handled gracefully
- [ ] `./mvnw -pl bootstrap -am package` builds successfully

## FAQ

**Q: Model not found?**
Set `spring.ai.ollama.chat.options.model` in `application.yaml` to a model you already have locally (`ollama list`).

**Q: Connection refused?**
Confirm `ollama serve` is running; default port is 11434.

**Q: Responses are slow?**
The first load of a local small model takes time; you can switch to a smaller one such as `llama3.2:3b`.
