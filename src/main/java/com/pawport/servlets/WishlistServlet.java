package com.pawport.servlets;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/*
@WebServlet("/wishlist")
*/
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com:3306/pawportDB";
        String user = "bclark_remote";
        String password = "password"; //put pass here

        return DriverManager.getConnection(url, user, password);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserID(request);

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<WishlistItem> wishlist = fetchWishlist(userId);

        request.setAttribute("wishlist", wishlist);

        // Optional debug message
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            request.setAttribute("message", message);
            request.getSession().removeAttribute("message");
        }

        request.getRequestDispatcher("wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserID(request);

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addItem(request, userId);
        } else if ("remove".equals(action)) {
            removeItem(request, userId);
        }

        response.sendRedirect("wishlist");
    }

    /*
    private Integer getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // TEMP: force user for testing
        if (session.getAttribute("userId") == null) {
            session.setAttribute("userId", 4);
        }

        return (Integer) session.getAttribute("userId");
    }
    */
    
    private Integer getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        // Use userId directly if already in session
        if (session.getAttribute("userId") != null) {
            return (Integer) session.getAttribute("userId");
        }

        // Fall back to looking up by email (in case of old sessions)
        String email = (String) session.getAttribute("userEmail");
        if (email == null) return null;
        int id = DatabaseUtility.getUserIdByEmail(email);
        if (id < 0) return null;
        session.setAttribute("userId", id); // cache it for next time
        return id;
    }

    private List<WishlistItem> fetchWishlist(int userId) {
        List<WishlistItem> items = new ArrayList<>();

        String sql = "SELECT i.id, i.item_name, i.item_type, i.item_location " +
                     "FROM wishlist w " +
                     "JOIN items i ON w.item_id = i.id " +
                     "WHERE w.user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(new WishlistItem(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getString("item_type"),
                        rs.getString("item_location")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // ✅ Add using item NAME → converts to item_id
    private void addItem(HttpServletRequest request, int userId) {
        String itemName = request.getParameter("itemName");

        if (itemName == null || itemName.trim().isEmpty()) return;

        String findSql = "SELECT id FROM items WHERE LOWER(item_name) = LOWER(?)";
        String insertSql = "INSERT INTO wishlist (user_id, item_id) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement findStmt = conn.prepareStatement(findSql)) {

            findStmt.setString(1, itemName.trim());
            ResultSet rs = findStmt.executeQuery();

            if (rs.next()) {
                int itemId = rs.getInt("id");

                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, itemId);
                    insertStmt.executeUpdate();

                    request.getSession().setAttribute("message", "Item added!");
                }

            } else {
                request.getSession().setAttribute("message", "Item not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeItem(HttpServletRequest request, int userId) {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));

            String sql = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setInt(2, itemId);
                ps.executeUpdate();

                request.getSession().setAttribute("message", "Item removed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

