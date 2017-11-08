import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class star extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		
		Connection dbcon = DB.getConn();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Movie Information</title><meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"../movie/moviecss.css\"></head><body><h1 class=\"biaoti\">    &nbsp;DYTmovie</h1><hr style=\"height:2px\"color=\"white\"><div id=\"navbar\">    <nobr><a href=\"../mainpage/mainpage.html\">Main Page</a></nobr> <br><br>        <nobr><a href=\"../Search/search.html\">Advance Search</a></nobr> <br><br>        <nobr><a href=\"../browse/browse.html\">Browsing</a></nobr> <br><br>        <nobr><a href=\"../shopping_cart/shopping_cart.html\">Shopping Cart</a></nobr><br><br><nobr><a href=\"../CheckOut/CheckOut.html\">Check out</a></nobr><br><br><nobr><a href=\"../Login/Login.html\">Logout</a></nobr><br><br></div><div class=\"starinformation\">");
		PreparedStatement pstmt = DB.prepareStmt(dbcon,
				"SELECT * FROM stars where id = ?");
		try {
			pstmt.setString(1, id);
			

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String first_name = rs.getString(2);
				String last_name = rs.getString(3);
				String DOB = rs.getString(4);
				String banner_url = rs.getString(5);
				PreparedStatement movie = DB.prepareStmt(dbcon, "SELECT * FROM movies where id in (select movie_id from stars_in_movies where star_id in (select id from stars where first_name= ? and last_name = ?))");
				movie.setString(1, first_name);
				movie.setString(2, last_name);
				ResultSet result = movie.executeQuery();
				out.println("<table align = \"center\">" + "<tr>" + "<td rowspan = \"5\">" + "<img src = "
						+ banner_url  +" alt=\"banner not found\""+ " width=\"180\" height=\"250\"></td>"
						+ "<td height = \"50\" >id:</td>" + "<td>" + id + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"50\" >first name:</td>" + "<td>" + first_name + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"50\" >last name:</td>" + "<td>" + last_name + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"50\" >Date of birth:</td>" + "<td>" + DOB + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"50\" >list of movies:</td>");
				out.println("<td>"); 
				
				while(result.next()){
					String mv_id = result.getString(1);
					String mv_title = result.getString(2);
					out.println("<a href=movie?ID=" + mv_id + ">" + mv_title + "</a>");
					
				}
				out.println("</td>" + "</tr>");
				out.println("</table>");
				
			}
		} catch (SQLException e) {
			out.println("SQLException");
		} finally {
			out.println("</BODY>");
			out.println("</HTML>");
			DB.close(pstmt);
			
			DB.close(dbcon);
			out.close();
		}
		
	}
}
