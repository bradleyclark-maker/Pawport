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

    // 🔌 Database connection helper
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://ec2-3-133-83-59.us-east-2.compute.amazonaws.com:3306/pawportDB";
        String user = "whobson";
        String password = "pass1234";

        return DriverManager.getConnection(url, user, password);
    }

    // 📥 Load wishlist page
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
        request.getRequestDispatcher("wishlist.jsp").forward(request, response);
    }

    // 📤 Handle add/remove
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

    // 🔑 Get user ID from session
    private Integer getUserID(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Integer) session.getAttribute("userId");
    }

    // 📚 Fetch wishlist using JOIN
    private List<WishlistItem> fetchWishlist(int userId) {
        List<WishlistItem> items = new ArrayList<>();

        String sql = "SELECT i.id, i.item_name, i.item_type, i.location " +
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
                        rs.getString("location")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // ➕ Add item using item_id
    private void addItem(HttpServletRequest request, int userId) {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));

            String sql = "INSERT INTO wishlist (user_id, item_id) VALUES (?, ?)";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setInt(2, itemId);

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ➖ Remove item using item_id
    private void removeItem(HttpServletRequest request, int userId) {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));

            String sql = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setInt(2, itemId);

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}