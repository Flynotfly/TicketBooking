package com.example.filters;

import com.example.utils.KeyManager;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.Key;
import java.util.Set;

@Provider
public class JwtFilter implements ContainerRequestFilter {

    private static final Set<String> UNPROTECTED_PATHS = Set.of("/auth/login");

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Exclude the auth/login endpoint from authentication
        if (path.equals("auth/login")) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing or invalid Authorization header").build());
            return;
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            Key secretKey = KeyManager.getInstance().getSecretKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Store userId and role for later use
            requestContext.setProperty("userId", Long.valueOf(claims.get("userId").toString()));
            requestContext.setProperty("role", claims.get("role").toString());
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build());
        }
    }
}
