package com.pawport.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)   // 5 MB photo limit
public class ProfilesServlet extends HttpServlet {

	// update this
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/testDB";
    private static final String DB_USER = "whobson_remote";
    private static final String DB_PASS = "your password"; 

    // create table profiles (
    //     profile_id   int primary key auto_incement,
    //     user_id      int not null,
    //     name         varchar(255),
    //     description  text,
    //     service_type varchar(50),
    //     contact_info text,
    //     hours        text,
    //     location     varchar(100),
    //     status       varchar(50) DEFAULT 'pending',
    //     photo        longblob,
    //     photo_type   varchar(50),
    //	   updated_at   timestamp default current_timestamp on update current_timestamp,
    //     foreign key (user_id) references users(user_id)
    // );

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Determine which user's profile to load:
        // If a userId param is in the URL (e.g. from search results), use that.
        // Otherwise load the logged in user's own profile.
        int userId;
        String userIdParam = req.getParameter("userId");
        if (userIdParam != null) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
                return;
            }
        } else {
            userId = getLoggedInUserId(req, resp);
            if (userId < 0) return;
        }

        // If redirect=view, send to viewprofile with the logged in user's ID
        if ("view".equals(req.getParameter("redirect"))) {
            resp.sendRedirect(req.getContextPath() + "/viewprofile.html?userId=" + userId);
            return;
        }

        // Stream photo bytes directly
        String pathInfo = req.getPathInfo();
        if ("/photo".equals(pathInfo)) {
            servePhoto(userId, resp);
            return;
        }

        // Load profile from DB
        String profileName = null, description = null, serviceType = null;
        String contactInfo = null, hours = null, location = null, status = null;
        boolean hasPhoto = false;

        try (Connection conn = getConnection()) {
            String sql = "SELECT name, description, service_type, contact_info, hours, location, status, photo_type " +
                         "FROM profiles WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        profileName = rs.getString("name");
                        description = rs.getString("description");
                        serviceType = rs.getString("service_type");
                        contactInfo = rs.getString("contact_info");
                        hours       = rs.getString("hours");
                        location    = rs.getString("location");
                        status      = rs.getString("status");
                        hasPhoto    = rs.getString("photo_type") != null;
                    }
                }
            }
        } catch (SQLException e) {
            log("ProfilesServlet.doGet: DB error", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not load profile.");
            return;
        }

        // If JSON format requested (used by viewprofile.js and editprofile.js), return JSON
        if ("json".equals(req.getParameter("format"))) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String json = String.format(
                "{\"name\":%s,\"description\":%s,\"serviceType\":%s,\"contactInfo\":%s," +
                "\"hours\":%s,\"location\":%s,\"status\":%s,\"hasPhoto\":%b}",
                toJson(profileName),
                toJson(description),
                toJson(serviceType),
                toJson(contactInfo),
                toJson(hours),
                toJson(location),
                toJson(status),
                hasPhoto
            );
            resp.getWriter().write(json);
            return;
        }

        // Otherwise set attributes and forward to edit page
        req.setAttribute("profileName", profileName);
        req.setAttribute("description", description);
        req.setAttribute("serviceType", serviceType);
        req.setAttribute("contactInfo", contactInfo);
        req.setAttribute("hours",       hours);
        req.setAttribute("location",    location);
        req.setAttribute("status",      status);
        req.setAttribute("hasPhoto",    hasPhoto);

        req.getRequestDispatcher("/editprofile.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int userId = getLoggedInUserId(req, resp);
        if (userId < 0) return;

        String name        = sanitize(req.getParameter("name"));
        String description = sanitize(req.getParameter("description"));
        String serviceType = sanitize(req.getParameter("serviceType"));
        String contactInfo = sanitize(req.getParameter("contactInfo"));
        String hours       = sanitize(req.getParameter("hours"));
        String location    = sanitize(req.getParameter("location"));

        byte[] photoBytes = null;
        String photoType  = null;
        Part photoPart = req.getPart("photo");
        if (photoPart != null && photoPart.getSize() > 0) {
            photoType = photoPart.getContentType();
            if (!photoType.startsWith("image/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Uploaded file is not an image.");
                return;
            }
            try (InputStream is = photoPart.getInputStream()) {
                photoBytes = is.readAllBytes();
            }
        }

        boolean removePhoto = "true".equalsIgnoreCase(req.getParameter("removePhoto"));

        try (Connection conn = getConnection()) {
            if (profileExists(conn, userId)) {
                updateProfile(conn, userId, name, description, serviceType,
                              contactInfo, hours, location, photoBytes, photoType, removePhoto);
            } else {
                insertProfile(conn, userId, name, description, serviceType,
                              contactInfo, hours, location, photoBytes, photoType);
            }
        } catch (SQLException e) {
            log("ProfilesServlet.doPost: DB error", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not save profile.");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/editprofile?redirect=view");
    }

    private void servePhoto(int userId, HttpServletResponse resp) throws IOException {
        try (Connection conn = getConnection()) {
            String sql = "SELECT photo, photo_type FROM profiles WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        byte[] photo = rs.getBytes("photo");
                        String type  = rs.getString("photo_type");
                        if (photo != null && type != null) {
                            resp.setContentType(type);
                            resp.setContentLength(photo.length);
                            resp.getOutputStream().write(photo);
                            return;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log("ProfilesServlet.servePhoto: DB error", e);
        }
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private boolean profileExists(Connection conn, int userId) throws SQLException {
        String sql = "SELECT 1 FROM profiles WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void insertProfile(Connection conn, int userId,
                               String name, String description, String serviceType,
                               String contactInfo, String hours, String location,
                               byte[] photo, String photoType) throws SQLException {
        String sql = "INSERT INTO profiles (user_id, name, description, service_type, " +
                     "contact_info, hours, location, status, photo, photo_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'pending', ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setString(4, serviceType);
            ps.setString(5, contactInfo);
            ps.setString(6, hours);
            ps.setString(7, location);
            setPhotoParams(ps, 8, 9, photo, photoType);
            ps.executeUpdate();
        }
    }

    private void updateProfile(Connection conn, int userId,
                               String name, String description, String serviceType,
                               String contactInfo, String hours, String location,
                               byte[] photo, String photoType,
                               boolean removePhoto) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "UPDATE profiles SET name=?, description=?, service_type=?, " +
            "contact_info=?, hours=?, location=?");

        boolean changePhoto = removePhoto || photo != null;
        if (changePhoto) sql.append(", photo=?, photo_type=?");
        sql.append(" WHERE user_id=?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, serviceType);
            ps.setString(4, contactInfo);
            ps.setString(5, hours);
            ps.setString(6, location);
            int next = 7;
            if (changePhoto) {
                setPhotoParams(ps, next, next + 1, removePhoto ? null : photo,
                               removePhoto ? null : photoType);
                next += 2;
            }
            ps.setInt(next, userId);
            ps.executeUpdate();
        }
    }

    private void setPhotoParams(PreparedStatement ps, int photoIdx, int typeIdx,
                                byte[] photo, String photoType) throws SQLException {
        if (photo != null) {
            ps.setBytes(photoIdx, photo);
            ps.setString(typeIdx, photoType);
        } else {
            ps.setNull(photoIdx, Types.BLOB);
            ps.setNull(typeIdx, Types.VARCHAR);
        }
    }

    private String toJson(Object value) {
        if (value == null) return "null";
        return "\"" + value.toString()
                           .replace("\\", "\\\\")
                           .replace("\"", "\\\"")
                           .replace("\n", "\\n")
                           .replace("\r", "\\r") + "\"";
    }

    private int getLoggedInUserId(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	return 1;
    	/*
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return -1;
        }
        return (int) session.getAttribute("userId");
        */
    }

    private String sanitize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}