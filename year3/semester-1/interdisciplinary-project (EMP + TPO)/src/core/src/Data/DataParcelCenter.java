package Data;

/**
 * Data representation of the parcel center location.
 */
public class DataParcelCenter extends Data {
    /** The parcel center id. */
    public final String id;
    /** The parcel center coordinates. */
    public final Coordinates coordinates;
    /** The country ISO3 code. */
    public final String countryISO;

    /**
     * Constructs a new DataParcelCenter object.
     * @param index int - The sequential record number.
     * @param id String - The parcel center id.
     * @param coordinates Coordinates - The parcel center latitude.
     * @param countryISO String - The country ISO3 code.
     */
    public DataParcelCenter(int index, String id, Coordinates coordinates, String countryISO) {
        super(index);
        this.id = id;
        this.coordinates = coordinates;
        this.countryISO = countryISO;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataParcelCenter{" +
                "recordNumber=" + recordNumber +
                ", id='" + id + '\'' +
                ", coordinates=" + coordinates +
                ", countryISO='" + countryISO + '\'' +
                '}';
    }
}
