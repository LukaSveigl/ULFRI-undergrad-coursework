package DBCore;

import ConfigLoader.ConfigLoader;
import ConfigLoader.DBData;
import Utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The internal database connection implementation.
 */
class DBCore {
    /** The database config loader. */
    private final ConfigLoader cfgLoader;
    /** The logger. */
    private final Logger logger;
    /** The database connection. */
    private Connection dbConnection;
    /** The MySQL connection string. */
    private String url;
    /** The username to connect with. */
    private String username;
    /** The password to connect with. */
    private String password;
    /** The flag that signifies if the connection is currently established. */
    private boolean connectionEstablished;

    /**
     * Constructs a new database core instance.
     */
    protected DBCore(boolean log) {
        this.cfgLoader = new ConfigLoader(log);
        this.logger = new Logger(log);

        this.url = "jdbc:mysql://<ip>:<port>/<dbname>";
        this.username = "";
        this.password = "";
        this.connectionEstablished = false;
    }

    /**
     * Sets the database url parameters.
     * @throws NullPointerException If database info could not be retrieved.
     */
    private void setUrlParameters() throws NullPointerException {
        cfgLoader.load();
        DBData data = cfgLoader.fetchData();

        if (data == null) {
            throw new NullPointerException("Database data is null.");
        }

        this.url = url.replace("<ip>", data.getIP());
        this.url = url.replace("<port>", data.getPort());
        this.url = url.replace("<dbname>", data.getName());
    }

    /**
     * Sets the database login parameters.
     * @param username String - The username.
     * @param password String - The password.
     */
    private void setLoginParameters(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Initializes the database connection.
     */
    private void init() {
        try {
            this.dbConnection = DriverManager.getConnection(this.url, this.username, this.password);
            this.connectionEstablished = true;
        } catch (SQLException e) {
            this.connectionEstablished = false;
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Logs into the database with the login parameters and url. This method encapsulates setting of parameters
     * and initialization.
     */
    public void login(String username, String password) {
        boolean successful = true;
        try {
            this.setUrlParameters();
            this.setLoginParameters(username, password);
        } catch (NullPointerException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
            successful = false;
        }

        if (successful) {
            this.init();
            if (this.isConnectionEstablished()) {
                this.logger.log("User " + username + " successfully logged into the database at url " +
                        this.url + ".", Logger.MessageType.LOG);
            }
        }
    }

    /**
     * Closes the current database connection.
     */
    public void logout() {
        String tmpUsername = this.username;
        this.url = "jdbc:mysql://<ip>:<port>/<dbname>";
        this.username = "";
        this.password = "";

        // If the connection is not established, skip the logout process.
        if (!this.isConnectionEstablished()) {
            return;
        }

        try {
            this.connectionEstablished = false;
            this.dbConnection.close();
        } catch (SQLException e) {
            this.logger.log(e.getMessage(), Logger.MessageType.ERROR);
            e.printStackTrace();
        }

        if (!this.isConnectionEstablished()) {
            this.logger.log("User " + tmpUsername + " successfully logged out.",
                    Logger.MessageType.LOG);
        }
    }

    /**
     * Database connection getter.
     * @return Connection - The database connection.
     */
    public Connection getDbConnection() {
        return this.dbConnection;
    }

    /**
     * Getter for the flag that signifies if the connection is established.
     * @return Boolean - True if connection is established, false otherwise.
     */
    public boolean isConnectionEstablished() {
        return this.connectionEstablished;
    }
}
