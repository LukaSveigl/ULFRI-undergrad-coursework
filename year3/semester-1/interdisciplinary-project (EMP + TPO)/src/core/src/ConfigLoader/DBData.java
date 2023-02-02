package ConfigLoader;

/**
 * A database configuration data object.
 */
public class DBData {
    /** The database name. */
    private final String name;
    /** The database location. */
    private final String ip;
    /** The database port. */
    private final String port;

    /**
     * Constructs a new DBData instance with given values.
     * @param name String - The database name.
     * @param ip String - The database ip.
     * @param port String - The database port.
     */
    protected DBData(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "[Name = " + this.name + ", ip = " + this.ip + ", port = " + this.port + "]";
    }

    /**
     * The database name getter.
     * @return String - The database name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * The database ip getter.
     * @return String - The database ip.
     */
    public String getIP() {
        return this.ip;
    }

    /**
     * The database port getter.
     * @return String - The database port.
     */
    public String getPort() {
        return this.port;
    }
}
