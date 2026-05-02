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

    private static final String URL  = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB";
    private static final String USER = "bclark_remote";
    private static final String PASS = "password";

    // ADD reminder 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        // 1. Require an active session with a userId
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("NOT_LOGGED_IN");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // 2. Collect form fields (email comes from the session/DB, not the form)
        String name        = request.getParameter("name");
        String description = request.getParameter("description");
        String date        = request.getParameter("date");
        String time        = request.getParameter("time");

        System.out.println("=== POST /ReminderServlet  userId=" + userId + " ===");
        System.out.println(name + " | " + description + " | " + date + " | " + time);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // 3. Look up the user's email from the users table
            String email = null;
            PreparedStatement emailPs = conn.prepareStatement(
                "SELECT email FROM users WHERE id = ?"
            );
            emailPs.setInt(1, userId);
            ResultSet emailRs = emailPs.executeQuery();
            if (emailRs.next()) {
                email = emailRs.getString("email");
            }
            emailRs.close();
            emailPs.close();

            if (email == null) {
                response.getWriter().println("USER_NOT_FOUND");
                conn.close();
                return;
            }

            // 4. Insert the reminder linked to this user
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reminders (user_id, name, description, reminder_date, reminder_time, email, sent) " +
                "VALUES (?, ?, ?, ?, ?, ?, FALSE)"
            );
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setString(4, date);
            ps.setString(5, time);
            ps.setString(6, email);

            int rows = ps.executeUpdate();
            conn.close();

            response.getWriter().println(rows > 0 ? "SUCCESS" : "FAILED");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("ERROR");
        }
    }

    // GET reminders for the current user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // 1. Require an active session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("[]");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        ArrayList<String> reminders = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // 2. Only fetch reminders belonging to this user
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, name, description, reminder_date, reminder_time, email, sent " +
                "FROM reminders WHERE user_id = ? ORDER BY reminder_date, reminder_time"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Build a simple JSON object per reminder
                String item = "{"
                    + "\"id\":"          + rs.getInt("id")                        + ","
                    + "\"name\":\""      + escape(rs.getString("name"))            + "\","
                    + "\"description\":\"" + escape(rs.getString("description"))   + "\","
                    + "\"date\":\""      + rs.getString("reminder_date")           + "\","
                    + "\"time\":\""      + rs.getString("reminder_time")           + "\","
                    + "\"email\":\""     + escape(rs.getString("email"))           + "\","
                    + "\"sent\":"        + rs.getBoolean("sent")
                    + "}";
                reminders.add(item);
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("[");
        for (int i = 0; i < reminders.size(); i++) {
            out.print(reminders.get(i));
            if (i < reminders.size() - 1) out.print(",");
        }
        out.print("]");
    }

    // DELETE a reminder 
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("NOT_LOGGED_IN");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.getWriter().println("MISSING_ID");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            // Only delete if the reminder belongs to this user (security check)
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM reminders WHERE id = ? AND user_id = ?"
            );
            ps.setInt(1, Integer.parseInt(idParam));
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            conn.close();

            response.getWriter().println(rows > 0 ? "DELETED" : "NOT_FOUND");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("ERROR");
        }
    }

    /** Escape special characters for inline JSON strings. */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}