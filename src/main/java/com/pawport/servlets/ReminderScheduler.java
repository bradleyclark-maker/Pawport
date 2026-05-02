package com.pawport.servlets;

import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ReminderScheduler {

    // Database
    private static final String DB_URL  = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com/pawportDB";
    private static final String DB_USER = "bclark_remote";
    private static final String DB_PASS = "password";

    // SMTP
    private static final String SMTP_HOST     = "smtp.gmail.com";
    private static final String SMTP_PORT     = "587";
    private static final String SMTP_USER     = "pawport.reminders@gmail.com"; 
    private static final String SMTP_PASSWORD = "app pass";        
    private static final String FROM_NAME     = "PawPort Reminders";

    public static void start() {
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndSendReminders();
            }
        }, 0, 60_000); // check every minute
    }

    private static void checkAndSendReminders() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Find all un-sent reminders whose time has arrived
            String query =
                "SELECT id, name, email, description, reminder_date, reminder_time " +
                "FROM reminders " +
                "WHERE sent = 0 " +
                "  AND CONCAT(reminder_date, ' ', reminder_time) <= NOW()";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int    id          = rs.getInt("id");
                String name        = rs.getString("name");
                String email       = rs.getString("email");
                String description = rs.getString("description");
                String date        = rs.getString("reminder_date");
                String time        = rs.getString("reminder_time");

                System.out.println("📬 Sending reminder #" + id + " → " + email);

                boolean sent = sendEmail(email, name, description, date, time);

                if (sent) {
                    // Mark as sent only if the email actually went through
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE reminders SET sent = 1 WHERE id = ?"
                    );
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("✅ Reminder #" + id + " marked as sent.");
                } else {
                    System.out.println("⚠️  Reminder #" + id + " email failed — will retry next cycle.");
                }
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.err.println("❌ ReminderScheduler error:");
            e.printStackTrace();
        }
    }

    /**
     * Sends a reminder email using JavaMail.
     * Returns true if the email was sent successfully.
     */
    private static boolean sendEmail(String toEmail, String reminderName,
                                     String description, String date, String time) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth",            "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host",            SMTP_HOST);
            props.put("mail.smtp.port",            SMTP_PORT);
            props.put("mail.smtp.ssl.trust",       SMTP_HOST);

            Session mailSession = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(SMTP_USER, FROM_NAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("🐾 PawPort Reminder: " + reminderName);

            // Plain-text + simple HTML body
            String html =
                "<html><body style='font-family:sans-serif;'>"
                + "<h2 style='color:#2563eb;'>🐾 PawPort Reminder</h2>"
                + "<p>Hi! This is your scheduled reminder from PawPort.</p>"
                + "<table style='border-collapse:collapse;'>"
                + "  <tr><td style='padding:4px 12px 4px 0;font-weight:bold;'>Reminder:</td>"
                + "      <td>" + reminderName + "</td></tr>"
                + "  <tr><td style='padding:4px 12px 4px 0;font-weight:bold;'>Details:</td>"
                + "      <td>" + description + "</td></tr>"
                + "  <tr><td style='padding:4px 12px 4px 0;font-weight:bold;'>Scheduled:</td>"
                + "      <td>" + date + " at " + time + "</td></tr>"
                + "</table>"
                + "<br><p style='color:#888;font-size:0.85em;'>You received this because you set up a reminder on PawPort.</p>"
                + "</body></html>";

            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}