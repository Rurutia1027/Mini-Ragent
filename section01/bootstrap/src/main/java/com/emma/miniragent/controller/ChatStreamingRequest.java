package com.emma.miniragent.controller;

import java.util.ArrayList;
import java.util.List;

public class ChatStreamingRequest {
    private String message;
    private List<ChatMessageDto> history = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChatMessageDto> getHistory() {
        return history;
    }

    public void setHistory(List<ChatMessageDto> history) {
        this.history = history != null ? history : new ArrayList<>();
    }

    public static class ChatMessageDto {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
