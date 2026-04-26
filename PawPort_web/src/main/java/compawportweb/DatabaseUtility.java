package compawportweb;

import java.sql.*;

public class DatabaseUtility {
    // You'll replace these with your actual AWS RDS credentials
	private static final String URL = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB"; 
    private static final String USER = "bclark_remote"; // Your MySQL user name
    private static final String PASS = "PawPortUser1"; // Your MySQL password


    public static boolean verifyUser(String email, String password) {
        boolean status = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            
            // Use a Prepared Statement to prevent SQL Injection (Cybersecurity Best Practice)
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
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
}
