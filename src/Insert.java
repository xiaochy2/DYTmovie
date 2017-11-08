import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Insert extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	 String [] a = new String[2];
	Connection dbcon = DB.getConn();
	try
    {
	PreparedStatement insertStars = DB.prepareStmt(dbcon, "insert into stars (first_name,last_name,dob,photo_url) values(?,?,?,?)");
    String name = request.getParameter("name");
    String dob = request.getParameter("dob");
    String url = request.getParameter("url");
    if (name.contains(" "))
	a = name.split(" ");
	else {
		a = new String[2];
		a[0] = "";
		a[1] = name;}
    
   

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String title = "Employee Operation";
    String docType =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
      "Transitional//EN\">\n";
    out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1>" + title + "</H1>");
    if (name.equals(""))
    	out.write("<script language='javascript'>alert('Please input valid Information!');window.location='Operation.html';</script>");
    insertStars.setString(1, a[0]);
   	insertStars.setString(2, a[1]);

   	if (!dob.contains("-")&&!dob.equals("")){
   	   	out.write("<script language='javascript'>alert('wrong date of birth format');window.location='Operation.html';</script>");

   	}
   	else if (dob.equals("")	)
   			insertStars.setNull(3, Types.DATE)	;

   	else
   	insertStars.setString(3, dob);
   	insertStars.setString(4, url);
   	insertStars.executeUpdate();
   	out.write("<script language='javascript'>alert('insert success');window.location='Operation.html';</script>");
   	out.println("</BODY></HTML>");
    }
    catch (SQLException e){
    	
    }
   	
   // The following two statements show how this thread can access an
   // object created by a thread of the ShowSession servlet
   // Integer accessCount = (Integer)session.getAttribute("accessCount");
   // out.println("<p>accessCount = " + accessCount);

   
  }
}
