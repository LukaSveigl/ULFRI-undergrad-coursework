import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
 
public class GetSentParcelsJSONServlet extends HttpServlet{


//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/json");
//        PrintWriter output = response.getWriter();


//        DBAPI db = new DBAPI(false);
//        db.login("aarmatt59", "password");

//        if (db.isConnectionEstablished()) {
//             System.out.println("Connection established");
//        } else {
//             System.out.println("Connection not established");
//        }
//        output.println("Logged in as Aarmatt59");

//        db.logout();
//    }
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

      
      // String username = request.getParameter("username");

      response.setContentType("text/json;charset=UTF-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();

      String user = request.getParameter("username"); // aarmatt59
      String pass = request.getParameter("password"); // password
      System.out.println("Username: " + user);
      System.out.println("Password: " + pass);
      // String user = "aarmatt59";
      // String pass = "password";

      String json = webAPI.getSentParcelsJSON(user, pass);
      out.println(json);

      out.close();  // Always close the output writer
   }
}