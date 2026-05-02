package com.pawport.servlets;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ReminderScheduler {

	private static final String URL = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB";
	private static final String USER = "bclark_remote"; // Your MySQL user name
	private static final String PASS = "password";


    public static void start() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                checkReminders();
            }
        }, 0, 60000); // every minute
    }

    private static void checkReminders() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            String query =
                "SELECT * FROM reminders WHERE sent = FALSE AND " +
                "CONCAT(reminder_date, ' ', reminder_time) <= NOW()";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("REMINDER TRIGGERED: " + name + " → " + email);

                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE reminders SET sent = TRUE WHERE id = ?"
                );
                ps.setInt(1, rs.getInt("id"));
                ps.executeUpdate();
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}