
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;

public class AjaxSearch extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection dbcon = DB.getConn();
		response.setContentType("text/xml");
		String title = request.getParameter("title");
		StringBuffer a = new StringBuffer();
		 String [] s = new String[2];
		PrintWriter out = response.getWriter();
		int i = 0;
		//out.write("<msg>nice!<msg>perf!</msg></msg>");
		
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT title from movies where match(title) against (? in boolean mode)");
		
		if (!title.contains(" "))
			a.append("+").append(title);
			else { 
				s = title.split(" ");
				a.append("+").append(s[0]).append(" +").append(s[1]).append("*");
				}
				
		try{
			String b = a.toString();
			pstmt.setString(1, b);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()){
				out.write("<msg>");
				System.out.println(rs.getString(1));
				out.write(rs.getString(1));
				i++;
			}
			while (i>0){
				out.write("</msg>");
				i--;
			}
		}
		catch(Exception e) {
			out.write("<msg>"+ title +"</msg>");
			e.printStackTrace();
		}
		
	}
}