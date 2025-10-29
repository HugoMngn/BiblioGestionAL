package com.example.BiblioGestionAL.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/biblio_gestion",
                "root",
                ""
            );
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }

    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
