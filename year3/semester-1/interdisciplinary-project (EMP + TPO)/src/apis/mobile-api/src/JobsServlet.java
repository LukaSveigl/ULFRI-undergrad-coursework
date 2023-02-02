import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
import Data.DataJob;
import Data.DataParcel;

// Compile with: javac -cp .;lib/core.jar;lib/servlet-api.jar -d WEB-INF\classes src/JobsServlet.java

public class JobsServlet extends HttpServlet {

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
        output.println("<h1>Jobs</h1>");

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

            ArrayList<DataJob> jobs = db.getJobsOfStaff(username);

            for (DataJob job : jobs) {
                for (String parcelID : job.parcelIDs) {
                    content.append(Integer.toString(job.jobID));
                    content.append(",");
                    content.append(job.jobTypeID);
                    content.append(",");
                    content.append(job.jobStatusID);
                    content.append(",");

                    DataParcel parcel = db.getParcelData(parcelID);
                    content.append(parcel.parcelID);
                    content.append(",");
                    content.append(Double.toString(parcel.weight));
                    content.append(",");
                    content.append(Integer.toString(parcel.dimensions.height) +
                            "x" +
                            Integer.toString(parcel.dimensions.width) +
                            "x" +
                            Integer.toString(parcel.dimensions.depth));
                    content.append(";");
                }
            }

            response.getWriter().append("success;" + content.toString());
        } else {
            response.getWriter().append("failure;none");
        }

        db.logout();
    }
}
