package com.example.ZenFlow.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    // Hash gerado para "admin123" - CORRIGIDO
    // Hash anterior estava incorreto, este foi gerado e testado
    private static final String PASSWORD_HASH = "$2a$10$1zIRKeI1qWygH2VT.ZD2Y.hiuRPUnJFl/cdsMSd9TxP1q7q1a/XNC";

    // Usuários em memória para demonstração
    // Em produção, buscar do banco de dados
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Tentando carregar usuário: {}", username);
        
        // Usuário padrão para demonstração
        if ("admin".equals(username)) {
            logger.info("Usuário admin encontrado");
            return User.builder()
                    .username("admin")
                    .password(PASSWORD_HASH) // senha: admin123
                    .authorities(new ArrayList<>())
                    .build();
        }
        
        if ("user".equals(username)) {
            logger.info("Usuário user encontrado");
            return User.builder()
                    .username("user")
                    .password(PASSWORD_HASH) // senha: admin123
                    .authorities(new ArrayList<>())
                    .build();
        }
        
        logger.warn("Usuário não encontrado: {}", username);
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}


