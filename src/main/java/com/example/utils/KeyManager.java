package com.example.utils;

import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class KeyManager {

    private static final KeyManager INSTANCE = new KeyManager();
    private final Key secretKey;

    private KeyManager() {
        // Generate a secure key
        this.secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    public static KeyManager getInstance() {
        return INSTANCE;
    }

    public Key getSecretKey() {
        return this.secretKey;
    }
}
