package com.emma.miniragent.idm.auth.token;

/**
 * Session token storage: maps token → username with TTL.
 */
public interface TokenStore {

    void save(String token, String username, long ttlSeconds);

    String getUsername(String token);

    void remove(String token);
}
