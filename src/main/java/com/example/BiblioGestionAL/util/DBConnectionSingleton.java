package com.example.BiblioGestionAL.util;

import javax.sql.DataSource;

/**
 * Exemple de Singleton pour la connexion base de données.
 * Spring gère déjà le DataSource en singleton, mais on illustre le pattern.
 */
public class DBConnectionSingleton {
    private static DBConnectionSingleton instance;
    private final DataSource dataSource;

    private DBConnectionSingleton(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static synchronized DBConnectionSingleton getInstance(DataSource ds) {
        if (instance == null) instance = new DBConnectionSingleton(ds);
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
