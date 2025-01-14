package com.example.resources;

import com.example.entities.User;
import com.example.services.UserService;
import com.example.utils.KeyManager;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Date;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    @EJB
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(@QueryParam("usernameOrEmail") String usernameOrEmail,
                          @QueryParam("password") String password) {
        // Authenticate user
        User user = userService.login(usernameOrEmail, password);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Invalid credentials\"}").build();
        }

        Key secretKey = KeyManager.getInstance().getSecretKey();

        // Generate JWT
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return Response.ok("{\"token\":\"" + token + "\"}").build();
    }
}
