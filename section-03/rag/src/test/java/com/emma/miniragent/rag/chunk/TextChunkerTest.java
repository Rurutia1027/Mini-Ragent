package com.emma.miniragent.rag.chunk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextChunkerTest {

    @Test
    void chunk_nullOrBlank_returnsEmpty() {
        TextChunker chunker = new TextChunker(500, 50);

        assertTrue(chunker.chunk(null).isEmpty());
        assertTrue(chunker.chunk("").isEmpty());
        assertTrue(chunker.chunk("   ").isEmpty());
    }

    @Test
    void chunk_shortText_fitsInOneChunk() {
        TextChunker chunker = new TextChunker(500, 50);
        String text = "Hello, this is a short document for RAG.";

        List<String> chunks = chunker.chunk(text);

        assertEquals(1, chunks.size());
        assertEquals(text, chunks.get(0));
    }

    @Test
    void chunk_trimsSurroundingWhitespace() {
        TextChunker chunker = new TextChunker(500, 50);

        List<String> chunks = chunker.chunk("  hello world  ");

        assertEquals(1, chunks.size());
        assertEquals("hello world", chunks.get(0));
    }

    @Test
    void chunk_longText_splitsIntoMultipleChunks() {
        // Use a small chunkSize so the test stays readable.
        // size=50 tokens, overlap=10 -> minChars=max(20, min(12, 40))=20
        TextChunker chunker = new TextChunker(50, 10);
        String sentence = "Spring AI splits documents into token-sized chunks for retrieval. ";
        String longText = sentence.repeat(40);

        List<String> chunks = chunker.chunk(longText);

        assertTrue(chunks.size() > 1, "long text should produce multiple chunks");
        // Kept chunks should meet the minChars threshold (20 here).
        chunks.forEach(chunk -> assertTrue(chunk.length() >= 20, "chunk too short: " + chunk.length()));
        // Chunks should still come from the original text (no random content).
        String joined = String.join("", chunks);
        assertTrue(longText.contains(joined.substring(0, Math.min(50, joined.length()))));
    }

    @Test
    void chunk_defaultConfig_matchesProductionDefaults() {
        // Same defaults as RagProperties: chunkSize=500, chunkOverlap=50 -> minChars=125
        TextChunker chunker = new TextChunker(500, 50);
        String filler = "RAG knowledge base chunking demo sentence. ";
        String longText = filler.repeat(200);

        List<String> chunks = chunker.chunk(longText);

        assertTrue(chunks.size() > 1);
        chunks.forEach(chunk -> assertTrue(chunk.length() >= 125, "chunk too short: " + chunk.length()));
    }
}
