package com.pawport.servlets;

public class PawportWishlist {

	// devnote: I have been busy so I have not been able to make much progress. Procedures will start after the primary demo and other homework.
	public static void main(String[] args) {
		//Anything regarding processing the webpage or account
		getID();
		fetchWishlist();
		//displayWishlist
		

	}
	
	public static String getID() {
		//first, we need the account ID.
		//This is provided by the database, which we'll need to connect first
		
		
		// String uniqueID = request.getParameter("userName");
		//return uniqueID
		return null;
	}
	
	public static void fetchWishlist(){
		//the search should be something like this (example from form search)
		
		/*
		 String selectSQL = "SELECT * FROM myTable WHERE MYUSER LIKE ?";
         String theUserName = keyword + "%";
         preparedStatement = connection.prepareStatement(selectSQL);
         preparedStatement.setString(1, theUserName);
		 */
	}
	
	public static void displayWishlist() {
		
		// This is legitimately a html thing, unsure how to proceed.
 	}

}
