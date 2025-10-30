package com.example.BiblioGestionAL.util;

import javax.sql.DataSource;

// Singleton class for managing database connection.
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
