<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PawPort - Create Account</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f6;
        }

        .viewport-wrapper {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 40px 20px;
        }
        .register-card {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0,0,0,0.1);
            text-align: center;
            width: 100%;
            max-width: 400px;
        }

        .logo {
            margin-bottom: 10px;
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
            font-size: 1.5rem;
        }

        .form-group {
            text-align: left;
            margin-bottom: 15px;
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            font-size: 0.9rem;
            color: #555;
        }

        input {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            box-sizing: border-box;
            width: 100%;
        }

        button[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #2c3e50;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
            font-size: 1rem;
            margin-top: 10px;
        }

        button[type="submit"]:hover {
            background-color: #1a252f;
        }

        .footer-link {
            margin-top: 20px;
            font-size: 0.9rem;
            color: #666;
        }
    </style>
</head>
<body>

    <div class="viewport-wrapper">
        <div class="register-card">
            
            <img src="Images/logoWithPawPort.png" alt="PawPort Logo" width="100" class="logo">
            
            <h2>Create Your PawPort Account</h2>
        
            <form action="RegisterServlet" method="POST">
                
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" name="regfname" placeholder="First Name" required />
                </div>

                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" name="reglname" placeholder="Last Name" required />
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="regEmail" placeholder="email@example.com" required />
                </div>

                <div class="form-group">
                    <label>Phone</label>
                    <input type="tel" name="regPhone" placeholder="123-456-7890" required />
                </div>

                <div class="form-group">
                    <label>Password</label>
                    <input type="password" 
                           name="regPassword" 
                           pattern="(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}" 
                           title="8+ chars, 1 capital, 1 symbol" 
                           placeholder="Create password"
                           required />
                </div>

                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password" name="confirmPassword" placeholder="Repeat password" required />
                </div>

                <button type="submit">Sign Up</button>
            </form>

            <p class="footer-link">
                Already have an account? <a href="login.jsp">Login here</a>
            </p>
            
        </div>
    </div>

</body>
</html>