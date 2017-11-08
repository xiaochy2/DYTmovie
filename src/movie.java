import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.ArrayList;
public class movie extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("ID");
		String cart = null;
		cart = request.getParameter("cart");
		if(cart!= null)
			{
				String item = id;
				HttpSession session = request.getSession(true);	
				ArrayList Items = (ArrayList)session.getAttribute("Items");
			    if (Items == null) {
			        Items = new ArrayList();
			        session.setAttribute("Items", Items);			        
			      }
			    Items.add(item);
			}
		Connection dbcon = DB.getConn();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Movie Information</title><meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"../movie/moviecss.css\"></head><body><h1 class=\"biaoti\">    &nbsp;DYTmovie</h1><hr style=\"height:2px\"color=\"white\"><div id=\"navbar\">    <nobr><a href=\"../mainpage/mainpage.html\">Main Page</a></nobr> <br><br>        <nobr><a href=\"../Search/search.html\">Advance Search</a></nobr> <br><br>        <nobr><a href=\"../browse/browse.html\">Browsing</a></nobr> <br><br>        <nobr><a href=\"../servlet/shopping_cart\">Shopping Cart</a></nobr><br><br><nobr><a href=\"../CheckOut/CheckOut.html\">Check out</a></nobr><br><br><nobr><a href=\"../Login/Login.html\">Logout</a></nobr><br><br></div><div class=\"starinformation\">");
		
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "select * from movies where id = ?");
		try {
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
			
			String title = rs.getString(2);
			String year = rs.getString(3);
			String director = rs.getString(4);
			String banner_url = rs.getString(5);
			String trailer = rs.getString(6);
			
			
				PreparedStatement genre = DB.prepareStmt(dbcon, "SELECT * FROM genres where id in (select genre_id from genres_in_movies where movie_id = ?)");
				genre.setString(1, id);
				ResultSet result = genre.executeQuery();
				PreparedStatement star = DB.prepareStmt(dbcon, "SELECT * FROM stars where id in (select star_id from stars_in_movies where movie_id = ?)");
				star.setString(1, id);
				ResultSet result_s = star.executeQuery();
				out.println("<table align = \"center\">" + "<tr>" + "<td rowspan = \"7\">" + "<img src = "
						+ banner_url + " alt=\"banner not found\"" + " width=\"200\" height=\"280\"></td>"
						+ "<td height = \"40\" >id:</td>" + "<td>" + id + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"40\" >title:</td>" + "<td>" + title + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"40\" >year:</td>" + "<td>" + year + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"40\" >director:</td>" + "<td>" + director + "</td>" + "</tr>" + "<tr>"
						+ "<td height = \"40\" >list of genre:</td>");
				out.println("<td>"); 
				while(result.next()){
					String genrename = result.getString(2);
					out.println(genrename);
				}
				out.println("</td>" + "</tr>");
				out.println("<tr>" + "<td height = \"30\" >list of stars:</td>" + "<td>");
				while(result_s.next()){
					String star_id = result_s.getString(1);
					String starfname = result_s.getString(2);
					String starlname = result_s.getString(3);
					out.println("<a href=star?id=" + star_id + ">" + starfname +" "+ starlname+" "
		+ "</a>");
					
				}
				out.println("</td>" + "</tr>");
				out.println("<tr>" + "<td height = \"40\">trailer:</td><td>");
				out.println("<a href =  \"" + trailer +"\">click here to display trailer</a>");
						 out.println("</td></tr></table>");
			}
			out.println("<a href = \"../servlet/movie?id=" + id +"&cart=1" + "\"><input type=\"button\"  id=\"shoppingcart\" value = \"Add to cart\" style=\"height:30px; width:100px; background-color: yellow\"></a>");
		} catch(SQLException s) {
			out.println("SQLException");
		}finally {
			out.println("</div>");
			out.println("</BODY>");
			out.println("</HTML>");
			DB.close(pstmt);
			
			DB.close(dbcon);
			out.close();
		}
		
	}
}
