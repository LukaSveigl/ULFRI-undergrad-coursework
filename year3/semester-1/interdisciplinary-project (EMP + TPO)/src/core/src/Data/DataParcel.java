package Data;

/**
 * Data representation of parcel info.
 */
public class DataParcel extends Data {
    /** The parcel ID. */
    public final String parcelID;
    /** The status ID. */
    public final String statusID;
    /** The sender data. */
    public final DataCustomer sender;
    /** The recipient data. */
    public final DataCustomer recipient;
    /** The parcel weight. */
    public final double weight;
    /** The parcel dimensions. */
    public final Dimensions dimensions;

    /**
     * Constructs a new DataParcel object.
     * @param index int - The sequential record number.
     * @param parcelID String - The parcel ID.
     * @param statusID int - The status ID.
     * @param sender DataCustomer - The sender data.
     * @param recipient DataCustomer - The recipient data.
     * @param weight double - The parcel weight.
     * @param dimensions Dimensions - The parcel dimensions (height, width, depth).
     */
    public DataParcel(int index, String parcelID, String statusID, DataCustomer sender, DataCustomer recipient, double weight, Dimensions dimensions ) {
        super(index);
        this.parcelID = parcelID;
        this.statusID = statusID;
        this.sender = sender;
        this.recipient = recipient;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataParcel{" +
                "recordNumber=" + recordNumber +
                ", parcelID=" + parcelID +
                ", statusID=" + statusID +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", weight=" + weight +
                ", dimensions=" + dimensions +
                '}';
    }
}
