import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Call extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	String [] a = new String[2];
	Connection dbcon = DB.getConn();
	PrintWriter out = response.getWriter();
	try
    {
		PreparedStatement addMovies = DB.prepareStmt(dbcon, "call add_movie(?,?,?,?,?,?,?,?,?,?)");
		String name = request.getParameter("star");
		String dob = request.getParameter("dob");
		String photo = request.getParameter("photo");
		
	    String  movie = request.getParameter("name");
	    String year = request.getParameter("year");
	    String director = request.getParameter("director");
	    String banner = request.getParameter("banner");
	    String trailer = request.getParameter("trailer");

	    String genre = request.getParameter("genre");
	    if (name.contains(" "))
		a = name.split(" ");
		else {
			a[0] = "";
			a[1] = name;
		}
	    if(year.equals(""))
	    	year = "9999";
	    int y = Integer.parseInt(year);
	    response.setContentType("text/html");
	    
	    String title = "Employee Operation";
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
	    out.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                "<H1>" + title + "</H1>");
	    if (name.equals("")||movie.equals("")||genre.equals(""))
	    	out.write("<script language='javascript'>alert('Please input valid information!');window.location='Operation.html';</script>");
	    addMovies.setString(1, movie);
	    addMovies.setInt(2, y);
	    addMovies.setString(3, director);
	   	addMovies.setString(4, banner);
	   	addMovies.setString(5, trailer);
	   	addMovies.setString(6, a[0]);
	   	addMovies.setString(7, a[1]);
	   	if (!dob.contains("-")&&!dob.equals("")){
	   	   	out.write("<script language='javascript'>alert('wrong date of birth format');window.location='Operation.html';</script>");

	   	}
	   	else if (dob.equals("")	)
	   			addMovies.setString(8, "1999-09-09");	

	   	else
	   	addMovies.setString(8, dob);
	   	addMovies.setString(9, photo);
	   	addMovies.setString(10, genre);
	   	
	   	
	   	
	   	int retID = addMovies.executeUpdate();
	   	if (retID ==0 )
	   		out.write("<script language='javascript'>alert('the operation has been done');window.location='Operation.html';</script>");
	   	else
	   		out.write("<script language='javascript'>alert('add movie success');window.location='Operation.html';</script>");
	   	out.println("</BODY></HTML>");
    }
    catch (Exception e){
    	out.write("<script language='javascript'>alert('add movie failed');window.location='Operation.html';</script>");
    	//e.printStackTrace() ;
    }
   	
   // The following two statements show how this thread can access an
   // object created by a thread of the ShowSession servlet
   // Integer accessCount = (Integer)session.getAttribute("accessCount");
   // out.println("<p>accessCount = " + accessCount);

   
  }
}
