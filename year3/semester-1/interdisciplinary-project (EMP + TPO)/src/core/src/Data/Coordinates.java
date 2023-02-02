package Data;

/**
 * The geographical coordinates.
 */
public class Coordinates extends Data {
    /** The latitude. */
    public final double latitude;
    /** The longitude. */
    public final double longitude;

    /**
     * Constructs a new Coordinates object.
     * @param index int - The sequential record number.
     * @param latitude double - The latitude.
     * @param longitude dobule - The longitude.
     */
    public Coordinates(int index, double latitude, double longitude) {
        super(index);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", recordNumber=" + recordNumber +
                '}';
    }
}
