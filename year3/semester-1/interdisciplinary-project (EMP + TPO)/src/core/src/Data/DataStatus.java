package Data;

/**
 * Data representation of status or type.
 */
public class DataStatus extends Data {
    /** The status or type id. */
    public final int id;
    /** The status or type name. */
    public final String name;

    /**
     * Constructs a new DataStatus object.
     * @param index int - The sequential record number.
     * @param id int - The status or type id.
     * @param name String - The status or type name.
     */
    public DataStatus(int index, int id, String name) {
        super(index);
        this.id = id;
        this.name = name;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataStatus{" +
                "recordNumber=" + recordNumber +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
