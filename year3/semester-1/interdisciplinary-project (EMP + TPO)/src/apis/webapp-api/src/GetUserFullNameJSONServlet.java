import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
 
public class GetUserFullNameJSONServlet extends HttpServlet{

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

      
      response.setContentType("text/json;charset=UTF-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();

      String user = request.getParameter("username"); // aarmatt59
      String pass = request.getParameter("password"); // password
      // String user = "aarmatt59";
      // String pass = "password";

      String json = webAPI.getUserFullNameJSON(user, pass);
      out.println(json);

      out.close();  // Always close the output writer
   }
}