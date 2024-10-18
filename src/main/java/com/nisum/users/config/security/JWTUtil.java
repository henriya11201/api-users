package com.nisum.users.config.security;

import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    @Value("${user.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${user.jwt.token-expires-minutes}")
    private long TOKEN_MINUTES;

    private SecretKey secretKey;

    private final UserService service;

    public JWTUtil(@Lazy UserService service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

    }

    public String generateToken(UserDto userDto) throws IOException {

        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDto.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Obtiene el nombre del rol
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDto.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(getTokenMillis()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public long getTokenMillis() {
        Instant expirationToken = Instant.now().plus(TOKEN_MINUTES, ChronoUnit.MINUTES);
        return expirationToken.toEpochMilli();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        List<Object> rolesFromToken = claims.get("roles", List.class);

        return rolesFromToken.stream()
                .map(role -> {
                    if (role instanceof LinkedHashMap)
                        return (String) ((LinkedHashMap) role).get("role");

                    return (String) role;
                })
                .collect(Collectors.toList());
    }

    public String getUsernameFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken, UUID id) {
        try {
            if (id != null) {
                String email = service.getUser(id).getEmail();

                String emailToken = getUsernameFromToken(authToken);
                if(email.equalsIgnoreCase(emailToken)){
                    Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
                    return true;
                }
            } else {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
                return true;
            }
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (Exception e) {
            System.out.println("Invalid Token");
        }
        return false;
    }

}
