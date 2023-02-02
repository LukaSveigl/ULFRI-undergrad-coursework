import ConfigLoader.*;
import DBCore.DBAPI;
import Data.Data;
import Data.DataParcel;
import Data.DataJob;
import Data.DataStaff;
import Data.GeneralAddress;
import Utils.Logger;
import Utils.Utils;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class containing test cases.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        DBAPI api = new DBAPI(true);

        /*Logger log = new Logger("system.log");

        log.log("Test message!", Logger.MessageType.LOG);
        log.log("Test warning!", Logger.MessageType.WARNING);
        log.log("Test error!", Logger.MessageType.ERROR);

        System.out.println("Is connection established: " + api.isConnectionEstablished());*/

        Scanner scan = new Scanner(System.in);
        System.out.println("Username: ");
        String username = scan.nextLine();
        System.out.println("Password: ");
        String password = scan.nextLine();

        api.login(username, password);

        System.out.println("Is connection established: " + api.isConnectionEstablished());

        if (!api.isConnectionEstablished()) {
            System.out.println("Fail");
            return;
        }

        if (args[0].equals("--tests-run=true")) {
            configLoaderTests();
            DBAPITests();
        }
        /*try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }*/

        System.out.println(api.getCoordinatesFromAddress(new GeneralAddress(0, "1000", "Ljubljana - dostava", "SVN")));
        System.out.println(api.getAllParcelCenterData().size());

        try {
            System.out.println(Utils.generateParcelID(api, "GER"));
            System.out.println(Utils.generateUsername(api, "", ""));
            System.out.println(Utils.generateUsername(api, "Majmun", "Bogoslavijević"));
        } catch (Exception e) {

        }

        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(Utils.generateParcelID(api, "SLV"));
            }
        } catch (Exception e) {

        }

        System.out.println("Testing getJobOfStaff()");
        ArrayList<DataJob> data = api.getJobsOfStaff("sheshut51");
        System.out.println(data.size());
        for (DataJob job : data) {
            System.out.println(job);
        }
        System.out.println("Testing getJobOfStaff() done");


        System.out.println("Testing getBranchID()");
        System.out.println(api.getBranchIDFromUsername("sheshut51"));
        System.out.println("Testing getBranchID() done");

        System.out.println(Utils.shortestPath(api,
                new GeneralAddress(0, "1000", "Ljubljana - dostava", "SVN"),
                //new GeneralAddress(0, "2000", "Maribor - dostava", "SVN")
                new GeneralAddress(0, "1230", "Wien, Liesing", "AUT")
        ));

        System.out.println(api.getStaffDataFromUsername("alfperr10"));


        ArrayList<DataStaff> dataStaffs = api.getWarehouseManagerEmployeesInfo("augdene34");
        for (DataStaff dataStaff : dataStaffs) {
            System.out.println(dataStaff);
        }

        System.out.println(api.getStaffDataFromUsername("augdene34"));

        ArrayList<DataParcel> dataSomething = api.getSentParcels("aarmatt59");

        api.logout();

        //Utils.shortestPath(api, "a", "n");
    }

    /**
     * Tests for the configuration loader.
     */
    static void configLoaderTests() {
        ConfigLoader cfgldr = new ConfigLoader(true);
        cfgldr.load();
        DBData data = cfgldr.fetchData();

        System.out.println("ConfigLoader tests:");

        if (data == null) {
            System.out.println("Test 1.1 not passed.");
            System.out.println("Data is null.");
        }
        else {
            System.out.println("Test 1.1 passed.");
        }

        if (!data.toString().equals("[Name = test_tpo, ip = 127.0.0.1, port = 3306]")) {
            System.out.println("Test 1.2 not passed.");
            System.out.println("Expected: " + "[Name = test_tpo, ip = 127.0.0.1, port = 3306]" + " || Result: " + data);
        }
        else {
            System.out.println("Test 1.2 passed.");
        }
    }

    /**
     * Test for the database api.
     */
    static void DBAPITests() {
        DBAPI dbapi = new DBAPI(true);

        System.out.println("DBAPI tests:");

        dbapi.login("tpo-tester", "password");

        if (!dbapi.isConnectionEstablished()) {
            System.out.println("Test 2.1 not passed.");
            System.out.println("Connection is not established.");
        }
        else {
            System.out.println("Test 2.1 passed.");
        }

        dbapi.logout();

        if (dbapi.isConnectionEstablished()) {
            System.out.println("Test 2.2 not passed.");
            System.out.println("Connection is established.");
        }
        else {
            System.out.println("Test 2.2 passed.");
        }
    }
}
