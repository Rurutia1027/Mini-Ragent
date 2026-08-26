package com.emma.miniragent.rag.config;

import com.emma.miniragent.rag.chunk.TextChunker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RagProperties.class)
public class RagAutoConfiguration {

    @Bean
    TextChunker textChunker(RagProperties properties) {
        return new TextChunker(properties.getChunkSize(), properties.getChunkOverlap());
    }

}
