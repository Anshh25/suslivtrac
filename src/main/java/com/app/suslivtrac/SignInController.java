


package com.app.suslivtrac;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class SignInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    //successful finally
    public void handleSignIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Add validation or authentication logic here
        System.out.println("Sign-In Attempt");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    public void handleCancel() {
        usernameField.clear();
        passwordField.clear();
    }

    public void handleSignUp() {
        // Logic for navigating to the Sign-Up screen or showing the Sign-Up form
        System.out.println("Navigating to Sign-Up screen...");
        // You can load the Sign-Up FXML here if needed
    }


}
