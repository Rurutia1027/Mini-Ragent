package com.emma.miniragent.rag.chunk;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.List;

/**
 * Spring AI {@link org.springframework.ai.transformer.splitter.TokenTextSplitter} wrapper
 * used at ingest time.
 */
public class TextChunker {
    private final TokenTextSplitter splitter;

    public TextChunker(int chunkSize, int chunkOverlap) {
        int size = Math.max(50, chunkSize);
        int minChars = Math.max(20, Math.min(size / 4, Math.max(1, size - Math.max(0, chunkOverlap))));
        this.splitter = TokenTextSplitter.builder()
                .withChunkSize(size)
                .withMinChunkSizeChars(minChars)
                .withKeepSeparator(true)
                .build();
    }

    public List<String> chunk(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return splitter.apply(List.of(new Document(text.trim()))).stream()
                .map(Document::getText)
                .filter(part -> part != null && !part.isBlank())
                .toList();
    }
}
