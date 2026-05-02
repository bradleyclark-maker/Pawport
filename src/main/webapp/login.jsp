<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PawPort Login</title>
<style>
/* 1. Fill the entire browser screen */
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f4f7f6;
}

/* 2. The Magic: Centers everything vertically and horizontally */
.viewport-wrapper {
	display: flex;
	flex-direction: column; /* Stacks children one above the other */
	justify-content: center; /* Vertical center */
	align-items: center; /* Horizontal center */
	min-height: 100vh; /* Uses 100% of the screen height */
	padding: 20px;
}

/* 3. A clean white box for the content */
.login-card {
	background: white;
	padding: 30px;
	border-radius: 12px;
	box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
	text-align: center;
	width: 100%;
	max-width: 350px; /* Limits the width for a better look */
}

.logo {
	margin-bottom: 10px;
}

h2 {
	color: #333;
	margin-bottom: 25px;
}

/* 4. Center the table and inputs */
table {
	margin: 0 auto; /* Centers the table within the card */
	width: 100%;
}

td {
	padding: 8px 0;
}

input[type="text"], input[type="password"] {
	width: 100%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 6px;
	box-sizing: border-box; /* Prevents inputs from overflowing */
}

input[type="submit"] {
	width: 100%;
	padding: 12px;
	background-color: #2c3e50; /* A nice professional dark blue/grey */
	color: white;
	border: none;
	border-radius: 6px;
	cursor: pointer;
	font-weight: bold;
	margin-top: 10px;
}

input[type="submit"]:hover {
	background-color: #1a252f;
}

.footer-link {
	margin-top: 20px;
	font-size: 0.9em;
	color: #666;
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

	<div class="viewport-wrapper">
		<div class="login-card">

			<img src="Images/logoWithPawPort.png" alt="PawPort Logo" width="100"
				class="logo">

			<h2>Sign in to PawPort</h2>

			<%
			String success = request.getParameter("success");
			if ("AccountCreated".equals(success)) {
			%>
			<div style="color: green;">Registration successful! Please log
				in.</div>
			<%
			}
			%>

			<form action="login" method="POST">
				<table>
					<tr>
						<td style="text-align: left;">Email</td>
					</tr>
					<tr>
						<td><input type="text" name="email"
							placeholder="Enter your email" required /></td>
					</tr>
					<tr>
						<td style="text-align: left;">Password</td>
					</tr>
					<tr>
						<td><input type="password" name="password"
							placeholder="Enter your password" required /></td>
					</tr>
					<tr>
						<td><input type="submit" value="Login" /></td>
					</tr>
				</table>
			</form>

			<p class="footer-link">
				New to PawPort? <a href="register.jsp">Register here</a>
			</p>

		</div>
	</div>

</body>
</html>