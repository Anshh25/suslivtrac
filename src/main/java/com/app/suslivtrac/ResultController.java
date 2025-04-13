package com.app.suslivtrac;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    private Label householdLabel;

    @FXML
    private Label carLabel;

    @FXML
    private Label flightLabel;

    @FXML
    private Label totalLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dbUrl = "jdbc:mysql://localhost:3306/suslivtracdb";
        String dbUser = "root";
        String dbPassword = "AB24$qaz";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            int userId = Session.getCurrentUserId();

            double household = getFootprintFromTable(conn, "household_footprint", userId);
            double car = getFootprintFromTable(conn, "car_footprint", userId);
            double flight = getFootprintFromTable(conn, "flight_footprint", userId);

            double total = household + car + flight;

            householdLabel.setText(String.format("Household: %.2f tonnes CO₂e", household));
            carLabel.setText(String.format("Car: %.2f tonnes CO₂e", car));
            flightLabel.setText(String.format("Flight: %.2f tonnes CO₂e", flight));
            totalLabel.setText(String.format("Total Footprint: %.2f tonnes CO₂e", total));

            // Insert or update results table
            String checkQuery = "SELECT COUNT(*) FROM total_footprint WHERE user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Update existing row
                        String updateQuery = "UPDATE total_footprint SET household_fp = ?, car_fp = ?, flight_fp = ?, total_fp = ? WHERE user_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setDouble(1, household);
                            updateStmt.setDouble(2, car);
                            updateStmt.setDouble(3, flight);
                            updateStmt.setDouble(4, total);
                            updateStmt.setInt(5, userId);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        // Insert new row
                        String insertQuery = "INSERT INTO total_footprint (user_id, household_fp, car_fp, flight_fp, total_fp) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setInt(1, userId);
                            insertStmt.setDouble(2, household);
                            insertStmt.setDouble(3, car);
                            insertStmt.setDouble(4, flight);
                            insertStmt.setDouble(5, total);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getFootprintFromTable(Connection conn, String tableName, int userId) throws SQLException {
        String query = "SELECT footprint FROM " + tableName + " WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                double total = 0.0;
                while (rs.next()) {
                    total += rs.getDouble("footprint");
                }
                return total;
            }
        }
    }
}
