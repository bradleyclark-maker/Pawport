package com.pawport.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Servlet implementation class LoginServlet
 *
@WebServlet("/LoginServlet")
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Extract the data from the request
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// 2. Simple null/empty check
		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
			response.sendRedirect("login.jsp?error=empty");
			return;
		}

		// 3. Check RDS Database for user
		boolean isAuthenticated = DatabaseUtility.verifyUser(email, password); 

		if (isAuthenticated) {
		    // 4. Look up the userId so all servlets can use it from the session
		    int userId = DatabaseUtility.getUserIdByEmail(email);

		    // 5. Save both email and userId in the session
		    request.getSession().setAttribute("userEmail", email);
		    request.getSession().setAttribute("userId", userId);
		    
		    // 6. Redirect to search.html
		    response.sendRedirect("search.html");
		} else {
		    response.sendRedirect("login.jsp?error=invalid");
		}
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}