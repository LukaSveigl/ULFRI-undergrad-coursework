package application;

import DBCore.DBAPI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Common items between all views.
 */
public class Common {
    /** The database API. */
    public static DBAPI dbapi = new DBAPI(true);
    /** The current staff username. */
    public static String username = "";
    /** Maps parcels to their jobs. */
    public static final HashMap<String, ArrayList<String>> parcelToJob = new HashMap<>();
}
