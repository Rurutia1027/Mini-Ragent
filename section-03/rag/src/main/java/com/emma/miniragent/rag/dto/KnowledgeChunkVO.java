package com.emma.miniragent.rag.dto;

import java.time.LocalDateTime;

public record KnowledgeChunkVO(
        Long id,
        Long documentId,
        int chunkIndex,
        String content,
        LocalDateTime createdAt) {
}
