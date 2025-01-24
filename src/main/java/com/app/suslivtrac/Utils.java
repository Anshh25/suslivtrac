package com.app.suslivtrac;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Utils {
//hellooo
    public static void SignInUser(ActionEvent event, String username, String password) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/suslivtracdb", "root", "Devansh@025");
            preparedStatement = connection.prepareStatement("SELECT password FROM auth WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect.");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");

                    if (retrievedPassword.equals(password)) {
                        System.out.println("Sign-In Attempt");
                        System.out.println("Username: " + username);
                        System.out.println("Password: " + password);
                        FXMLLoader loader = new FXMLLoader(Utils.class.getResource("homescreen.fxml"));
                        Parent root = loader.load();

                        homescreen controller = loader.getController();
                        controller.setWelcomeMessage(username);

                        // Get the current stage and set the scene to the home screen
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setTitle("Home Screen");
                        stage.show();

                    }
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void SignUpUser(ActionEvent event, String email, String username, String password){

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/suslivtracdb", "root", "Devansh@025");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM auth WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username already exists.");
                alert.show();
            }
            else{
                psInsert = connection.prepareStatement("INSERT INTO auth (username, password, email) VALUES (?, ?, ?);");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, email);
                psInsert.executeUpdate();


                FXMLLoader loader = new FXMLLoader(Utils.class.getResource("signin.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Sign In");
                stage.show();

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }





}
