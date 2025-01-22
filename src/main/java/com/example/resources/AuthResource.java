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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Key;
import java.util.Date;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication API", description = "APIs for user authentication.")
public class AuthResource {

    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    @EJB
    private UserService userService;

    @Operation(summary = "Login", description = "Authenticates a user and generates a JWT token.")
    @POST
    @Path("/login")
    public Response login(
            @Parameter(description = "Username or email of the user", required = true) @QueryParam("usernameOrEmail") String usernameOrEmail,
            @Parameter(description = "Password of the user", required = true) @QueryParam("password") String password) {
        User user = userService.login(usernameOrEmail, password);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Invalid credentials\"}").build();
        }

        Key secretKey = KeyManager.getInstance().getSecretKey();

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
