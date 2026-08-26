package com.emma.miniragent.rag.model;

import java.time.LocalDateTime;

public record KnowledgeDocument(
        Long id,
        Long kbId,
        String filename,
        String contentType,
        String filePath,
        String status,
        int chunkCount,
        LocalDateTime createdAt) {
}