package com.emma.miniragent.rag.model;

import java.time.LocalDateTime;

public record KnowledgeChunk(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}