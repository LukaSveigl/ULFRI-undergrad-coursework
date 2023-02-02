import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
 
public class RegisterUserJSONServlet extends HttpServlet{

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {

    // http://localhost:17076/api/registerUser?username=dick&name=Dick&surname=Dickins&companyName=MegaDickINC&phoneNumber=123456789&streetName=DickStreet&streetNumber=99&postCode=1230&cityName=Domžale&countryISO=DNK

      
      response.setContentType("text/json;charset=UTF-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String companyName = request.getParameter("companyName");
        String phoneNumber = request.getParameter("phoneNumber");
        String streetName = request.getParameter("streetName");
        String streetNumber = request.getParameter("streetNumber");
        String postCode = request.getParameter("postCode");
        String cityName = request.getParameter("cityName");
        String countryISO = request.getParameter("countryISO");

    try {
        webAPI.createNewUser(username, name, surname, companyName, phoneNumber, streetName, streetNumber, postCode, cityName, countryISO);
        out.println("{\"status\": \"Request processed.\"}");
        
    } catch (Exception e) {
        out.println("{\"status\": \"Error creating user.\"}");
        e.printStackTrace();
    }

      out.close();  // Always close the output writer
   }
}