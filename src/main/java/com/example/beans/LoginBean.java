package com.example.beans;

import com.example.entities.User;
import com.example.services.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Managed Bean for user authentication and session handling.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private String message;
    private User loggedInUser;

    private User newUser = new User();
    private String confirmPassword;
    private boolean registrationSuccessful;

    @Inject
    private UserService userService;

    /**
     * Authenticates the user using the provided credentials.
     * @return Navigation outcome for successful or failed login.
     */
    public String authenticate() {
        try {
            loggedInUser = userService.findByUsernameOrEmail(username);

            if (loggedInUser != null && loggedInUser.getPassword().equals(password)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedInUser", loggedInUser);
                return "home.xhtml?faces-redirect=true"; // Redirect to home page
            } else {
                message = "Invalid username or password.";
                return null; // Stay on the login page
            }
        } catch (Exception e) {
            message = "An error occurred during login. Please try again.";
            return null;
        }
    }

    /**
     * Ends the user session and logs the user out.
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml?faces-redirect=true"; // Redirect to login page
    }

    /**
     * Handles user registration.
     * @return Navigation outcome for successful or failed registration.
     */
    public String register() {
        if (!newUser.getPassword().equals(confirmPassword)) {
            message = "Passwords do not match.";
            registrationSuccessful = false;
            return null;
        }

        try {
            if (userService.findByUsernameOrEmail(newUser.getName()) != null ||
                userService.findByUsernameOrEmail(newUser.getEmail()) != null) {
                message = "Username or email already exists.";
                registrationSuccessful = false;
                return null;
            }

            newUser.setRole(User.Role.USER); // Set default role
            userService.registerUser(newUser);

            message = "Registration successful! You can now log in.";
            registrationSuccessful = true;

            newUser = new User();
            confirmPassword = null;

            return "login.xhtml?faces-redirect=true"; // Redirect to login page
        } catch (Exception e) {
            message = "An error occurred during registration. Please try again.";
            registrationSuccessful = false;
            return null;
        }
    }

    /**
     * Redirects the user to the login page if not authenticated.
     */
    public void ensureAuthentication() {
        if (loggedInUser == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if the logged-in user is an admin.
     * @return True if the user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.getRole() == User.Role.ADMIN;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }

    public void setRegistrationSuccessful(boolean registrationSuccessful) {
        this.registrationSuccessful = registrationSuccessful;
    }
}
