import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Android_search extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String title = request.getParameter("title");

		Connection dbcon = DB.getConn();

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		StringBuffer a = new StringBuffer();
		String[] s = new String[2];

		// int i = 0;

		PreparedStatement pstmt = DB.prepareStmt(dbcon,
				"SELECT title from movies where match(title) against (? in boolean mode)");

		if (!title.contains(" "))
			a.append("+").append(title);
		else {
			s = title.split(" ");
			a.append("+").append(s[0]).append(" +").append(s[1]).append("*");
		}

		try {
			String b = a.toString();
			pstmt.setString(1, b);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				rs = pstmt.executeQuery();

				while (rs.next()) {

					out.println(rs.getString(1));

					// i++;
				}
			} else {
				out.println("Movie not found");
			}
			/*
			 * while (i>0){ out.write("</msg>"); i--; }
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}

		out.close();

	}
}
