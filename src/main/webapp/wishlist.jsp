<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.pawport.servlets.WishlistItem"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
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

.site-header {
	background: white;
	border-bottom: 1px solid #dbe4f0;
	position: sticky;
	top: 0;
	z-index: 10;
}

.navbar {
	width: min(1200px, 94%);
	margin: 0 auto;
	padding: 14px 0;
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 16px;
	flex-wrap: wrap;
}

.logo {
	font-size: 1.6rem;
	font-weight: 800;
	color: #2563eb;
}

.nav-links {
	display: flex;
	gap: 10px;
	flex-wrap: wrap;
}

.nav-links a {
	text-decoration: none;
	color: #1f2937;
	background: #f3f4f6;
	border: 1px solid #dbe4f0;
	padding: 10px 14px;
	border-radius: 12px;
	font-weight: 600;
}

.nav-links a:hover {
	background: #eff6ff;
	color: #2563eb;
}

.nav-links .login-btn {
	background: #2563eb;
	color: white;
	border-color: #2563eb;
}
</style>
</head>
<body>
	<header class="site-header">
		<nav class="navbar">
			<div class="logo">🐾 PawPort</div>

			<div class="nav-links">
				<a href="login" class="login-btn">Login</a> <a href="register.jsp">Register</a>
				<a href="search.html">Search</a> <a href="viewprofile.html">Profile</a>
				<a href="wishlist">Wishlist</a> <a href="Reminders.html">Reminders</a>
				<a href="documentVault.jsp">Vault</a>
			</div>
		</nav>
	</header>

	<div class="container">

		<h2>Your Wishlist</h2>

		<!-- Feedback Message -->
		<%
		String message = (String) request.getAttribute("message");
		if (message != null) {
		%>
		<div class="message"><%=message%></div>
		<%
		}
		%>

		<ul>
			<%
			List<WishlistItem> wishlist = (List<WishlistItem>) request.getAttribute("wishlist");

			if (wishlist != null && !wishlist.isEmpty()) {
				for (WishlistItem item : wishlist) {
			%>
			<li><strong><%=item.getItemName()%></strong> (<%=item.getItemType()%>
				- <%=item.getLocation()%>)

				<form action="wishlist" method="post" style="display: inline;">
					<input type="hidden" name="action" value="remove"> <input
						type="hidden" name="itemId" value="<%=item.getItemId()%>">
					<button type="submit">Remove</button>
				</form></li>
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
			<input type="hidden" name="action" value="add"> <input
				type="text" name="itemName" placeholder="Enter item name" required>

			<button type="submit">Add</button>
		</form>

	</div>

</body>
</html>