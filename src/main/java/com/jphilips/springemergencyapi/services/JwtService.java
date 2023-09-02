package com.jphilips.springemergencyapi.services;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    // @Value("${application.security.jwt.secret-key}")
    // private String secret;

    private final SecretService secretService;

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretService.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> additionalClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(additionalClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("Authorities", userDetails.getAuthorities());
        return generateToken(claims, userDetails);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        boolean roleIsValid = false;

        @SuppressWarnings("unchecked")
        Function<Claims, ArrayList<Map<String, Object>>> extractAuthorities = claims -> claims.get("Authorities",
                ArrayList.class);
        ArrayList<Map<String, Object>> roles = extractClaim(token, extractAuthorities);

        // TODO: Add better logic to prevent error by comparing full List ofauthorities
        // from token and full List of authorities from userdetails
        for (Map<String, Object> role : roles) {
            Object authority = role.get("authority");
            if (authority != null && authority
                    .equals(userDetails.getAuthorities().iterator().next().getAuthority())) {
                roleIsValid = true;
                break;
            }
        }

        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && roleIsValid;
    }

}
