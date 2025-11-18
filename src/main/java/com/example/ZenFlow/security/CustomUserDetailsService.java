package com.example.ZenFlow.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final String PASSWORD_HASH = "$2a$10$1zIRKeI1qWygH2VT.ZD2Y.hiuRPUnJFl/cdsMSd9TxP1q7q1a/XNC";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username) || "user".equals(username)) {
            return User.builder()
                    .username(username)
                    .password(PASSWORD_HASH)
                    .authorities(new ArrayList<>())
                    .build();
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}


