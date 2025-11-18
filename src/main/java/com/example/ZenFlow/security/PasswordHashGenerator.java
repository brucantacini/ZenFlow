package com.example.ZenFlow.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe utilitária para gerar hash de senhas.
 * Execute o método main para gerar um novo hash.
 * 
 * Hash atual para "admin123": $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6a
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = encoder.encode(password);
        System.out.println("Senha: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Verificação: " + encoder.matches(password, hash));
    }
}

