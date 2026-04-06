package com.pawport.servlets;

import java.io.IOException;
import java.io.BufferedReader;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reminder")
public class RemindersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RemindersServlet() {
        super();
    }

    // Handle POST (from button)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String data = sb.toString();

        String title = extractValue(data, "title");
        String minutesStr = extractValue(data, "minutes");
        String email = extractValue(data, "email");

        int minutes = Integer.parseInt(minutesStr);
        int delay = minutes * 60 * 1000;

        System.out.println("📌 Reminder set: " + title + " for " + email);

        // Background reminder
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                System.out.println("⏰ Reminder Triggered: " + title + " → " + email);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        response.setContentType("text/plain");
        response.getWriter().write("✅ Reminder scheduled!");
    }

    // Test endpoint
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("Servlet is working!");
    }

    // Simple JSON parser
    private String extractValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search) + search.length();
        int end = json.indexOf(",", start);

        if (end == -1) {
            end = json.indexOf("}", start);
        }

        return json.substring(start, end).replaceAll("\"", "").trim();
    }
}