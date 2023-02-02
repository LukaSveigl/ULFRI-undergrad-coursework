import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
import Data.DataStaff;
import Data.DataCount;

// Compile with: javac -cp .;lib/core.jar;lib/servlet-api.jar -d WEB-INF\classes src/StatisticsServlet.java

public class StatisticsServlet extends HttpServlet {

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
        output.println("<h1>Statistics</h1>");

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

            StringBuilder content = new StringBuilder("");

            DataCount[] data = db.getWarehouseManagerWarehouseParcelInfo(username);

            content.append(data[0].value + ","); // Number of all parcels.
            content.append(data[1].value + ","); // Number of pending parcels.
            content.append(data[2].value + ","); // Number of completed parcels.

            ArrayList<DataStaff> staff = db.getWarehouseManagerEmployeesInfo(username);

            int drivers = (int) staff.stream().filter(s -> s.role.split(" ")[1].equals("driver")).count();
            int agents = (int) staff.stream().filter(s -> s.role.equals("Warehouse agent")).count();

            content.append(drivers + ","); // Number of drivers.
            content.append(agents); // Number of agents.

            response.getWriter().append("success;" + content.toString());
        } else {
            response.getWriter().append("failure;none");
        }

        db.logout();
    }
}