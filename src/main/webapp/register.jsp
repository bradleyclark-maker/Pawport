<head>
    <title>PawPort - Create Account</title>
    <style>
        /* This lines up the labels and inputs */
        form div {
            display: grid;
            grid-template-columns: 150px 250px; /* Label width, then Input width */
            margin-bottom: 10px; /* Space between rows */
            align-items: center;
        }
        label {
            text-align: right;
            padding-right: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="main-container">
        <img src="Images/logoWithoutPawPort.png" alt="logoWithoutPawPort / PawPort Logo" width="100">
     </div>   
    <h2>Create Your PawPort Account</h2>
    <form action="RegisterServlet" method="POST">
        <div>
            <label>First Name:</label>
            <input type="text" name="regfname" required />
        </div>
        <div>
            <label>Last Name:</label>
            <input type="text" name="reglname" required />
        </div>
        <div>
            <label>Email:</label>
            <input type="email" name="regEmail" required />
        </div>
        <div>
            <label>Phone:</label>
            <input type="tel" name="regPhone" required />
        </div>
        <div>
            <label>Password:</label>
            <input type="password" 
           name="regPassword" 
           pattern="(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}" 
           title="Must be at least 8 characters, include 1 capital letter and 1 symbol (!@#$%^&*)" 
           required />
        </div>
        <div>
            <label>Confirm Password:</label>
            <input type="password" name="confirmPassword" required />
        </div>
        <div style="grid-column: 2;">
            <button type="submit">Sign Up</button>
        </div>
    </form>
    <br>
    <a href="login.jsp">Already have an account? Login here</a>
</body>