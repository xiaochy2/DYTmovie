import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*; 

public class viewItems extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
	Connection dbcon = DB.getConn();
	
	int t = 999;
	if (request.getParameter("t")!=null)
		t = Integer.valueOf(request.getParameter("t"));

	int TotalPrice = 0;
	int num = 0;
	String f = "0";
	String price = "0";

	 HttpSession Quantity = request.getSession(true);
	 int [] quan = (int [])Quantity.getAttribute("quan");
	 if (quan == null) {
	        quan = new int [100];
	       Quantity.setAttribute("quan", quan);
	       for (int j = 0; j<quan.length; j++)
	   		quan[j] = 1;
	      }
	String op = null;
	op = request.getParameter("oper"); 
	if (request.getParameter("f")!=null)
	f = request.getParameter("f");
    HttpSession session = request.getSession(true);    
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    ArrayList Items = (ArrayList)session.getAttribute("Items");
    if(f.equals("1"))
    	Items =null;
    if (Items == null) {
        Items = new ArrayList();
        session.setAttribute("Items", Items);
      }
    out.println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>Movie Information</title><meta charset=\"UTF-8\"><link rel=\"stylesheet\" href=\"../CheckOut/shopping_cart.css\"></head><body><h1 class=\"biaoti\">    &nbsp;DYTmovie Shopping Cart</h1><hr style=\"height:2px\"color=\"white\"><div id=\"navbar\">   <nobr><a href=\"../mainpage/mainpage.html\">Main Page</a></nobr> <br><br>    <nobr><a href=\"../Search/search.html\">Advanced Search</a></nobr> <br><br>    <nobr><a href=\"../browse/browse.html\">Browsing</a></nobr> <br><br>    <nobr><a href=\"../servlet/shopping_cart\">Shopping Cart</a></nobr><br><br><nobr><a href=\"../Login/Login.html\">Logout</a></nobr><br><br></div>");
    out.println("<div class=\"checkoutbox\">            <table cellpadding=\"10\">            <caption>Shopping Cart Information </caption>            <tr></tr>            <tr>                <td><strong>Item</strong></td>                <td>&nbsp;</td>                <td><strong>Quantity</strong>                </td><td>&nbsp;</td>                <td><strong>Price</strong></td>            </tr>");
    if (Items.size() !=0)
    for (int i = 0; i < Items.size(); i++)
	    {
	    	try
			{   
	    		Statement shop = dbcon.createStatement();			
	    		String a = (String)Items.get(i);
	    		ResultSet result = shop.executeQuery("Select * from movies where id =" + a );
	    		while (result.next())
	    			{
	    				if ( i == t && op.equals("1"))
	    				quan[i]++;
	    				if ( i == t && op.equals("2"))
	    				quan[i]--;
	    				if ( i == t && op.equals("0"))
	    				quan[i] = 0;
	    				if (quan[i]!=0)
	    				out.println("<tr><td>"+ result.getString(2) +"</td><td>&nbsp;</td><td>"+quan[i]+"</td><td>&nbsp;</td><td>"+quan[i]*20+"</td><td><a href = \"../servlet/shopping_cart?t=" + i +"&oper=1" + "\"><input type=\"button\"  id=\"shoppingcart\" value = \"Add\" style=\"height:30px; width:100px; background-color: yellow\"></a></td><td> <a href = \"../servlet/shopping_cart?t=" + i +"&oper=2" + "\"><input type=\"button\"  id=\"shoppingcart\" value = \"substract\" style=\"height:30px; width:100px; background-color: yellow\"></a></td><td><a href = \"../servlet/shopping_cart?t=" + i +"&oper=0" + "\"><input type=\"button\"  id=\"shoppingcart\" value = \"delete\" style=\"height:30px; width:100px; background-color: yellow\"></a></td></tr>");
	    				
	    			}
    		}
    		catch (SQLException s) {
				s.printStackTrace();
			}
	    }
    for (int i =0; i < Items.size(); i++){
    	TotalPrice = TotalPrice + quan[i]*20;
    }
    	
    out.println("<tr><td></td><td></td><td></td><td>Total Price</td>");
    out.println("<td>"+TotalPrice +"</td>");
    out.println("</tr><tr><td>");
    out.println("<a href = \"../servlet/shopping_cart?f=1\" ><input type=\"button\" value=\"clear\" style=\"height:21px;width:125px;\"/><br><br></a></td><td></td><td>");
    out.println("<a href = \"../CheckOut/CheckOut.html\" ><input type=\"button\" value=\"Proceed to Checkout\"><br><br></a></td>");
    
    out.println("</tr></table></div><div class=\"update\">");
    


   

   // The following two statements show how this thread can access an
   // object created by a thread of the ShowSession servlet
   // Integer accessCount = (Integer)session.getAttribute("accessCount");
   // out.println("<p>accessCount = " + accessCount);

   out.println("</div></BODY></HTML>");
  }
}