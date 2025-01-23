package com.example.services;

import com.example.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "bookingPU")
    private EntityManager em;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    /**
     * Registers a new user in the database.
     */
    public void registerUser(User user) {
        em.persist(user);
    }

    /**
     * Finds a user by username or email for authentication purposes.
     * @param usernameOrEmail The username or email of the user.
     * @return The matching user, or null if not found.
     */
    public User findByUsernameOrEmail(String usernameOrEmail) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name = :usernameOrEmail OR u.email = :usernameOrEmail", User.class)
                     .setParameter("usernameOrEmail", usernameOrEmail)
                     .getSingleResult();
        } catch (NoResultException e) {
            LOGGER.warning("No user found for username/email: " + usernameOrEmail);
            return null;
        } catch (NonUniqueResultException e) {
            LOGGER.warning("Multiple users found for username/email: " + usernameOrEmail);
            return null;
        } catch (Exception e) {
            LOGGER.severe("An error occurred while finding user by username/email: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles user login by verifying credentials.
     * @param usernameOrEmail The username or email of the user.
     * @param password The password provided by the user.
     * @return The authenticated user, or null if authentication fails.
     */
    public User login(String usernameOrEmail, String password) {
    try {
        User user = em.createQuery(
                "SELECT u FROM User u WHERE (u.name = :usernameOrEmail OR u.email = :usernameOrEmail) AND u.password = :password", 
                User.class)
                .setParameter("usernameOrEmail", usernameOrEmail)
                .setParameter("password", password) // Ensure password handling is secure
                .getSingleResult();
        LOGGER.info("Login successful for user: " + user.getName());
        return user;
    } catch (NoResultException e) {
        LOGGER.warning("Login failed: No user found for username/email: " + usernameOrEmail);
        return null;
    } catch (NonUniqueResultException e) {
        LOGGER.warning("Login failed: Multiple users found for username/email: " + usernameOrEmail);
        return null;
    } catch (Exception e) {
        LOGGER.severe("An error occurred during login: " + e.getMessage());
        return null;
    }
}

    /**
     * Retrieves all users from the database.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    /**
     * Deletes a user by ID.
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    /**
    * Updates the role of a user.
    * @param id The ID of the user.
    * @param role The new role to assign (must match User.Role values).
    */
   public void updateUserRole(Long id, String role) {
       User user = em.find(User.class, id);
       if (user != null) {
           try {
               // Convert String to User.Role enum
               User.Role roleEnum = User.Role.valueOf(role.toUpperCase());
               user.setRole(roleEnum);
               em.merge(user);
           } catch (IllegalArgumentException e) {
               throw new RuntimeException("Invalid role: " + role);
           }
       }
   }

    /**
     * Finds a user by their ID.
     * @param id The ID of the user.
     * @return The user if found, or null if not.
     */
    public User findUserById(Long id) {
        try {
            User user = em.find(User.class, id);
            if (user != null) {
                LOGGER.info("User found: " + user.getName());
                return user;
            } else {
                LOGGER.warning("User not found with ID: " + id);
                return null;
            }
        } catch (Exception e) {
            LOGGER.severe("Error finding user by ID: " + e.getMessage());
            return null;
        }
    }
}
