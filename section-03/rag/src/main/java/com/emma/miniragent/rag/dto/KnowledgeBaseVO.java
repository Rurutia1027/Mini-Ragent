package com.emma.miniragent.rag.dto;

import java.time.LocalDateTime;

public record KnowledgeBaseVO(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
