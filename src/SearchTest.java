import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
import java.util.ArrayList;

public class SearchTest extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cart = null;
		cart = request.getParameter("cart");
		if(cart!= null&&!cart.equals("null"))
			{
				String item = request.getParameter("id");
				HttpSession session = request.getSession(true);	
				ArrayList Items = (ArrayList)session.getAttribute("Items");
			    if (Items == null) {
			        Items = new ArrayList();
			        session.setAttribute("Items", Items);			        
			      }
			    Items.add(item);
			    cart = null;
			}
		int marknum = 0;
		String capital ="";
		String capital_ ="";
		capital = request.getParameter("capital");
		if(capital != ""&&capital != null&&!capital.equals("null")) {
			marknum++;
			capital_ = add_(capital);
			capital_ = add("\"", capital_);
			capital_ = add(" title like ",capital_);
			
		}
		
		String ge ="";
		String ge_ ="";
		ge = request.getParameter("ge");
		if(ge != ""&& ge != null &&!ge.equals("null")) {
			
			marknum++;
			ge_ = add("\"", ge);
			ge_ = add(ge_,"\"");
			ge_ = add (" id IN (SELECT movie_id FROM genres_in_movies WHERE genre_id IN (SELECT id FROM genres WHERE name = ",ge_); 
			ge_ = add (ge_ , "))");
			
		}
		
		
		int pageSize = 5;
		int pageNow = 1;
		int pageCount = 0;
		int rowCount = 0;
		String sPageNow = request.getParameter("pageNow");

		if (sPageNow != null) {
			pageNow = Integer.parseInt(sPageNow);
		}
		
		String spageSize = request.getParameter("pageSize");

		if (spageSize != null) {
			pageSize = Integer.parseInt(spageSize);
		}
		
		String order = "0";
		order = request.getParameter("order");
		if(order == null) {
			order = "0";
		}
		
		String sort = request.getParameter("sort");
		if (sort == null) {
			sort = "id";
		}
		
		
		String _A_ = null;
		String A = request.getParameter("A");
		if(A != ""&& A != null&&!A.equals("null")) {
			marknum++;
			_A_ = _add_(A);
			//_A_ = add(" title like ",_A_);
			//_A_ = add(" ed(title, '",A);  //fuzzy search
			//_A_ = add(_A_,"')<=2 ");
			
			_A_ = " (title like "+_A_+" or ed(title, '"+A+"')<=2)";
			
		}
		String _B_ = null;
		String B = request.getParameter("B");
		if(B != ""&& B != null&&!B.equals("null")) {
			marknum++;
			_B_ = _add_(B);
			_B_ = add(" year like ",_B_);
			
		}
		String _C_ = null;
		String C = request.getParameter("C");
		if(C != ""&& C != null&&!C.equals("null")) {
			marknum++;
			_C_ = _add_(C);
			_C_ = " (director like "+_C_+" or ed(director, '"+C+"')<=2)";
			/*
			_C_ = add(" (director like ",_C_);
			_C_ = add(_C_," or");
			_C_ = add(" ed(director, '",A);
			*/
			
		}
		String _D_ = null;
		String[] E = null;
		String[] _E_;
		_E_ = new String[2];
		String _F_ = null;
		String D = request.getParameter("D");
		if (D != ""&& D != null&&!D.equals("null")){
			marknum++;
			
			if (D.contains(" ")) {
				E = D.split(" ");
				_E_[0] = _add_(E[0]);
				_E_[1] = _add_(E[1]);
				_F_ = add (" id IN (SELECT movie_id FROM stars_in_movies WHERE star_id IN (SELECT id FROM stars WHERE first_name like ",_E_[0]); 
				_F_ = add (_F_ ," and last_name like ");
				_F_ = add (_F_ , _E_[1]);
				_F_ = add (_F_ , "))");
			}
			else {
				_D_ = _add_(D);
				_F_ = add (" id IN (SELECT movie_id FROM stars_in_movies WHERE star_id IN (SELECT id FROM stars WHERE (first_name like ",_D_); 
				_F_ = _F_ + " or ed(first_name, '"+D+"')<=2)";
				
				
				_F_ = add (_F_ ," or (last_name like ");
				_F_ = _F_ + _D_;
				_F_ = _F_ + " or ed(last_name, '"+D+"')<=2)";
				//_F_ = add (_F_ , _D_);
				_F_ = add (_F_ , "))");
				
			}
		}
		
		
		
		
		Connection dbcon = DB.getConn();
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html><html lang=\"en\"><head>    <meta charset=\"UTF-8\">    <title>DYTmovie Search Result</title>    <link rel=\"stylesheet\" href=\"../movielist/movielistcss.css\"><meta name=\"description\" content=\"\"><link href=\"../bootstrap/twitter-bootstrap-v2/docs/assets/css/bootstrap.css\" rel=\"stylesheet\"></head><body><div><h1 class=\"biaoti\">    &nbsp;&nbsp;DYTmovie</h1>    <hr style=\"height:2px\"color=\"white\"></div><div id=\"navbar\">    <nobr><a href=\"../mainpage/mainpage.html\">Main Page</a></nobr> <br><br>    <nobr><a href=\"../Search/search.html\">Advanced Search</a></nobr> <br><br>    <nobr><a href=\"../browse/browse.html\">Browsing</a></nobr> <br><br>    <nobr><a href=\"../servlet/shopping_cart\">Shopping Cart</a></nobr><br><br><br><br><nobr><a href=\"../CheckOut/CheckOut.html\">Check out</a></nobr><br><br><nobr><a href=\"../Login/Login.html\">Logout</a></nobr><br><br></div>    <div>    <h2 class=\"movielisttitle\">        Result for your movie    </h2></div>");
		
		
		
		
		
		String prepare = "select * from movies where";
		
		if (A != ""&& A != null&&!A.equals("null")) {
			prepare = add(prepare, _A_);
			if(marknum>1) {
				prepare = add(prepare, "and");
				marknum--;
			}
		}
		if (B != ""&& B != null&&!B.equals("null")) {
			prepare = add(prepare, _B_);
			if(marknum>1) {
				prepare = add(prepare, "and");
				marknum--;
		}
		}
		if (C != ""&& C != null&&!C.equals("null")) {
			prepare = add(prepare, _C_);
			if(marknum>1) {
				prepare = add(prepare, "and");
				marknum--;
		}
		}
		if (D != ""&& D != null&&!D.equals("null")) {
			prepare = add(prepare, _F_);
			if(marknum>1) {
				prepare = add(prepare, "and");
				marknum--;
			}
		}
		if(capital != ""&&capital != null&&capital != "null") {
			
				prepare = add(prepare, capital_);
				marknum--;
			
		}
		
		if(ge != ""&&ge != null&&ge != "null") {
			
				prepare = add(prepare, ge_);
				marknum--;
			
		}
		prepare = add(prepare, " order by ");
		prepare = add(prepare, sort);
		if(order.equals("1")) {
			prepare = add(prepare, " desc");
		}
		
		
		
		Statement stmt = null;
		int count =0;
		try {
		stmt = dbcon.createStatement();
		
				
		
			
		System.out.println(prepare);
			ResultSet rs = stmt.executeQuery(prepare);
			if (rs != null && rs.next()) {
				rs = stmt.executeQuery(prepare);
				
				out.println("<div class=\"searchresult\">");
				
				while (rs.next()) {

					rowCount++;
					String id = rs.getString(1);
					String title = rs.getString(2);
					String year = rs.getString(3);
					String director = rs.getString(4);
					String banner_url = rs.getString(5);
					
					if (rowCount > pageSize * (pageNow - 1) && rowCount <= pageSize * pageNow) {
						count++;
						PreparedStatement genre = DB.prepareStmt(dbcon, "SELECT * FROM genres where id in (select genre_id from genres_in_movies where movie_id = ?)");
						genre.setString(1, id);
						ResultSet result = genre.executeQuery();
						PreparedStatement star = DB.prepareStmt(dbcon, "SELECT * FROM stars where id in (select star_id from stars_in_movies where movie_id = ?)");
						star.setString(1, id);
						ResultSet result_s = star.executeQuery();
						out.println("<div class=\"result"+count+"\">");
						out.println("<table>" + "<tr>" + "<td rowspan = \"7\">" + "<img src = "
								+ banner_url + " alt=\"banner not found\""  + " width=\"150\" height=\"210\"></td>"
								+ "<td height = \"30\" >id:</td>" + "<td>" + id + "</td>" + "</tr>" + "<tr>"
								+ "<td height = \"30\" >title:</td>" + "<td>" + "<a href=movie?ID=" + id + " id=" + id
								+ " class=\"btn btn-success\" rel=\"popover\" data-content=\"It's so simple to create a tooltop for my website!\" data-original-title=\"Popover\" onmouseover=\"over"+id+"()\">" + title+
										 "</a>" + "</td>" + "</tr>" + "<tr>"
								+ "<td height = \"30\" >year:</td>" + "<td>" + year + "</td>" + "</tr>" + "<tr>"
								+ "<td height = \"30\" >director:</td>" + "<td>" + director + "</td>" + "</tr>" + "<tr>"
								+ "<td height = \"30\" >list of genre:</td>");
						
						
						//<a href="#" id="905" class="btn btn-success" rel="popover" data-content="It's so simple to create a tooltop for my website!" data-original-title="Popover" onmouseover="over()">Star Wars: Episode III - Revenge of the Sith</a>
						
						
						
						
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
						out.println("</td>");
						out.println("</tr>");
						out.println("<tr>");
						out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a href = \"../servlet/Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort + "&order=" + order + "&capital=" + capital + "&ge=" + ge + "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=1"  + "&id=" + id + "\"><input type=\"button\" value=\"Add to Cart\" style=\"background:yellow;color:black ;width:100px \"></a></td>");
						out.println("</tr>");
						
								 out.println("</table></div>");
								 
								 
								 
								 out.print("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\"></script><script src=\"../bootstrap/twitter-bootstrap-v2/js/bootstrap-tooltip.js\"></script><script src=\"../bootstrap/twitter-bootstrap-v2/js/bootstrap-popover.js\"></script><script>function over"+id+"(){");   		
							
								 
								 out.print("var iden = $(\"#"
								 		+id+ "\").attr(\"id\");");
								 out.print("var url = \"../servlet/AjaxShow?id=\"+ iden;	var ajaxRequest;  	try{				ajaxRequest = new XMLHttpRequest();	} catch (e){				try{			ajaxRequest = new ActiveXObject(\"Msxml2.XMLHTTP\");		} catch (e) {			try{				ajaxRequest = new ActiveXObject(\"Microsoft.XMLHTTP\");			} catch (e){								alert(\"Your browser broke!\");				return false;			}		}	}	ajaxRequest.onreadystatechange = function(){			if(ajaxRequest.readyState == 4){						if(ajaxRequest.status == 200){																var html = 'Title:  '+ ajaxRequest.responseXML.getElementsByTagName(\"msg\")[0].childNodes[0].nodeValue + '<br>';				html += 'Year:  '+ ajaxRequest.responseXML.getElementsByTagName(\"msg\")[1].childNodes[0].nodeValue + '<br>';				html += 'Director: '+ ajaxRequest.responseXML.getElementsByTagName(\"msg\")[2].childNodes[0].nodeValue + '<br>';				html += 'Stars: '+ ajaxRequest.responseXML.getElementsByTagName(\"msg\")[4].childNodes[0].nodeValue + '<br>';				html += '<td rowspan = \"7\"><img src ='+ ajaxRequest.responseXML.getElementsByTagName(\"msg\")[3].childNodes[0].nodeValue +'alt=\"banner not found\" width=\"200\" height=\"280\"></td>'+'<br>';");
								 
								 //var iden = $("#905").attr("id");
								 
								  out.print("html += '<a href = \"../servlet/Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort + "&order=" + order + "&capital=" + capital + "&ge=" + ge + "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=1"  + "&id=" + id + "\">add to cart</a>';");
					
								out.println("$(\"#"+id+"\").attr(\"data-content\",html);"); 
								 out.println("$(\"#"+id+"\").popover({delay: {show: 100, hide: 3000}});}}};");
								 
								 out.println("ajaxRequest.open(\"GET\", url, true);ajaxRequest.send(null);}</script>");
					}

				}
					
				
				
				
				
				
				
				
				
				
				
	
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				if (rowCount % pageSize == 0) {
					pageCount = rowCount / pageSize;
				} else {
					pageCount = rowCount / pageSize + 1;
				}
				
				
			
out.println("<div class=\"result"+ ++count +"\">");
			if (pageNow == 1) {
				out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "forward"
						+ "</a>");
			} else {
				out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + (pageNow - 1) + "&pageSize=" + pageSize + "&cart=" + cart + ">"
						+ "forward" + "</a>");
			}
			if (pageCount <= 5) {
				for (int i = 1; i <= pageCount; i++) {
					out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + i + "&pageSize=" + pageSize + "&cart=" + cart + ">" + i + "</a>");
				}
			} else if (pageCount - pageNow <= 5) {
				for (int i = pageNow; i <= pageCount; i++)
					out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + i + "&pageSize=" + pageSize + "&cart=" + cart + ">" + i + "</a>");
			} else {
				for (int i = pageNow; i <= pageNow + 5; i++)
					out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + i + "&pageSize=" + pageSize + "&cart=" + cart + ">" + i + "</a>");
			}

			if (pageNow == pageCount) {
				out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "backward"
						+ "</a>");
			} else {
				out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&order=" + order +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + (pageNow + 1) + "&pageSize=" + pageSize + "&cart=" + cart + ">"
						+ "backward" + "</a>");
			}
			out.println("</div>");
			out.println("</div>");
			
			out.println("<div class=\"biaodan\">Sort by:<br>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=id" + "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "id"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=title" + "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "title"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=year" + "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "year"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=director" + "&capital="+ capital+ "&ge=" + ge +"&order=" + order + "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "director"
					+ "</a>");
			out.println("<br><br>Order:<br>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort + "&capital="+ capital+ "&ge=" + ge + "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "Ascend"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+"&order=1" +"&capital="+ capital+ "&ge=" + ge +  "&pageNow=" + pageNow + "&pageSize=" + pageSize + "&cart=" + cart + ">" + "Descend"
					+ "</a>");
			out.println("<br><br>Amount per page:<br>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=1" + "&pageSize=5" + "&cart=" + cart + ">" + "5"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=1" + "&pageSize=10" + "&cart=" + cart + ">" + "10"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&capital="+ capital+ "&ge=" + ge +"&order=" + order+ "&pageNow=1" + "&pageSize=15" + "&cart=" + cart + ">" + "15"
					+ "</a>");
			out.println("<a href=Search?A=" + A + "&B=" + B + "&C=" + C + "&D=" + D + "&sort=" + sort+ "&capital="+ capital+ "&ge=" + ge +"&order=" + order + "&pageNow=1" + "&pageSize=20" + "&cart=" + cart + ">" + "20"
					+ "</a>");
			} else {
				out.println("<div class=\"searchresult\">");
				out.println("<font size = \"6\">Movie not found</font>");
				out.println("</div>");
			}
		} catch (SQLException e) {
			out.println("<div class=\"searchresult\">");
			out.println("<font size = \"6\">Input cannot be null! </font>");
			out.println("</div>");
		} finally {
			out.println("</BODY>");
			out.println("</HTML>");
			DB.close(stmt);
			
			DB.close(dbcon);
			out.close();
		}
		

	}
	public String add(String front, String tail) {
		StringBuffer bufffront = new StringBuffer(front);
		bufffront.insert(front.length(), tail);
		String whole = bufffront.toString();
		return whole;
	}
	
	public String _add_(String middle) {
		StringBuffer buffermiddle = new StringBuffer(middle);
		buffermiddle.insert(middle.length(), "%\"");
		buffermiddle.insert(0, "\"%");
		String whole = buffermiddle.toString();
		return whole;
	}
	public String add_(String middle) {
		StringBuffer buffermiddle = new StringBuffer(middle);
		buffermiddle.insert(middle.length(), "%\"");
		String whole = buffermiddle.toString();
		return whole;
	}
}
