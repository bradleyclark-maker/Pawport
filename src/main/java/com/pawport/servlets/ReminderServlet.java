package com.pawport.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ReminderServlet")
public class ReminderServlet extends HttpServlet {

	private static final String URL = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB";
	private static final String USER = "bclark_remote"; // Your MySQL user name
	private static final String PASS = "PawPortUser1";


    // 🔹 ADD reminder
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== POST REQUEST RECEIVED ===");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String email = request.getParameter("email");

        System.out.println("DATA:");
        System.out.println(name + ", " + description + ", " + date + ", " + time + ", " + email);

        try {
        	try {
        	    Class.forName("com.mysql.cj.jdbc.Driver");
        	    System.out.println("✅ DRIVER LOADED");
        	} catch (Exception e) {
        	    System.out.println("❌ DRIVER NOT FOUND");
        	}
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Connected to database");

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reminders (name, description, reminder_date, reminder_time, email) VALUES (?, ?, ?, ?, ?)"
            );

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, email);

            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);

            conn.close();

            response.setContentType("text/plain");

            if (rows > 0) {
                response.getWriter().println("SUCCESS");
            } else {
                response.getWriter().println("FAILED");
            }

        } catch (Exception e) {
            System.out.println("❌ DATABASE ERROR:");
            e.printStackTrace();

            response.setContentType("text/plain");
            response.getWriter().println("ERROR");
        }
    }

    // 🔹 GET reminders
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        ArrayList<String> reminders = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ DRIVER LOADED");
        } catch (Exception e) {
            System.out.println("❌ DRIVER NOT FOUND");
        }

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT name, description, reminder_date, reminder_time, email FROM reminders"
            );

            while (rs.next()) {;
                String item =
                    rs.getString("name") + " | " +
                    rs.getString("description") + " | " +
                    rs.getString("reminder_date") + " " +
                    rs.getString("reminder_time") + " | " +
                    rs.getString("email");

                reminders.add(item);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("[");
        for (int i = 0; i < reminders.size(); i++) {
            out.print("\"" + reminders.get(i) + "\"");
            if (i < reminders.size() - 1) {
                out.print(",");
            }
        }
        out.print("]");
    }
}