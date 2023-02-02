package Data;

/**
 * Data representation of the result given by COUNT statement.
 */
public class DataCount extends Data {
    /** The COUNT value. */
    public final int value;

    /**
     * Constructs a new DataCount object.
     * @param index int - The sequential record number.
     * @param value int - The COUNT value.
     */
    public DataCount(int index, int value) {
        super(index);
        this.value = value;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataCount{" +
                "recordNumber=" + recordNumber +
                ", value=" + value +
                '}';
    }
}
