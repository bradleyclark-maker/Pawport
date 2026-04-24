package com.pawport;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 🔌 Database connection helper (you can improve this later)
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pawport";
        String user = "root";
        String password = "password";

        return DriverManager.getConnection(url, user, password);
    }

    // 📥 Handles loading the wishlist page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId = getUserID(request);

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<String> wishlist = fetchWishlist(userId);

        // Send data to frontend
        request.setAttribute("wishlist", wishlist);

        // Forward to JSP
        request.getRequestDispatcher("wishlist.jsp").forward(request, response);
    }

    // 📤 Handles add/remove actions (we'll expand later)
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

        // Redirect back to refresh wishlist
        response.sendRedirect("wishlist");
    }

    // 🔑 Get user ID from session
    private Integer getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) return null;

        return (Integer) session.getAttribute("userId");
    }

    // 📚 Fetch wishlist items from DB
    private List<String> fetchWishlist(int userId) {
        List<String> items = new ArrayList<>();

        String sql = "SELECT item_name FROM wishlist WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(rs.getString("item_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // ➕ Add item (basic version)
    private void addItem(HttpServletRequest request, int userId) {
        String itemName = request.getParameter("itemName");

        String sql = "INSERT INTO wishlist (user_id, item_name) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, itemName);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ➖ Remove item (basic version)
    private void removeItem(HttpServletRequest request, int userId) {
        String itemName = request.getParameter("itemName");

        String sql = "DELETE FROM wishlist WHERE user_id = ? AND item_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, itemName);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}