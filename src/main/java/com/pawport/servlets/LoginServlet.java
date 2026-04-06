package com.pawport.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 1. Get data from your login.jsp
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    // 2. Simple check (Cybersecurity best practice)
	    if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
	        response.sendRedirect("login.jsp?error=empty");
	        return;
	    }

	    // 3. For now, let's just print it to the console to test the connection
	    System.out.println("Login attempt received for: " + email);

	    // 4. Eventually, your AWS Cognito code goes here to verify the user
	    
	    // ---------------------------
	}
}
