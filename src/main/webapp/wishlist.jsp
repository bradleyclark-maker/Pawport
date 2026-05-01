<%@ page import="java.util.List" %>
<%@ page import="com.pawport.servlets.WishlistItem" %>

<!DOCTYPE html>
<html>
<head>
    <title>Pawport Wishlist</title>
    <style>
        body {
            font-family: Arial;
            background: #f4f4f4;
        }

        .container {
            width: 60%;
            margin: auto;
            background: white;
            padding: 20px;
            border-radius: 10px;
        }

        h2 {
            color: #2b6cb0;
        }

        li {
            margin: 10px 0;
            padding: 10px;
            background: #e2e8f0;
            border-radius: 5px;
        }

        button {
            margin-left: 10px;
            background: #8b5e3c;
            color: white;
            border: none;
            padding: 5px;
            border-radius: 5px;
        }

        .message {
            color: green;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>Your Wishlist</h2>

    <!-- Feedback Message -->
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <div class="message"><%= message %></div>
    <%
        }
    %>

    <ul>
    <%
        List<WishlistItem> wishlist = (List<WishlistItem>) request.getAttribute("wishlist");

        if (wishlist != null && !wishlist.isEmpty()) {
            for (WishlistItem item : wishlist) {
    %>
        <li>
            <strong><%= item.getItemName() %></strong>
            (<%= item.getItemType() %> - <%= item.getLocation() %>)

            <form action="wishlist" method="post" style="display:inline;">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="itemId" value="<%= item.getItemId() %>">
                <button type="submit">Remove</button>
            </form>
        </li>
    <%
            }
        } else {
    %>
        <li>No items in the wishlist</li>
    <%
        }
    %>
    </ul>

    <hr>

    <!-- Add by Name -->
    <h3>Add Item by Name</h3>
    <form action="wishlist" method="post">
        <input type="hidden" name="action" value="add">

        <input type="text" name="itemName" placeholder="Enter item name" required>

        <button type="submit">Add</button>
    </form>

</div>

</body>
</html>