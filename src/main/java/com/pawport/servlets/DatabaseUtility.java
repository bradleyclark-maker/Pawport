package com.pawport.servlets;

import java.sql.*;

public class DatabaseUtility {
    private static final String URL = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB"; 
    private static final String USER = "bclark_remote"; 
    private static final String PASS = "password"; // your pass here

    public static boolean verifyUser(String email, String password) {
        boolean status = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            
            // Use a Prepared Statement to prevent SQL Injection (Cybersecurity Best Practice)
            String sql = "SELECT * FROM users WHERE EMAIL = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            status = rs.next(); // If a row is found, status becomes true

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public static int getUserIdByEmail(String email) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            String sql = "SELECT id FROM users WHERE EMAIL = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}