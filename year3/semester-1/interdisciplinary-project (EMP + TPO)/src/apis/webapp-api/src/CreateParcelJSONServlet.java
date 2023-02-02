import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
 
public class CreateParcelJSONServlet extends HttpServlet{

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

    // http://localhost:17076/api/createParcel?username=aarmatt59&password=password&parcelID=SVN&statusID=1&sender=aarmatt59&recipient=aarmatt59&weight=22.9&length=1&width=1&height=1

      
      response.setContentType("text/json;charset=UTF-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();

      String user = request.getParameter("username"); 
      String pass = request.getParameter("password"); 
      String parcelID = request.getParameter("parcelID"); // parcelID
      String statusID = request.getParameter("statusID"); // statusID
      String sender = request.getParameter("sender"); // sender
      String recipient = request.getParameter("recipient"); // recipient
      String weight = request.getParameter("weight"); // weight
      String length = request.getParameter("length"); // length
      String width = request.getParameter("width"); // width
      String height = request.getParameter("height"); // height
      System.out.println(user + " " + pass + " " + parcelID + " " + statusID + " " + sender + " " + recipient + " " + weight + " " + length + " " + width + " " + height);
      // String user = "aarmatt59";
      // String pass = "password";

    try {
        webAPI.createNewParcel(user, pass, "0", parcelID, statusID, sender, recipient, weight, length, width, height);
        out.println("{\"status\": \"Request processed.\"}");
        
    } catch (Exception e) {
        out.println("{\"status\": \"Error creating parcel.\"}");
        e.printStackTrace();
    }

      out.close();  // Always close the output writer
   }
}