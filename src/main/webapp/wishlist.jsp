<%@ page import="java.util.List" %>
<%@ page import="com.pawport.WishlistItem" %>

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
    <li>No items in wishlist</li>
<%
    }
%>
</ul>