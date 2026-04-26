

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import compawportweb.S3Service;
import jakarta.servlet.annotation.MultipartConfig;

/**
 * Servlet implementation class DocumentVault
 */
@WebServlet("/DocumentVault")
@MultipartConfig(
		  fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
		  maxFileSize = 1024 * 1024 * 10,       // 10 MB
		  maxRequestSize = 1024 * 1024 * 100    // 100 MB
		)

public class DocumentVaultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private S3Service s3Service = new S3Service();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentVaultServlet() {
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
	    // 1. Get the file part from the JSP form
	    jakarta.servlet.http.Part filePart = request.getPart("file");
	    String category = request.getParameter("category");
	    
	    // Combine these into one line to avoid the "Duplicate Variable" error
	    String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

	    // 2. Logic to upload to S3...
	    try {
	        // You'll eventually call something like:
	        // s3Service.uploadFile(fileName, filePart.getInputStream());
	        
	        // 3. Logic to save metadata to MySQL (Calling your DatabaseUtility)
	        // dbUtility.saveDocumentRecord(fileName, category, s3Url);

	        System.out.println("Uploaded: " + fileName + " Category: " + category);
	        
	        // Redirect back to the vault with a success message
	        response.sendRedirect("documentVault.jsp?upload=success");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendRedirect("documentVault.jsp?upload=error");
	    }
	}

}
