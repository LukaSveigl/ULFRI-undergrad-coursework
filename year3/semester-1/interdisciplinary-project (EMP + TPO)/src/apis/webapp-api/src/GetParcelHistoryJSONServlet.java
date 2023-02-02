import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
 
public class GetParcelHistoryJSONServlet extends HttpServlet{

    // http://localhost:17076/api/getParcelHistory?username=aarmatt59&password=password&parcelID=SVNXcZjO

    // public static void main(String[] args) {
    //     System.out.println(webAPI.getParcelHistoryJSON("aarmatt59", "password", "SVN2NE5O"));
    // }

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

      
      response.setContentType("text/json;charset=UTF-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();

      String parcelID = request.getParameter("parcelID"); 

      String json = webAPI.getParcelHistoryJSON(parcelID);
      out.println(json);

      out.close();  // Always close the output writer
   }
}