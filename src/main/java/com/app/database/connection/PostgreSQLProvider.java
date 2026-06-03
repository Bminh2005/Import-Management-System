package com.app.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLProvider implements IDBProvider {
    private HikariDataSource dataSource;

    public PostgreSQLProvider() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:6543/postgres");
        config.setUsername("postgres.lsrmwnisivtnpeqmpxcu");
        config.setPassword("binhminh3000");
        config.setDriverClassName("org.postgresql.Driver"); // Driver PostgreSQL

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}