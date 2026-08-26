package com.emma.miniragent.idm.auth.token;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(name = "auth.token-store", havingValue = "memory")
public class MemoryTokenStore implements TokenStore {

    private record Entry(String username, long expireAtMs) {
    }

    private final ConcurrentHashMap<String, Entry> tokens = new ConcurrentHashMap<>();

    @Override
    public void save(String token, String username, long ttlSeconds) {
        long expireAt = System.currentTimeMillis() + ttlSeconds * 1000L;
        tokens.put(token, new Entry(username, expireAt));
    }

    @Override
    public String getUsername(String token) {
        Entry entry = tokens.get(token);
        if (entry == null) {
            return null;
        }
        if (entry.expireAtMs() < System.currentTimeMillis()) {
            tokens.remove(token);
            return null;
        }
        return entry.username();
    }

    @Override
    public void remove(String token) {
        tokens.remove(token);
    }
}
