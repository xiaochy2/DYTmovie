import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*; 
import java.sql.*;

public class eLogin extends HttpServlet {
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	  

	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	   
	        
	     

	    Connection dbcon = DB.getConn();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Login</TITLE></HEAD>");
		out.println("<BODY>");
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT * from employees where email = ? and password = ?");

		try {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				
				response.sendRedirect("Operation.html");
				
			} else {
				out.write("<script language='javascript'>alert('Wrong username or password');window.location='Login/Login.html';</script>");
				
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
		out.println("</BODY>");
		out.println("</HTML>");
  }
}