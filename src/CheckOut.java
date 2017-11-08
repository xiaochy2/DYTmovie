
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;

public class CheckOut extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection dbcon = DB.getConn();
		response.setContentType("text/html");
		HttpSession session = request.getSession(true);	
		ArrayList Items = (ArrayList)session.getAttribute("Items");
		if (Items == null) {
	        Items = new ArrayList();
	        session.setAttribute("Items", Items);			        
	      }
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Login</TITLE></HEAD>");
		out.println("<BODY>");
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT * from creditcards where id = ? and first_name = ? and last_name = ? and expiration = ?");
		PreparedStatement pstmt2 = DB.prepareStmt(dbcon, "SELECT * from customers where email = ? and password = ?");
		String username = request.getParameter("name");
		String credit = request.getParameter("cardnumber");
		String exp = request.getParameter("expiredate");
		
		HttpSession Login = request.getSession(true);
		ArrayList Info = (ArrayList)Login.getAttribute("Info");
		
		 
		try {
			if (Info == null) {
				out.write("<script language='javascript'>alert('please Login');window.location='../Login/Login.html';</script>");
			    }
				
			else
			{
				
				pstmt2.setString(1, (String)Info.get(0));
				pstmt2.setString(2, (String)Info.get(1));
				ResultSet rs2 = pstmt2.executeQuery();
					
			    if (rs2 != null &&rs2.next())
				{
						if(!username.contains(" "))
							
							out.write("<script language='javascript'>alert('Invalid Information');window.location='../CheckOut/CheckOut.html';</script>");
							
						else{
								String [] a = username.split(" ");
								pstmt.setString(1, credit);
								pstmt.setString(2, a[0]);
								pstmt.setString(3, a[1]);
								pstmt.setString(4, exp);
								ResultSet rs = pstmt.executeQuery();
								if (rs != null && rs.next()) {
									Items.clear();									
									response.sendRedirect("../CheckOut/thanks.html");
								} else {
									out.write("<script language='javascript'>alert('Invalid Information');window.location='../CheckOut/CheckOut.html';</script>");
								}
							}
				}	
			    else out.write("<script language='javascript'>alert('please Login');window.location='../Login/Login.html';</script>");
			}
			
		} catch (SQLException s) {
			s.printStackTrace();
		}
		out.println("</BODY>");
		out.println("</HTML>");

	}
}