package com.ahnis.servaia.common.security;

import com.ahnis.servaia.common.security.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * <p>
 * This class provides methods for generating, validating, and extracting information from JWTs.
 * It uses the {@link JwtProperties} class to retrieve the secret key and expiration time.
 * </p>
 *
 * @author Ahnis Singh Aneja
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * Generates the signing key for JWT using the secret key from {@link JwtProperties}.
     *
     * @return A {@link SecretKey} instance used to sign the JWT.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Generates a JWT for the given user details.
     *
     * @param userDetails The {@link UserDetails} object containing the user's information.
     * @return A JWT as a string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add roles to the JWT claims
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);

        return createToken(claims, userDetails);
    }

    /**
     * Creates a JWT with the given claims and user details.
     *
     * @param claims      A map of claims to include in the JWT.
     * @param userDetails The {@link UserDetails} object containing the user's information.
     * @return A JWT as a string.
     */
    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates the given JWT against the user details.
     *
     * @param token       The JWT to validate.
     * @param userDetails The {@link UserDetails} object containing the user's information.
     * @return {@code true} if the JWT is valid, {@code false} otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }

    /**
     * Extracts the username from the given JWT.
     *
     * @param token The JWT from which to extract the username.
     * @return The username as a string.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the roles from the given JWT.
     *
     * @param token The JWT from which to extract the roles.
     * @return A list of roles as strings.
     */
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    /**
     * Extracts a specific claim from the given JWT.
     *
     * @param token          The JWT from which to extract the claim.
     * @param claimsResolver A function to resolve the desired claim from the JWT claims.
     * @return The resolved claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT.
     *
     * @param token The JWT from which to extract the claims.
     * @return A {@link Claims} object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Checks if the given JWT is expired.
     *
     * @param token The JWT to check.
     * @return {@code true} if the JWT is expired, {@code false} otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT.
     *
     * @param token The JWT from which to extract the expiration date.
     * @return The expiration date as a {@link Date}.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
