package com.app.database.connection;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try {
            PostgreSQLProvider provider = new PostgreSQLProvider();

            Connection conn = provider.getConnection();

            System.out.println("Connected!");

            conn.close();
            provider.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}