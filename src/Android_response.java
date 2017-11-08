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

public class Android_response extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Connection dbcon = DB.getConn();
		PreparedStatement pstmt = DB.prepareStmt(dbcon, "SELECT * from customers where email = ? and password = ?");

		PrintWriter out = response.getWriter();
		try {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {
				out.println("success");
			} else {
				out.println("failed");
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}

		out.close();
	}

}
