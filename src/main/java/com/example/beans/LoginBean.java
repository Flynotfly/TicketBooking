package com.example.beans;

import com.example.entities.User;
import com.example.services.UserService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class LoginBean {
    private String usernameOrEmail;
    private String password;

    // Inject UserService to handle login logic
    @jakarta.inject.Inject
    private UserService userService;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        User user = userService.login(usernameOrEmail, password);

        if (user != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
            return "home.xhtml?faces-redirect=true"; // Redirect to home page
        } else {
            FacesContext.getCurrentInstance().addMessage("global", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
            return null; // Stay on the login page
        }
    }
}
