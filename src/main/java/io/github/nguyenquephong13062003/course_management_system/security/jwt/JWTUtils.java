package io.github.nguyenquephong13062003.course_management_system.security.jwt;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JWTUtils is a utility class for handling JWT operations such as token generation, extraction, and validation.
 */
@Component
public class JWTUtils {

    /**
     * The secret key used for signing the JWT tokens, injected from application properties.
     */
    @Value("${jwt.secret-key}")
    private String secretKey;

    /**
     * The expiration time for the JWT tokens, injected from application properties.
     */
    @Value("${jwt.expiration}")
    private Long expiredTime;

    /**
     * Retrieves the signing key for JWT operations by decoding the secret key.
     * @return The SecretKey used for signing JWT tokens.
     */
    public SecretKey getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Generates a JWT token for the given username.
     * @param username The username for which the token is generated.
     * @return A signed JWT token as a String.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with the specified claims and username.
     * @param claims A map of claims to include in the token.
     * @param username The username to set as the subject of the token.
     * @return A signed JWT token as a String.
     */
    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Extracts all claims from the given JWT token.
     * @param token The JWT token from which to extract claims.
     * @return A Claims object containing the extracted claims.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts a specific claim from the given Claims object using a claims resolver function.
     * @param claims The Claims object from which to extract the claim.
     * @param claimsResolver A function that defines how to extract the desired claim.
     * @param <T> The type of the claim to be extracted.
     * @return The extracted claim of type T.
     */
    public <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the username (subject) from the given Claims object.
     * @param claims The Claims object from which to extract the username.
     * @return The username extracted from the Claims.
     */
    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    /**
     * Extracts the expiration date from the given Claims object.
     * @param claims The Claims object from which to extract the expiration date.
     * @return The expiration date extracted from the Claims.
     */
    public Date extractExpiration(Claims claims) {
        return claims.getExpiration();
    }

    /**
     * Checks if the JWT token represented by the given Claims object has expired.
     * @param claims The Claims object representing the JWT token.
     * @return true if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(Claims claims) {
        return extractExpiration(claims).before(new Date());
    }

    /**
     * Validates the JWT token represented by the given Claims object against the provided UserDetails.
     * @param claims The Claims object representing the JWT token.
     * @param userDetails The UserDetails to validate against.
     * @return true if the token is valid and matches the user details, false otherwise.
     */
    public boolean validateToken(Claims claims, UserDetails userDetails) {
        String username = extractUsername(claims);
        return username.equals(userDetails.getUsername()) && userDetails.isEnabled() && !isTokenExpired(claims);
    }

}
