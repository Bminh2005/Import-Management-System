package com.app.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLProvider implements IDBProvider{
    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public void shutdown() {

    }
}
