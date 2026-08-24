# Section 01 focus: local LLM + SSE streaming chat

## Relative to the previous section
None (this is the starting point).

## Core concepts
- Spring AI 2.0 `ChatClient` with the Ollama chat model
- Local inference with Ollama (`llama3.2:latest`)
- Spring `SseEmitter` streaming (`event: message` / `event: done`)
- React `fetch` + ReadableStream typewriter effect

## Mapping to Ragent
- Ragent: `OllamaChatClient` / `AbstractOpenAIStyleChatClient`
- Mini Ragent: `spring-ai-starter-model-ollama` + `ChatClient.prompt().stream()`

## How to run
See this section's `README.md`.