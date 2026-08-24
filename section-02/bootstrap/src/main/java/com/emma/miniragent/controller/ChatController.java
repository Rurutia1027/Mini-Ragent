package com.emma.miniragent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private static final long SSE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestBody ChatStreamRequest request) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            emitter.completeWithError(new IllegalArgumentException("message is required"));
            return emitter;
        }

        chatClient.prompt()
                .messages(toHistory(request.getHistory()))
                .user(request.getMessage())
                .stream()
                .content()
                .subscribe(
                        token -> send(emitter, "message", token),
                        error -> {
                            log.warn("Chat stream failed", error);
                            emitter.completeWithError(error);
                        },
                        () -> {
                            send(emitter, "done", "[DONE]");
                            emitter.complete();
                        });

        emitter.onTimeout(emitter::complete);
        emitter.onCompletion(() -> log.debug("SSE completed"));
        return emitter;
    }

    private List<Message> toHistory(List<ChatStreamRequest.ChatMessageDto> history) {
        List<Message> messages = new ArrayList<>();
        if (history == null) {
            return messages;
        }
        for (ChatStreamRequest.ChatMessageDto item : history) {
            if (item.getContent() == null || item.getContent().isBlank() || item.getRole() == null) {
                continue;
            }
            Message message = switch (item.getRole().toLowerCase()) {
                case "system" -> new SystemMessage(item.getContent());
                case "user" -> new UserMessage(item.getContent());
                case "assistant" -> new AssistantMessage(item.getContent());
                default -> null;
            };
            if (message != null) {
                messages.add(message);
            }
        }
        return messages;
    }

    private void send(SseEmitter emitter, String event, String data) {
        try {
            emitter.send(SseEmitter.event().name(event).data(data));
        } catch (IOException ex) {
            log.warn("Failed to send SSE event {}", event, ex);
            emitter.completeWithError(ex);
        }
    }
}
