<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PawPort Login</title>
</head>
<body>
	<div class="main-container">
        <img src="Images/logoWithoutPawPort.png" alt="logoWithoutPawPort / PawPort Logo" width="100">
     </div>   
        
    <h2>Sign in to PawPort</h2>
    <form action="LoginServlet" method="POST">
        <table>
            <tr>
                <td>Email:</td>
                <td><input type="text" name="email" required /></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required /></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Login" /></td>
            </tr>
        </table>
    </form>
    <form>
    <tr>
        <td>
           <tr>
               <td></td>
                <p>New to PawPort? <a href="register.jsp">Register here</a></p>
		</td>
    </tr> 
    </form>
</body>
</html>