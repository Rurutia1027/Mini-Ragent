package com.emma.miniragent.rag.dto;

import java.time.LocalDateTime;

public record KnowledgeDocumentVO(
        Long id,
        Long kbId,
        String filename,
        String contentType,
        String status,
        int chunkCount,
        LocalDateTime createdAt) {
}
