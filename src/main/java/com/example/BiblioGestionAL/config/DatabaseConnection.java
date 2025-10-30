package com.example.BiblioGestionAL.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton Database Connection 

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/biblio_gestion",
                    "root",
                    "");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }

    }

    // Public method to provide access to the instance
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    // Method to get the database connection
    public Connection getConnection() {
        return connection;
    }
}
