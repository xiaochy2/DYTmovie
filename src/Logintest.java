import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*; 
import java.sql.*;

public class Logintest extends HttpServlet {
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	  String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
	  boolean valid = VerifyUtils.verify(gRecaptchaResponse);
	  
	   HttpSession Login = request.getSession();
	   HttpSession session = request.getSession(true);	
	    ArrayList Info = (ArrayList)Login.getAttribute("Info");
	    if (Info == null) {
	      Info = new ArrayList();
	      Login.setAttribute("Info", Info);
	    }
	    ArrayList Items = (ArrayList)session.getAttribute("Items");
		if (Items == null) {
	        Items = new ArrayList();
	        session.setAttribute("Items", Items);			        
	      }
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	   
	        
	     

	    Connection dbcon = DB.getConn();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		/*
		if (!valid) {
		    //errorString = "Recaptcha WRONG!";
			out.write("<script language='javascript'>alert('Captcha invalid!');window.location='../Login/Login.html';</script>");
		}
		*/
		
		out.println("<HTML><HEAD><TITLE>Login</TITLE></HEAD>");
		out.println("<BODY>");
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT * from customers where email = ? and password = ?");

		try {
			
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				Items.clear();
				Info.add(username);
		        Info.add(password);
				response.sendRedirect("../mainpage/mainpage.html");
				
			} else {
				out.write("<script language='javascript'>alert('Wrong username or password');window.location='../Login/Login.html';</script>");
				
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
		out.println("</BODY>");
		out.println("</HTML>");
  }
}