package com.reneekbartlett.verisimilar.core.datasets.loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResourceLoaderUtilTests {
    public static void main(String[] args) {
        // Specify the path to your SQLite database file
        // In-Memory Database: If you just want a temporary database that deletes itself when the program closes, use jdbc:sqlite::memory:.
        String dbPath = "C:/path/to/your/database.db"; 
        String url = "jdbc:sqlite:" + dbPath;

        // Use try-with-resources to automatically close connections and statements
        String sql = "SELECT id, name, email FROM users";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {

            System.out.println("Connected to SQLite successfully!");

            // Loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                
                System.out.printf("ID: %d | Name: %s | Email: %s%n", id, name, email);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void createTableAndInsertData(String[] args) {
        String dbPath = "mydata.db"; // Creates in your project root folder
        String url = "jdbc:sqlite:" + dbPath;

        // SQL statement to create the table if it doesn't exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS items ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " value REAL NOT NULL,"
                + " weight REAL NOT NULL"
                + ");";

        // SQL statement to insert data safely using parameters (?)
        String insertSQL = "INSERT INTO items(value, weight) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // 1. Create the table
            stmt.execute(createTableSQL);
            System.out.println("Table 'items' is ready.");

            // 2. Insert Row 1
            pstmt.setDouble(1, 45.50);  // set value
            pstmt.setDouble(2, 12.35);  // set weight
            pstmt.executeUpdate();

            // 3. Insert Row 2
            pstmt.setDouble(1, 100.00); 
            pstmt.setDouble(2, 2.50);   
            pstmt.executeUpdate();

            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
