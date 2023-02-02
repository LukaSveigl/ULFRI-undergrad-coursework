import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Objects;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import DBCore.DBAPI;
import Data.DataJob;
import Data.DataParcel;
import Data.DataStaff;
import Data.DataParcelCenter;
import Data.GeneralAddress;
import Data.SpecificAddress;
import Utils.Utils;

// Compile with: javac -cp .;lib/core.jar;lib/servlet-api.jar -d WEB-INF\classes src/JobsUpdateServlet.java

public class JobsUpdateServlet extends HttpServlet {

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
        output.println("<h1>JobsUpdate</h1>");

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
        String jobID = request.getParameter("jobID");
        String parcelID = request.getParameter("parcelID");
        String newStatus = request.getParameter("newStatus");
        String role = request.getParameter("role");

        DBAPI db = new DBAPI(false);
        db.login(username, password);
        if (db.isConnectionEstablished()) {

            ArrayList<DataJob> jobs = db.getJobsOfStaff(username);

            DataJob tmp = null;

            for (DataJob job : jobs) {
                if (job.jobID == Integer.parseInt(jobID)) {
                    tmp = job;
                    break;
                }
            }

            if (tmp.jobStatusID.equals("completed") || tmp.jobStatusID.equals("cancelled")) {
                response.getWriter().append("success;");
                return;
            }

            if (role.equals("Warehouse agent")) {
                if (tmp.jobTypeID.equals("Check in")) {
                    int branchID = db.getBranchIDFromUsername(username).value;
                    GeneralAddress address = db.getBranchAddress(branchID);
                    DataParcel parcel = db.getParcelData(parcelID);

                    SpecificAddress finalAddress = parcel.recipient.address;
                    GeneralAddress finalAddressGeneral = new GeneralAddress(
                            0,
                            finalAddress.postCode,
                            finalAddress.cityName,
                            finalAddress.countryISO);

                    ArrayList<DataParcelCenter> centers = Utils.shortestPath(db, address, finalAddressGeneral);

                    boolean jobCreated = false;

                    int finalCenterID = 0;
                    if (centers.size() == 0) {
                        // Move warehouse to delivery
                        MoveJobFromWarehouseToDelivery(db, branchID, Integer.parseInt(jobID), parcelID, username);
                        jobCreated = true;
                    } else {
                        finalCenterID = Integer.parseInt(centers.get(centers.size() - 1).id);
                    }

                    if (!jobCreated) {
                        if (finalCenterID == branchID) {
                            // Move warehouse to delivery
                            MoveJobFromWarehouseToDelivery(db, branchID, Integer.parseInt(jobID), parcelID, username);
                        } else {
                            // Move warehouse to international
                            MoveJobFromWarehouseToInternational(db, branchID, Integer.parseInt(jobID), parcelID,
                                    username);
                        }
                    }
                }
            } else if (role.equals("Delivery driver")) {
                int branchID = db.getBranchIDFromUsername(username).value;
                GeneralAddress address = db.getBranchAddress(branchID);
                DataParcel parcel = db.getParcelData(parcelID);

                if (tmp.jobTypeID.equals("Handover")) {
                    // Move delivery to warehouse
                    MoveJobFromDeliveryToWarehouse(db, branchID, Integer.parseInt(jobID), parcelID, username);
                } else if (tmp.jobTypeID.equals("Delivery cargo confirmation")) {
                    // Move delivery to delivery
                    MoveJobFromDeliveryToDelivery(db, branchID, Integer.parseInt(jobID), parcelID, username);
                }
            } else if (role.equals("International driver")) {
                int branchID = db.getBranchIDFromUsername(username).value;
                GeneralAddress address = db.getBranchAddress(branchID);
                DataParcel parcel = db.getParcelData(parcelID);

                if (tmp.jobTypeID.equals("Cargo departing confirmation")) {
                    // Move international to international
                    MoveJobFromInternationalToInternational(db, branchID, Integer.parseInt(jobID), parcelID, username);
                } else {
                    // Move international to warehouse
                    MoveJobFromInternationalToWarehouse(db, branchID, Integer.parseInt(jobID), parcelID, username);
                }
            }

            db.updateJobStatus(Integer.parseInt(jobID), Integer.parseInt(newStatus));

            response.getWriter().append("success;");
        } else {
            response.getWriter().append("failure;");
        }

        db.logout();
    }

    private void MoveJobFromWarehouseToDelivery(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        ArrayList<DataStaff> deliveryDrivers = dbapi.getAllEmployeesWithRoleAtBranch(branchID, 4);
        DataStaff deliveryDriver = deliveryDrivers.get(new Random().nextInt(deliveryDrivers.size()));
        int newJobID = dbapi.createJob(7, 1, deliveryDriver.username).value;
        dbapi.linkJobAndParcel(parcelID, newJobID);
    }

    private void MoveJobFromWarehouseToInternational(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        GeneralAddress branchAddress = dbapi.getBranchAddress(branchID);
        int headOfficeID = dbapi.getBranchOffice(branchAddress.countryISO).value;
        ArrayList<DataStaff> internationalDrivers = dbapi.getAllEmployeesWithRoleAtBranch(headOfficeID, 5);

        DataStaff internationalDriver = internationalDrivers.get(new Random().nextInt(internationalDrivers.size()));
        int newJobID = dbapi.createJob(5, 1, internationalDriver.username).value;
        dbapi.linkJobAndParcel(parcelID, newJobID);
    }

    private void MoveJobFromDeliveryToWarehouse(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        ArrayList<DataStaff> warehouseAgents = dbapi.getAllEmployeesWithRoleAtBranch(branchID,
                3);
        DataStaff warehouseAgent = warehouseAgents.get(new Random().nextInt(warehouseAgents.size()));
        int newJobID = dbapi.createJob(3, 1, warehouseAgent.username).value;
        dbapi.linkJobAndParcel(parcelID, newJobID);
    }

    private void MoveJobFromDeliveryToDelivery(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        ArrayList<DataJob> jobs = dbapi.getJobsOfStaff(username);
        DataJob tmp = null;
        for (DataJob job : jobs) {
            if (job.jobID == jobID) {
                tmp = job;
                break;
            }
        }

        int newJobID = dbapi.createJob(8, 1, username).value;

        ArrayList<DataParcel> parcels = new ArrayList<>();
        for (String jobParcelID : Objects.requireNonNull(tmp).parcelIDs) {
            dbapi.linkJobAndParcel(jobParcelID, newJobID);
        }
    }

    private void MoveJobFromInternationalToInternational(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        ArrayList<DataJob> jobs = dbapi.getJobsOfStaff(username);
        DataJob currentJob = null;
        HashMap<String, ArrayList<String>> parcelsOnBranch = new HashMap<>();
        for (DataJob job : jobs) {
            if (job.jobID == jobID) {
                currentJob = job;
                break;
            }
        }

        int newJobID = dbapi.createJob(6, 1, username).value;
        dbapi.linkJobAndParcel(parcelID, newJobID);
    }

    private void MoveJobFromInternationalToWarehouse(DBAPI dbapi, int branchID, int jobID, String parcelID,
            String username) {
        ArrayList<DataJob> jobs = dbapi.getJobsOfStaff(username);
        DataJob currentJob = null;
        HashMap<String, ArrayList<String>> parcelsOnBranch = new HashMap<>();
        for (DataJob job : jobs) {
            if (job.jobID == jobID) {
                currentJob = job;
                break;
            }
        }

        System.out.println("Here: ");

        GeneralAddress branchAddress = dbapi.getBranchAddress(branchID);
        SpecificAddress finalAddress = dbapi.getParcelData(parcelID).recipient.address;
        GeneralAddress finalAddressGeneral = new GeneralAddress(0, finalAddress.postCode,
                finalAddress.cityName, finalAddress.countryISO);

        System.out.println("Addressess calculated");

        List<String> visitedBranches = dbapi.getParcelLocations(parcelID).stream()
                .map(pl -> Integer.toString(pl.value)).collect(Collectors.toList());

        System.out.println("Visited branches calculated, lenght: " + visitedBranches.size());

        ArrayList<DataParcelCenter> parcelCenters = Utils.shortestPath(dbapi, branchAddress,
                finalAddressGeneral);

        System.out.println("Parcel centers calculated, lenght: " + parcelCenters.size());

        parcelCenters.removeIf(e -> visitedBranches.contains(e.id));

        System.out.println("Parcel centers filtered, lenght: " + parcelCenters.size());

        String nextBranchID = parcelCenters.get(0).id;

        System.out.println("Next branch ID: " + nextBranchID);

        ArrayList<DataStaff> warehouseAgents = dbapi.getAllEmployeesWithRoleAtBranch(
                Integer.parseInt(nextBranchID),
                3);

        System.out.println("Warehouse agents calculated, lenght: " + warehouseAgents.size());

        DataStaff warehouseAgent = warehouseAgents.get(new Random().nextInt(warehouseAgents.size()));

        System.out.println("Warehouse agent selected");

        int newJobID = dbapi.createJob(3, 1,
                warehouseAgent.username).value;

        System.out.println("New job created");

        dbapi.linkJobAndParcel(parcelID, newJobID);
    }
}