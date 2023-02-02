package Data;

/**
 * Representation of data returned by database queries.
 */
public abstract class Data {
    /** The sequential number of the record returned by the query. */
    protected final int recordNumber;

    /**
     * Constructs a new data representation object.
     * @param index int - The sequential record number.
     */
    public Data(int index) {
        this.recordNumber = index;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "Data{" +
                "recordNumber=" + recordNumber +
                '}';
    }
}

