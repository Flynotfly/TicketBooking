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

    public void registerUser(User user) {
        em.persist(user);
    }

    public User login(String usernameOrEmail, String password) {
        try {
            // Fetch user with matching username or email and password
            User user = em.createQuery("SELECT u FROM User u WHERE (u.name = :username OR u.email = :email) AND u.password = :password", User.class)
                          .setParameter("username", usernameOrEmail)
                          .setParameter("email", usernameOrEmail)
                          .setParameter("password", password) // Ensure password is handled securely
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

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public void deleteUser(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    public void updateUserRole(Long id, String role) {
        User user = em.find(User.class, id);
        if (user != null) {
            user.setRole(role);
            em.merge(user);
        }
    }
    
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
