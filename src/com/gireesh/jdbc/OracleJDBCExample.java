package com.gireesh.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleJDBCExample {

    // JDBC URL, username, and password of Oracle database
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "XXXX";

    // Establishing a connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Insert data into the customer table
    public static void insertData(int customerId, String customerName, int mobile_Number, String home_Address) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO customer (customer_ID, customer_name, mobile_Number, home_Address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.setString(2, customerName);
                preparedStatement.setInt(3, mobile_Number);
                preparedStatement.setString(4, home_Address);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update data in the customer table
    public static void updateData(int customerId, String newCustomerName) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE customer SET customer_name = ? WHERE customer_ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newCustomerName);
                preparedStatement.setInt(2, customerId);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Select all data from the customer table
    public static void selectData() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM customer";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int customerId = resultSet.getInt("customer_ID");
                        String customerName = resultSet.getString("customer_name");
                        long mobile_Number = resultSet.getLong("mobile_Number");
                        String home_Address = resultSet.getString("home_Address");

                        System.out.println("Customer ID: " + customerId);
                        System.out.println("Customer Name: " + customerName);
                        System.out.println("Mobile Number: " + mobile_Number);
                        System.out.println("Home Address: " + home_Address);
                        System.out.println("--------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage
        insertData(1, "John Doe", 123456789, "123 Main St");
        updateData(1, "Updated Name");
        selectData();
    }
}
