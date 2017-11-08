
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;

public class AjaxShow extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection dbcon = DB.getConn();
		response.setContentType("text/xml");
		String id = request.getParameter("id");	
		int i = 0;
		//out.write("<msg>nice!<msg>perf!</msg></msg>");
		PrintWriter out = response.getWriter();
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT * from movies where id = ?");
		PreparedStatement pstmt2 = DB.prepareStmt(dbcon, "select first_name,last_name from stars where id in (SELECT star_id from stars_in_movies where movie_id = ?)");
		
				
		try{
			
			pstmt.setString(1, id);
			pstmt2.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			ResultSet rs2 = pstmt2.executeQuery();
			while (rs.next()){
				
				out.write("<msg>");
				out.write(rs.getString(2));
				out.write("<msg>");
				out.write(rs.getString(3));
				out.write("<msg>");
				System.out.println(rs.getString(2));
				out.write(rs.getString(4));
				out.write("<msg>");
				
				out.write(rs.getString(5));
			
				
				
			
			}
			out.write("<msg>");
			while (rs2.next()){
				
				out.write(rs2.getString(1)+" ");
				out.write(rs2.getString(2));
			
			}
			out.write("</msg>");
			out.write("</msg>");
			out.write("</msg>");
			out.write("</msg>");
			out.write("</msg>");
		}
		catch(Exception e) {
			out.write("<msg>"+ id +"</msg>");
			e.printStackTrace();
		}
		
	}
}