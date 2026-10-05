package com.gustavo.ToDoList.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JWTService {

    private final JwtEncoder encoder;

    public JWTService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication){
        Instant now = Instant.now(); // Quando o Token foi criado
        Long expiry = 3600L; // Quando ele irá expirar

        var claims = JwtClaimsSet.builder()
                .issuer("ToDoList")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .build();

        return  encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
