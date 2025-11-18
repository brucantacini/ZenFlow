package com.example.ZenFlow.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordHashTest {

    @Test
    public void testPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6a";
        
        boolean matches = encoder.matches(password, hash);
        System.out.println("Senha: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Matches: " + matches);
        
        if (!matches) {
            System.out.println("\n=== GERANDO NOVO HASH ===");
            String newHash = encoder.encode(password);
            System.out.println("Novo Hash: " + newHash);
            System.out.println("Verificação do novo hash: " + encoder.matches(password, newHash));
        }
        
        assertTrue(matches, "O hash não corresponde à senha!");
    }
}

