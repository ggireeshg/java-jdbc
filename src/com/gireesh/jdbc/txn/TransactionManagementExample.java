package com.gireesh.jdbc.txn;

import com.gireesh.jdbc.constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionManagementExample {


    // Establishing a connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Constants.JDBC_URL, Constants.USERNAME, Constants.PASSWORD);
    }

    // Method to perform a transaction (transfer amount between two accounts)
    public static void performTransaction(int fromAccountId, int toAccountId, double amount) {
        Connection connection = null;

        try {
            // Get a connection and set auto-commit to false
            connection = getConnection();
            connection.setAutoCommit(false);

            // Check if both accounts exist
            if (!accountExists(connection, fromAccountId) || !accountExists(connection, toAccountId)) {
                throw new RuntimeException("One or more accounts do not exist.");
            }

            // Check if the balance is sufficient in the 'from' account
            if (getAccountBalance(connection, fromAccountId) < amount) {
                throw new RuntimeException("Insufficient balance for the transaction.");
            }

            // Perform the withdrawal from the 'from' account
            updateAccountBalance(connection, fromAccountId, -amount);

            // Perform the deposit to the 'to' account
            updateAccountBalance(connection, toAccountId, amount);

            // Record the transaction
            recordTransaction(connection, fromAccountId, toAccountId, amount);

            // Commit the transaction
            connection.commit();
            System.out.println("Transaction completed successfully.");

        } catch (Exception e) {
            // Rollback the transaction in case of an exception
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();

        } finally {
            // Close the connection in the finally block
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }

    // Helper method to check if an account exists
    private static boolean accountExists(Connection connection, int accountId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    // Helper method to get the balance of an account
    private static double getAccountBalance(Connection connection, int accountId) throws SQLException {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getDouble("balance");
            }
        }
    }

    // Helper method to update the balance of an account
    private static void updateAccountBalance(Connection connection, int accountId, double amount) throws SQLException {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();
        }
    }

    // Helper method to record a transaction
    private static void recordTransaction(Connection connection, int fromAccountId, int toAccountId, double amount) throws SQLException {
        String sql = "INSERT INTO transaction (account_id_from, account_id_to, amount) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, fromAccountId);
            preparedStatement.setInt(2, toAccountId);
            preparedStatement.setDouble(3, amount);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        // Example usage
        performTransaction(1, 2, 100.0);
    }
}
