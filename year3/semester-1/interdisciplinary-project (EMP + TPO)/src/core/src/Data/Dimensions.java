package Data;

/**
 * The parcel dimensions.
 */
public class Dimensions extends Data {
    /** The parcel height. */
    public final int height;
    /** The parcel width. */
    public final int width;
    /** The parcel depth. */
    public final int depth;

    /**
     * Constructs a new Dimensions object.
     * @param index int - The sequential record number.
     * @param height int - The parcel height.
     * @param width int - The parcel width.
     * @param depth int - The parcel depth.
     */
    public Dimensions(int index, int height, int width, int depth) {
        super(index);
        this.height = height;
        this.width = width;
        this.depth = depth;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "Dimensions{" +
                "recordNumber=" + recordNumber +
                ", height=" + height +
                ", width=" + width +
                ", depth=" + depth +
                '}';
    }
}
