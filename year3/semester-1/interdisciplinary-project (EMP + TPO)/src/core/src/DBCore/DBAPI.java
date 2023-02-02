package DBCore;

import Data.*;
import Utils.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * The public facing database access API.
 */
public class DBAPI {
    /** The database connection management core functionality. */
    private final DBCore core;
    /** The logger. */
    private final Logger logger;
    /** The current username. */
    private String currentUser;
    /** The list of precompiled prepared statements. */
    private final PreparedStatement [] statements;
    /** The list of precompiled callable statements. */
    private final CallableStatement [] callables;

    /**
     * Constructs a new database API instance.
     */
    public DBAPI(boolean log) {
        this.core = new DBCore(log);
        this.logger = new Logger(log);
        this.statements = new PreparedStatement[15];
        this.callables = new CallableStatement[35];
    }

    /**
     * Pre-compiles the prepared statements to be used later. This is to increase performance, as the statements
     * need to be compiled only once.
     */
    private void precompileStatements() {
        try {
            this.statements[0] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(id) AS idcount FROM parcel WHERE id = ?");
            this.statements[1] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(username) AS uscount FROM customer WHERE username = ?");
            this.statements[2] = this.core.getDbConnection()
                    .prepareStatement("SELECT COUNT(code) AS postCode FROM city WHERE code = ?");
            this.statements[3] = this.core.getDbConnection()
                    .prepareStatement("SELECT branch_id, latitude, longitude, country FROM parcel_center_locations");
            this.statements[4] = this.core.getDbConnection()
                    .prepareStatement("SELECT id AS id, name AS name FROM parcel_status");
            this.statements[5] = this.core.getDbConnection()
                    .prepareStatement("SELECT id AS id, name AS name FROM job_type");
            this.statements[6] = this.core.getDbConnection()
                    .prepareStatement("SELECT id AS id, name AS name FROM job_status");
            this.statements[7] = this.core.getDbConnection()
                    .prepareStatement("SELECT id AS id, name AS name FROM staff_role");
            this.statements[8] = this.core.getDbConnection()
                    .prepareStatement("SELECT id as id FROM branch WHERE name = ?");
            this.statements[9] = this.core.getDbConnection()
                    .prepareStatement("SELECT b.id as branch_id, COUNT(jp.parcel_id) as inbound_parcels\n" +
                            "    FROM branch b\n" +
                            "    INNER JOIN staff s on b.id = s.branch_id\n" +
                            "    INNER JOIN job j on s.username = j.staff_username\n" +
                            "    INNER JOIN job_packet jp on j.id = jp.job_id\n" +
                            "    WHERE j.job_status_id=1 AND j.job_type_id=3\n" +
                            "    GROUP BY b.id;");
            this.statements[10] = this.core.getDbConnection()
                    .prepareStatement("SELECT b.id as branch_id, COUNT(jp.parcel_id) as outbound_parcels\n" +
                            "    FROM branch b\n" +
                            "    INNER JOIN staff s on b.id = s.branch_id\n" +
                            "    INNER JOIN job j on s.username = j.staff_username\n" +
                            "    INNER JOIN job_packet jp on j.id = jp.job_id\n" +
                            "    WHERE j.job_status_id=2 AND j.job_type_id=3\n" +
                            "    GROUP BY b.id;");
            /*this.statements[11] = this.core.getDbConnection()
                    .prepareStatement("CREATE USER '?'@'%' IDENTIFIED BY 'password;");
            this.statements[12] = this.core.getDbConnection()
                    .prepareStatement("GRANT SELECT, INSERT, UPDATE ON *.* TO '?'@'%';");
            this.statements[13] = this.core.getDbConnection()
                    .prepareStatement("FLUSH PRIVILEGES;");*/
            // ... more statements.
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Pre-compiles the callable statements to be used later. This is to increase performance, as the statements
     * need to be compiled only once.
     */
    private void precompileCallables() {
        try {
            this.callables[0] = this.core.getDbConnection().prepareCall("CALL resolve_address(?, ?, ?, ?, ?)");
            this.callables[1] = this.core.getDbConnection().prepareCall("CALL user_info(?, ?, ?, ?)");
            this.callables[2] = this.core.getDbConnection().prepareCall("CALL get_jobs(?)");
            this.callables[3] = this.core.getDbConnection().prepareCall("CALL get_no_jobs(?, ?)");
            this.callables[4] = this.core.getDbConnection().prepareCall("CALL get_warehouse_parcel_info(?, ?, ? ,?)");
            this.callables[5] = this.core.getDbConnection().prepareCall("CALL get_jobs_filter_type(?, ?)");
            this.callables[6] = this.core.getDbConnection().prepareCall("CALL update_parcel_status(?, ?)");
            this.callables[7] = this.core.getDbConnection().prepareCall("CALL get_warehouse_employee_info(?)");
            this.callables[8] = this.core.getDbConnection().prepareCall("CALL branch_employees(?)");
            this.callables[9] = this.core.getDbConnection().prepareCall("CALL branch_delivery_drivers(?)");
            this.callables[10] = this.core.getDbConnection().prepareCall("CALL branch_international_drivers(?)");
            this.callables[11] = this.core.getDbConnection().prepareCall("CALL get_branches()");
            this.callables[12] = this.core.getDbConnection().prepareCall("CALL get_parcel(?)");
            this.callables[13] = this.core.getDbConnection().prepareCall("CALL get_warehouse_parcel_data(?)");
            this.callables[14] = this.core.getDbConnection().prepareCall("CALL get_branch_stats(?, ?, ?, ?, ?)");
            this.callables[15] = this.core.getDbConnection().prepareCall("CALL parcel_create(?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?)");
            this.callables[16] = this.core.getDbConnection().prepareCall("CALL customer_add(?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?)");
            this.callables[17] = this.core.getDbConnection().prepareCall("CALL update_job_status(?, ?)");
            this.callables[18] = this.core.getDbConnection().prepareCall("CALL branch_employee_lookup(?, ?)");
            this.callables[19] = this.core.getDbConnection().prepareCall("CALL get_branch_address(?)");
            this.callables[20] = this.core.getDbConnection().prepareCall("CALL get_branch_office(?, ?)");
            this.callables[21] = this.core.getDbConnection().prepareCall("CALL get_parcel(?)");
            this.callables[22] = this.core.getDbConnection().prepareCall("CALL branch_lookup(?)");
            this.callables[23] = this.core.getDbConnection().prepareCall("CALL job_create(?, ?, ?, ?)");
            this.callables[24] = this.core.getDbConnection().prepareCall("CALL link_parcel_job(?, ?)");
            this.callables[25] = this.core.getDbConnection().prepareCall("CALL get_job_type(?, ?)");
            this.callables[26] = this.core.getDbConnection().prepareCall("CALL get_parcel_locations(?)");
            this.callables[27] = this.core.getDbConnection().prepareCall("CALL get_parcel_sender(?)");
            this.callables[28] = this.core.getDbConnection().prepareCall("CALL get_parcel_recipient(?)");
            this.callables[29] = this.core.getDbConnection().prepareCall("CALL get_customer(?)");
            this.callables[30] = this.core.getDbConnection().prepareCall("CALL track_parcel(?)");
            // ... more callables.
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Logs into the database.
     * @param username String - The username.
     * @param password String - The password.
     */
    public void login(String username, String password) {
        this.logger.log("The user " + username + " is trying to log in.", Logger.MessageType.LOG);
        this.currentUser = username;
        this.core.login(username, password);
        if (this.core.isConnectionEstablished()) {
            this.precompileStatements();
            this.precompileCallables();
        }
    }

    /**
     * Logs out of the database.
     */
    public void logout() {
        this.logger.log("The user " + this.currentUser + " is trying to log out.",
                Logger.MessageType.LOG);
        this.core.logout();
        this.currentUser = "";
    }

    /**
     * Getter for the flag that signifies if the connection is established.
     * @return Boolean - True if connection is established, false otherwise.
     */
    public boolean isConnectionEstablished() {
        return this.core.isConnectionEstablished();
    }

    // PREPARED STATEMENTS

    /**
     * Gets the number of identical parcel IDs from database.
     * @param parcelID String - The parcel ID to check.
     * @return DataCount - Object representing the number of identical IDs.
     */
    public DataCount getCountOfIdenticalParcelIDs(String parcelID) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[0].setString(1, parcelID);
            ResultSet rs = this.statements[0].executeQuery();
            rs.next();
            count = new DataCount(0, rs.getInt("idcount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the number of identical usernames from database.
     * @param username String - The username to check.
     * @return DataCount - Object representing the number of identical usernames.
     */
    public DataCount getCountOfIdenticalUsernames(String username) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[1].setString(1, username);
            ResultSet rs = this.statements[1].executeQuery();
            rs.next();
            count = new DataCount(0,  rs.getInt("uscount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the number of identical post codes from database.
     * @param postCode String - The post code to check.
     * @return DataCount - Object representing the number of identical post codes.
     */
    public DataCount getCountOfIdenticalPostCodes(String postCode) {
        DataCount count = new DataCount(0, -1);
        try {
            this.statements[2].setString(1, postCode);
            ResultSet rs = this.statements[2].executeQuery();
            rs.next();
            count = new DataCount(0,  rs.getInt("uscount"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Gets the data of all parcel centers from database.
     * @return ArrayList<DataParcelCenter> - List of parcel center data objects.
     */
    public ArrayList<DataParcelCenter> getAllParcelCenterData() {
        ArrayList<DataParcelCenter> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[3].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataParcelCenter(
                        i,
                        rs.getString("branch_id"),
                        new Coordinates(
                                0,
                                Double.parseDouble(rs.getString("latitude")),
                                Double.parseDouble(rs.getString("longitude"))
                        ),
                        rs.getString("country")
                    ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets a list of all parcel statuses.
     * @return ArrayList<DataStatus> - The list of parcel statuses.
     */
    public ArrayList<DataStatus> getParcelStatuses() {
        ArrayList<DataStatus> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[4].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStatus(
                        i,
                        rs.getInt("id"),
                        rs.getString("name")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets a list of all job types.
     * @return ArrayList<DataStatus> - The list of job types.
     */
    public ArrayList<DataStatus> getJobTypes() {
        ArrayList<DataStatus> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[5].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStatus(
                        i,
                        rs.getInt("id"),
                        rs.getString("name")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets a list of all job statuses.
     * @return ArrayList<DataStatus> - The list of job statuses.
     */
    public ArrayList<DataStatus> getJobStatuses() {
        ArrayList<DataStatus> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[6].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStatus(
                        i,
                        rs.getInt("id"),
                        rs.getString("name")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets a list of all staff roles.
     * @return ArrayList<DataStatus> - The list of staff roles.
     */
    public ArrayList<DataStatus> getStaffRoles() {
        ArrayList<DataStatus> data = new ArrayList<>();
        try {
            ResultSet rs = this.statements[7].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStatus(
                        i,
                        rs.getInt("id"),
                        rs.getString("name")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    public String getBranchIDFromName(String name) {
        try {
            this.statements[8].setString(1, name);
            ResultSet rs = this.statements[8].executeQuery();
            rs.next();
            return Integer.toString(rs.getInt("id"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets the number of inbound parcels and number of all branches.
     * @return DataCount[] - First element is the number of inbound parcels, second element is the number of all branches.
     */
    public DataCount[] getCountOfAllInboundParcels() {
        DataCount[] data = {null, null};
        try {
            ResultSet rs = this.statements[9].executeQuery();
            int sum = 0;
            int i = 0;
            while (rs.next()) {
                i++;
                sum += rs.getInt("inbound_parcels");
            }
            data[0] = new DataCount(0, sum);
            data[1] = new DataCount(1, i);
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the number of outbound parcels and number of all branches.
     * @return DataCount[] - First element is the number of outbound parcels, second element is the number of all branches.
     */
    public DataCount[] getCountOfAllOutboundParcels() {
        DataCount[] data = {null, null};
        try {
            ResultSet rs = this.statements[10].executeQuery();
            int sum = 0;
            int i = 0;
            while (rs.next()) {
                i++;
                sum += rs.getInt("outbound_parcels");
            }
            data[0] = new DataCount(0, sum);
            data[1] = new DataCount(1, i);
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Creates a customer as user in database.
     * @param username String - The username of customer.
     */
    public void createCustomerInDatabase(String username) {
        try {
            /*this.statements[11].setString(1, username);
            this.statements[11].executeQuery();
            this.statements[12].setString(1, username);
            this.statements[12].executeQuery();
            this.statements[13].executeQuery();*/

            /*this.statements[11] = this.core.getDbConnection()
                    .prepareStatement("CREATE USER '?'@'%' IDENTIFIED BY 'password;");
            this.statements[12] = this.core.getDbConnection()
                    .prepareStatement("GRANT SELECT, INSERT, UPDATE ON *.* TO '?'@'%';");
            this.statements[13] = this.core.getDbConnection()
                    .prepareStatement("FLUSH PRIVILEGES;");*/

            String statement = "CREATE USER '" + username + "' IDENTIFIED BY 'password';";
            System.out.println(statement);
            core.getDbConnection().createStatement().execute(statement);
            statement = "GRANT EXECUTE, SELECT, INSERT, UPDATE ON *.* TO '" + username + "';";
            System.out.println(statement);
            core.getDbConnection().createStatement().execute(statement);
            core.getDbConnection().createStatement().execute("FLUSH PRIVILEGES;");
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    // CALLABLE STATEMENTS

    /**
     * Gets coordinates from a general address data object.
     * @param address GeneralAddress - The general address (post code, city name, country ISO code).
     * @return Coordinates - The coordinates of the address.
     */
    public Coordinates getCoordinatesFromAddress(GeneralAddress address) {
        Coordinates data = null;
        try {
            this.callables[0].setString("postcode", address.postCode);
            this.callables[0].setString("cityname", address.cityName);
            this.callables[0].setString("country", address.countryISO);
            this.callables[0].registerOutParameter("latitude", Types.VARCHAR);
            this.callables[0].registerOutParameter("longitude", Types.VARCHAR);
            this.callables[0].executeQuery();
            data = new Coordinates(
                    0,
                    Double.parseDouble(this.callables[0].getString("latitude")),
                    Double.parseDouble(this.callables[0].getString("longitude"))
            );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets staff data from staff username.
     * @param username String - The staff username.
     * @return DataStaff - The staff data (name, surname, role name).
     */
    public DataStaff getStaffDataFromUsername(String username) {
        DataStaff data = null;
        try {
            this.callables[1].setString("username", username);
            this.callables[1].registerOutParameter("u_name", Types.VARCHAR);
            this.callables[1].registerOutParameter("u_surname", Types.VARCHAR);
            this.callables[1].registerOutParameter("u_role", Types.VARCHAR);
            this.callables[1].executeQuery();
            data = new DataStaff(
                    0,
                    username,
                    this.callables[1].getString("u_name"),
                    this.callables[1].getString("u_surname"),
                    this.callables[1].getString("u_role")
            );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all jobs assigned to a given employee.
     * @param username String - The staff username.
     * @return ArrayList<DataJob> - The jobs of employee.
     */
    public ArrayList<DataJob> getJobsOfStaff(String username) {
        ArrayList<DataJob> data = new ArrayList<>();
        try {
            this.callables[2].setString("username", username);
            ResultSet rs = this.callables[2].executeQuery();
            int i = 0;
            while (rs.next()) {
                int currentJobID = rs.getInt("job_id");
                // Check if this job already exists.
                DataJob job = data.stream().filter(jobd -> jobd.jobID == currentJobID).findAny().orElse(null);
                // If the job doesn't exist, create it.
                if (job == null) {
                    data.add(new DataJob(
                            i,
                            currentJobID,
                            rs.getDate("date_created").toLocalDate(),
                            rs.getDate("date_completed") == null ? null : rs.getDate("date_completed").toLocalDate(),
                            rs.getString("job_type"),
                            rs.getString("job_status"),
                            username
                    ));
                    data.get(data.size() - 1).parcelIDs.add(rs.getString("parcel_id"));
                }
                // If the job already exists, add parcel to its list.
                else {
                    String currentParcelID = rs.getString("parcel_id");
                    if (job.parcelIDs.stream().filter(id -> id.equals(currentParcelID)).findAny().orElse(null) == null) {
                        job.parcelIDs.add(currentParcelID);
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the number of uncompleted jobs assigned to a given employee.
     * @param username String - The staff username.
     * @return DataCount - Object representing the number of uncompleted jobs.
     */
    public DataCount getCountOfUncompletedJobOfStaff(String username) {
        DataCount data = null;
        try {
            this.callables[3].setString("username", username);
            this.callables[3].registerOutParameter("no_of_jobs", Types.INTEGER);
            this.callables[3].executeQuery();
            data = new DataCount(0, this.callables[3].getInt("no_of_jobs"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the numbers of parcels that belong to the warehouse of a given warehouse manager.
     * @param username String - The warehouse manager username.
     * @return DataCount[] - Object representing the numbers of parcels (all, pending, processed).
     */
    public DataCount[] getWarehouseManagerWarehouseParcelInfo(String username) {
        DataCount[] data = {null, null, null};
        try {
            this.callables[4].setString("username", username);
            this.callables[4].registerOutParameter("no_of_all_parcels", Types.INTEGER);
            this.callables[4].registerOutParameter("no_of_pending_parcels", Types.INTEGER);
            this.callables[4].registerOutParameter("no_of_processed_parcels", Types.INTEGER);
            this.callables[4].executeQuery();
            data[0] = new DataCount(0, this.callables[4].getInt("no_of_all_parcels"));
            data[1] = new DataCount(1, this.callables[4].getInt("no_of_pending_parcels"));
            data[2] = new DataCount(2, this.callables[4].getInt("no_of_processed_parcels"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all jobs assigned to a given employee with given type.
     * @param username String - The staff username.
     * @param type String - The job type.
     * @return ArrayList<DataJob> - The jobs of employee.
     */
    public ArrayList<DataJob> getJobsOfStaffByType(String username, String type) {
        ArrayList<DataJob> data = new ArrayList<>();
        try {
            this.callables[5].setString("username", username);
            this.callables[5].setString("type", type);
            ResultSet rs = this.callables[5].executeQuery();
            int i = 0;
            while (rs.next()) {
                int currentJobID = rs.getInt("job_id");
                // Check if this job already exists.
                DataJob job = data.stream().filter(jobd -> jobd.jobID == currentJobID).findAny().orElse(null);
                // If the job doesn't exist, create it.
                if (job == null) {
                    data.add(new DataJob(
                            i,
                            currentJobID,
                            null,
                            null,
                            "",
                            "",
                            username
                    ));
                }
                // If the job already exists, add parcel to its list.
                else {
                    String currentParcelID = rs.getString("parcel_id");
                    if (job.parcelIDs.stream().filter(id -> id.equals(currentParcelID)).findAny().orElse(null) == null) {
                        job.parcelIDs.add(currentParcelID);
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Updates the parcel status for the given parcel.
     * @param parcelID String - The parcel ID.
     * @param newStatus String - The new parcel status.
     */
    public void updateParcelStatus(String parcelID, int newStatus) {
        try {
            this.callables[6].setString("parcel_id", parcelID);
            this.callables[6].setInt("new_status", newStatus);
            this.callables[6].executeQuery();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Gets all employees that work in warehouse of given warehouse manager.
     * @param username String - The warehouse manager username.
     * @return ArrayList<DataStaff> - The list of employees.
     */
    public ArrayList<DataStaff> getWarehouseManagerEmployeesInfo(String username) {
        ArrayList<DataStaff> data = new ArrayList<>();
        try {
            this.callables[7].setString("uname", username);
            ResultSet rs = this.callables[7].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStaff(
                        i,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("role")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all employees at given branch.
     * @param branchID int - The branch ID.
     * @return ArrayList<DataStaff> - The list of employees.
     */
    public ArrayList<DataStaff> getEmployeesAtBranch(int branchID) {
        ArrayList<DataStaff> data = new ArrayList<>();
        try {
            this.callables[8].setInt("branch_id", branchID);
            ResultSet rs = this.callables[8].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStaff(
                        i,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("role")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all delivery drivers at given branch.
     * @param branchID int - The branch ID.
     * @return ArrayList<DataStaff> - The list of delivery drivers.
     */
    public ArrayList<DataStaff> getDeliveryDriversAtBranch(int branchID) {
        ArrayList<DataStaff> data = new ArrayList<>();
        try {
            this.callables[9].setInt("branch_id", branchID);
            ResultSet rs = this.callables[9].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStaff(
                        i,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("role")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets all international drivers at given branch.
     * @param branchID int - The branch ID.
     * @return ArrayList<DataStaff> - The list of international drivers.
     */
    public ArrayList<DataStaff> getInternationalDriversAtBranch(int branchID) {
        ArrayList<DataStaff> data = new ArrayList<>();
        try {
            this.callables[10].setInt("branch_id", branchID);
            ResultSet rs = this.callables[10].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStaff(
                        i,
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("role")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the data of all branches.
     * @return ArrayList<DataBranch> - The list of branches.
     */
    public ArrayList<DataBranch> getBranches() {
        ArrayList<DataBranch> data = new ArrayList<>();
        try {
            ResultSet rs = this.callables[11].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataBranch(
                        i,
                        rs.getInt("bid"),
                        rs.getString("name"),
                        rs.getString("type")
                ));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the data of parcel with given ID.
     * @param parcelID String - The parcel ID.
     * @return DataParcel - The parcel data.
     */
    public DataParcel getParcelData(String parcelID) {
        DataParcel data = null;
        try {
            this.callables[12].setString("ID", parcelID);
            ResultSet rs = this.callables[12].executeQuery();
            rs.next();
            // Assemble the sender data.
            DataCustomer sender = new DataCustomer(
                    0, "", "", "", "", "",
                    new SpecificAddress(
                            0,
                            rs.getString("sender_street_name"),
                            rs.getInt("sender_street_num"),
                            rs.getString("sender_city_code"),
                            rs.getString("sender_city_name"),
                            rs.getString("sender_country_code")
                    )
            );
            // Assemble the recipient data.
            DataCustomer recipient = new DataCustomer(
                    0, "", "", "", "", "",
                    new SpecificAddress(
                            0,
                            rs.getString("recipient_street_name"),
                            rs.getInt("recipient_street_num"),
                            rs.getString("recipient_city_code"),
                            rs.getString("recipient_city_name"),
                            rs.getString("recipient_country_code")
                    )
            );

            data = new DataParcel(
                    0,
                    parcelID,
                    rs.getString("parcel_status"),
                    sender,
                    recipient,
                    rs.getInt("weight"),
                    new Dimensions(
                            0,
                            rs.getInt("height"),
                            rs.getInt("width"),
                            rs.getInt("depth")
                    )
            );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the data of all parcels at given warehouse manager's warehouse.
     * @param username String - The warehouse manager username.
     * @return ArrayList<DataParcel> - The list of parcels.
     */
    public ArrayList<DataParcel> getWarehouseManagerParcelData(String username) {
        ArrayList<DataParcel> data = new ArrayList<>();
        try {
            this.callables[13].setString("username", username);
            ResultSet rs = this.callables[13].executeQuery();
            int i = 0;
            while (rs.next()) {
                // Assemble the sender data.
                DataCustomer sender = new DataCustomer(
                        0, rs.getString("sender"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("sender_street_name"),
                                rs.getInt("sender_street_num"),
                                rs.getString("sender_city_code"),
                                rs.getString("sender_city_name"),
                                rs.getString("sender_country_code")
                        )
                );
                // Assemble the recipient data.
                DataCustomer recipient = new DataCustomer(
                        0, rs.getString("recipient"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("recipient_street_name"),
                                rs.getInt("recipient_street_num"),
                                rs.getString("recipient_city_code"),
                                rs.getString("recipient_city_name"),
                                rs.getString("recipient_country_code")
                        )
                );

                data.add(new DataParcel(
                        i,
                        rs.getString("parcel_id"),
                        rs.getString("parcel_status"),
                        sender,
                        recipient,
                        rs.getInt("weight"),
                        new Dimensions(
                                0,
                                rs.getInt("height"),
                                rs.getInt("width"),
                                rs.getInt("depth")
                        )
                ));
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the branch statistics, such as inbound, outbound parcels, number of jobs, number of drivers.
     * @param branchID String - The branch ID.
     * @return DataCount[] - The array of the statistics.
     */
    public DataCount[] getBranchStats(String branchID) {
        DataCount[] data = {null, null, null, null};
        try {
            this.callables[14].setString("branch_id", branchID);
            this.callables[14].registerOutParameter("inbound_parcels", Types.INTEGER);
            this.callables[14].registerOutParameter("outbound_parcels", Types.INTEGER);
            this.callables[14].registerOutParameter("all_jobs_drivers", Types.INTEGER);
            this.callables[14].registerOutParameter("no_of_drivers", Types.INTEGER);
            this.callables[14].executeQuery();
            data[0] = new DataCount(0, this.callables[14].getInt("inbound_parcels"));
            data[1] = new DataCount(1, this.callables[14].getInt("outbound_parcels"));
            data[2] = new DataCount(2, this.callables[14].getInt("all_jobs_drivers"));
            data[3] = new DataCount(3, this.callables[14].getInt("no_of_drivers"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Creates a new parcel.
     * @param parcel DataParcel - The parcel data.
     */
    public void createParcel(DataParcel parcel) {
        try {
            this.callables[15].setString("ID", parcel.parcelID);
            this.callables[15].setString("sender", parcel.sender.username);
            this.callables[15].setString("sender_st_name", parcel.sender.address.streetName);
            this.callables[15].setInt("sender_st_num", parcel.sender.address.streetNumber);
            this.callables[15].setString("sender_city_code", parcel.sender.address.postCode);
            this.callables[15].setString("sender_city_name", parcel.sender.address.cityName);
            this.callables[15].setString("sender_country_code", parcel.sender.address.countryISO);
            this.callables[15].setString("recipient", parcel.recipient.username);
            this.callables[15].setString("recipient_street_name", parcel.recipient.address.streetName);
            this.callables[15].setInt("recipient_street_num", parcel.recipient.address.streetNumber);
            this.callables[15].setString("recipient_city_code", parcel.recipient.address.postCode);
            this.callables[15].setString("recipient_city_name", parcel.recipient.address.cityName);
            this.callables[15].setString("recipient_country_code", parcel.recipient.address.countryISO);
            this.callables[15].setDouble("weight", parcel.weight);
            this.callables[15].setInt("height", parcel.dimensions.height);
            this.callables[15].setInt("width", parcel.dimensions.width);
            this.callables[15].setInt("depth", parcel.dimensions.depth);
            this.callables[15].executeQuery();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Creates a new customer if the customer does not exist.
     * @param customer DataCustomer - The customer.
     */
    public void createCustomer(DataCustomer customer) {
        try {
            this.callables[16].setString("username", customer.username);
            this.callables[16].setString("name", customer.name);
            this.callables[16].setString("surname", customer.surname);
            this.callables[16].setString("company_name", customer.companyName);
            this.callables[16].setString("tel_num", customer.phoneNumber);
            this.callables[16].setString("st_name", customer.address.streetName);
            this.callables[16].setInt("st_num", customer.address.streetNumber);
            this.callables[16].setString("city_code", customer.address.postCode);
            this.callables[16].setString("city_name", customer.address.cityName);
            this.callables[16].setString("country_code", customer.address.countryISO);
            this.callables[16].executeQuery();
        } catch(SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of the given job.
     * @param jobID int - The job ID.
     * @param newStatus int - The new status ID.
     */
    public void updateJobStatus(int jobID, int newStatus) {
        try {
            this.callables[17].setInt("job_ID", jobID);
            this.callables[17].setInt("new_status", newStatus);
            this.callables[17].executeQuery();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Gets all employees with given role at the given branch.
     * @param branchID int - The branch ID.
     * @param roleID int - The role ID.
     * @return ArrayList<DataStaff> - The list of employees.
     */
    public ArrayList<DataStaff> getAllEmployeesWithRoleAtBranch(int branchID, int roleID) {
        ArrayList<DataStaff> data = new ArrayList<>();
        try {
            this.callables[18].setInt("branch_id", branchID);
            this.callables[18].setInt("staff_role", roleID);
            ResultSet rs = this.callables[18].executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new DataStaff(i, rs.getString("username"), "", "", ""));
                i++;
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the branch address for given branch ID.
     * @param branchID int - The branch ID.
     * @return GeneralAddress - The branch address.
     */
    public GeneralAddress getBranchAddress(int branchID) {
        GeneralAddress data = null;
        try {
            this.callables[19].setInt("branch_id", branchID);
            ResultSet rs = this.callables[19].executeQuery();
            rs.next();
            data = new GeneralAddress(0,
                    rs.getString("city_code"),
                    rs.getString("city_name"),
                    rs.getString("country_code"));

        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the ID of the branch office for the given country.
     * @param countryISO String - The country ISO code.
     * @return DataCount - The branch ID.
     */
    public DataCount getBranchOffice(String countryISO) {
        DataCount data = null;
        try {
            this.callables[20].setString("country", countryISO);
            this.callables[20].registerOutParameter("branch_id", Types.INTEGER);
            this.callables[20].executeQuery();
            //rs.next();
            data = new DataCount(0, this.callables[20].getInt("branch_id"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the data for the given parcel id.
     * @param parcelID String - The parcel ID.
     * @return DataParcel - The parcel data.
     */
    public DataParcel getParcelDataFromID(String parcelID) {
        DataParcel data = null;
        try {
            this.callables[21].setString("ID", parcelID);
            ResultSet rs = this.callables[21].executeQuery();
            rs.next();
            // Assemble the sender data.
            DataCustomer sender = new DataCustomer(
                    0, rs.getString("sender"), "", "", "", "",
                    new SpecificAddress(
                            0,
                            rs.getString("sender_street_name"),
                            rs.getInt("sender_street_num"),
                            rs.getString("sender_city_code"),
                            rs.getString("sender_city_name"),
                            rs.getString("sender_country_code")
                    )
            );
            // Assemble the recipient data.
            DataCustomer recipient = new DataCustomer(
                    0, rs.getString("recipient"), "", "", "", "",
                    new SpecificAddress(
                            0,
                            rs.getString("recipient_street_name"),
                            rs.getInt("recipient_street_num"),
                            rs.getString("recipient_city_code"),
                            rs.getString("recipient_city_name"),
                            rs.getString("recipient_country_code")
                    )
            );

            data = new DataParcel(
                    0,
                    rs.getString("parcel_id"),
                    rs.getString("parcel_status"),
                    sender,
                    recipient,
                    rs.getInt("weight"),
                    new Dimensions(
                            0,
                            rs.getInt("height"),
                            rs.getInt("width"),
                            rs.getInt("depth")
                    )
            );

        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the branch ID of the given employee.
     * @param username String - The employee username.
     * @return DataCount - The branch ID.
     */
    public DataCount getBranchIDFromUsername(String username) {
        DataCount data = null;
        try {
            this.callables[22].setString("username", username);
            ResultSet rs = this.callables[22].executeQuery();
            rs.next();
            data = new DataCount(0, rs.getInt("branch_id"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Creates a new job and returns its ID.
     * @param jobTypeID int - The job type ID.
     * @param jobStatusID int - The job status ID.
     * @param username String - The employee username.
     * @return DataCount - The job ID.
     */
    public DataCount createJob(int jobTypeID, int jobStatusID, String username) {
        DataCount data = null;
        try {
            this.callables[23].setInt("job_type", jobTypeID);
            this.callables[23].setInt("job_status_id", jobStatusID);
            this.callables[23].setString("staff_username", username);
            this.callables[23].registerOutParameter("job_id", Types.INTEGER);
            this.callables[23].executeQuery();
            data = new DataCount(0, this.callables[23].getInt("job_id"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Links a parcel to a job.
     * @param parcelID String - The parcel ID.
     * @param jobID int - The job ID.
     */
    public void linkJobAndParcel(String parcelID, int jobID) {
        try {
            this.callables[24].setString("parcel_id", parcelID);
            this.callables[24].setInt("job_id", jobID);
            this.callables[24].executeQuery();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Gets the type of the given job.
     * @param jobID int - The job ID.
     * @return DataCount - The job type ID.
     */
    public DataCount getJobType(int jobID) {
        DataCount data = null;
        try {
            this.callables[25].setInt("jID", jobID);
            this.callables[25].registerOutParameter("type_id", Types.INTEGER);
            this.callables[25].executeQuery();
            data = new DataCount(0, this.callables[25].getInt("type_id"));
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the previous branch ID's of the given parcel.
     * @param parcelID String - The parcel ID.
     * @return ArrayList<DataCount> - The branch ID's.
     */
    public ArrayList<DataCount> getParcelLocations(String parcelID) {
        ArrayList<DataCount> data = new ArrayList<>();
        try {
            this.callables[26].setString("pID", parcelID);
            ResultSet rs = this.callables[26].executeQuery();
            while (rs.next()) {
                data.add(new DataCount(0, rs.getInt("branch_id")));
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the list of parcels sent by user.
     * @param username String - The username.
     * @return ArrayList<DataParcel> - The list of parcels.
     */
    public ArrayList<DataParcel> getSentParcels(String username) {
        ArrayList<DataParcel> data = new ArrayList<>();
        try {
            this.callables[27].setString("uname", username);
            ResultSet rs = this.callables[27].executeQuery();
            while (rs.next()) {
                DataCustomer sender = new DataCustomer(
                        0, rs.getString("sender"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("sender_street_name"),
                                rs.getInt("sender_street_num"),
                                rs.getString("sender_city_code"),
                                rs.getString("sender_city_name"),
                                rs.getString("sender_country_code")
                        )
                );
                // Assemble the recipient data.
                DataCustomer recipient = new DataCustomer(
                        0, rs.getString("recipient"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("recipient_street_name"),
                                rs.getInt("recipient_street_num"),
                                rs.getString("recipient_city_code"),
                                rs.getString("recipient_city_name"),
                                rs.getString("recipient_country_code")
                        )
                );

                data.add(new DataParcel(
                        0,
                        rs.getString("parcel_id"),
                        rs.getString("parcel_status"),
                        sender,
                        recipient,
                        rs.getInt("weight"),
                        new Dimensions(
                                0,
                                rs.getInt("height"),
                                rs.getInt("width"),
                                rs.getInt("depth")
                        )
                ));
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the list of parcels ordered by user.
     * @param username String - The username.
     * @return ArrayList<DataParcel> - The list of parcels.
     */
    public ArrayList<DataParcel> getOrderedParcels(String username) {
        ArrayList<DataParcel> data = new ArrayList<>();
        try {
            this.callables[28].setString("uname", username);
            ResultSet rs = this.callables[28].executeQuery();
            while (rs.next()) {
                DataCustomer sender = new DataCustomer(
                        0, rs.getString("sender"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("sender_street_name"),
                                rs.getInt("sender_street_num"),
                                rs.getString("sender_city_code"),
                                rs.getString("sender_city_name"),
                                rs.getString("sender_country_code")
                        )
                );
                // Assemble the recipient data.
                DataCustomer recipient = new DataCustomer(
                        0, rs.getString("recipient"), "", "", "", "",
                        new SpecificAddress(
                                0,
                                rs.getString("recipient_street_name"),
                                rs.getInt("recipient_street_num"),
                                rs.getString("recipient_city_code"),
                                rs.getString("recipient_city_name"),
                                rs.getString("recipient_country_code")
                        )
                );

                data.add(new DataParcel(
                        0,
                        rs.getString("parcel_id"),
                        rs.getString("parcel_status"),
                        sender,
                        recipient,
                        rs.getInt("weight"),
                        new Dimensions(
                                0,
                                rs.getInt("height"),
                                rs.getInt("width"),
                                rs.getInt("depth")
                        )
                ));
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the data of customer with given username.
     * @param username String - The username.
     * @return DataCustomer - The customer data.
     */
    public DataCustomer getCustomerData(String username) {
        DataCustomer data = null;
        try {
            this.callables[29].setString("uname", username);
            ResultSet rs = this.callables[29].executeQuery();
            rs.next();
            data = new DataCustomer(
                    0,
                    username,
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("company_name"),
                    rs.getString("tel_num"),
                    new SpecificAddress(
                            0,
                            rs.getString("street_name"),
                            rs.getInt("street_number"),
                            rs.getString("city_code"),
                            rs.getString("city_name"),
                            rs.getString("country_code")
                    )
            );
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Gets the parcel history.
     * @param parcelID String - The parcel ID.
     * @return ArrayList<DataParcelHistory> - The parcel history.
     */
    public ArrayList<DataParcelHistory> getParcelHistory(String parcelID) {
        ArrayList<DataParcelHistory> data = new ArrayList<>();
        try {
            this.callables[30].setString("parcel_id", parcelID);
            ResultSet rs = this.callables[30].executeQuery();
            while (rs.next()) {
                String status = switch (rs.getString("type")) {
                    case "Order processing" -> "Order confirmed";
                    case "Handover" -> "Parcel handed over to the courier";
                    case "Check in" -> "Parcel arrived at the parcel center";
                    case "Cargo departing confirmation" -> "Parcel departed from the parcel center";
                    case "Cargo arrival confirmation" -> "Parcel arrived at the destination";
                    case "Delivery cargo confirmation" -> "Parcel in delivery";
                    case "Parcel handover" -> "Parcel handed over to the recipient";
                    default -> rs.getString("type");
                };

                data.add(new DataParcelHistory(
                        0,
                        status,
                        rs.getDate("date").toLocalDate(),
                        rs.getString("post"),
                        rs.getString("city"),
                        rs.getString("country")
                ));
            }
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

}
