import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class MetaData extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	Connection dbcon = DB.getConn();
	try
    {
	DatabaseMetaData dbMeta = dbcon.getMetaData(); 
	Statement show = dbcon.createStatement();
	PreparedStatement insertStars = DB.prepareStmt(dbcon, "insert into stars (first_name,last_name,dob,photo_url) values(?,?,?,?)");
    String s = request.getParameter("table");
    
    

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
    
    	if (s.contains("movies")||s.contains("stars")||s.contains("genres")||s.contains("customers")||s.contains("sales")||s.contains("creditcards")||s.contains("employees"))
           	ShowMetaData(s, show, dbMeta,out);
           else out.write("<script language='javascript'>alert('wrong tablename');window.location='Operation.html';</script>");
    	 out.println("</BODY></HTML>");
    }
    catch (Exception e){
    	
    }
   	
   // The following two statements show how this thread can access an
   // object created by a thread of the ShowSession servlet
   // Integer accessCount = (Integer)session.getAttribute("accessCount");
   // out.println("<p>accessCount = " + accessCount);

  
  }
  
  
  public static void ShowMetaData (String  s, Statement show, DatabaseMetaData dbMeta,PrintWriter out) throws Exception{
 		
 		ResultSet result = show.executeQuery("Select * from " + s);
      ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, s);
      ResultSet imPSet = dbMeta.getPrimaryKeys(null, null, s);

      ResultSetMetaData metadata = result.getMetaData();
      out.println("Table name is " + metadata.getTableName(1)+"<br>");
      
	 	if( pkRSet.next() ) { 
					out.println("****** Comment ******<br>");
				    out.println("Primary key COLUMN_NAME: "+pkRSet.getObject(4)+"<br>");
					out.println("****** ******* ******"+"<br>");
			}
		else  out.println("There is no Primary key"+"<br>");  
		if ( imPSet.next() ) { 
					
				
					out.println("IMPORTED FORENGN KEY: "+imPSet.getObject(4)+"<br>");
		
					
					out.println("****** ******* ******"+"<br>");
			}
		else out.println("There is no Foreign key"+"<br>");

		
      out.println("There are " + metadata.getColumnCount() + " columns"+"<br>");
      for (int i = 1; i <= metadata.getColumnCount(); i++){
 			
 		out.println("Column " +i +" is "+ metadata.getColumnLabel(i)+"<br>");
        out.print("Type of column "+ i + " is " + metadata.getColumnTypeName(i) +"  ");

          if (metadata.isNullable(i) == 1){
          	out.print("   Nullable  ");
      	}
      	else out.print(" Not null");
      	
			if (metadata.isAutoIncrement(i))
				out.print("    Auto-Increment    ");
			
			out.println();
      }

 }
}
