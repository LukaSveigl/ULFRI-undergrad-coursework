import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
import Data.DataStaff;

// Compile with: javac -cp .;lib/core.jar;lib/servlet-api.jar -d WEB-INF\classes src/LoginServlet.java

public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter output = response.getWriter();

        // WARNING: GET IS ONLY FOR TESTING PURPOSES

        output.println("<HTML>");
        output.println("<HEAD>");
        output.println("<TITLE>Servlet Testing</TITLE>");
        output.println("</HEAD>");
        output.println("<BODY>");
        output.println("<h1>Login</h1>");

        DBAPI db = new DBAPI(false);
        db.login("sheshut51", "password");

        if (db.isConnectionEstablished()) {
            output.println("Connection established");
        } else {
            output.println("Connection not established");
        }
        output.println("</BODY>");
        output.println("</HTML>");

        db.logout();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        DBAPI db = new DBAPI(false);
        db.login(username, password);
        if (db.isConnectionEstablished()) {
            DataStaff staff = db.getStaffDataFromUsername(username);
            response.getWriter().append("success," + staff.getFullName() + "," + staff.role);
        } else {
            response.getWriter().append("failure,none,none");
        }

        db.logout();
    }
}