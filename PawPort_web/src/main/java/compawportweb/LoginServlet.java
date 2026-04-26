package compawportweb;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Extract the data from the request (Crucial step!)
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// 2. Simple null/empty check (Cybersecurity best practice)
		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
			response.sendRedirect("login.jsp?error=empty");
			return;
		}

		// 3. Check RDS Database for user via your Utility class
		// Note: Ensure DatabaseUtility is imported or in the same package
		boolean isAuthenticated = DatabaseUtility.verifyUser(email, password); 

		if (isAuthenticated) {
		    // 4. Create a session so search.html knows who is logged in
		    request.getSession().setAttribute("userEmail", email);
		    
		    // 5. Redirect to search.html
		    response.sendRedirect("search.html");
		} else {
		    // Redirect back with an error flag
		    response.sendRedirect("login.jsp?error=invalid");
		}
	}
}