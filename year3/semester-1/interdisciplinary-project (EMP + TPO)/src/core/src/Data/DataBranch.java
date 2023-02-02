package Data;

/**
 * Data representation of branch info.
 */
public class DataBranch extends Data {
    /** The branch ID. */
    public final int branchID;
    /** The branch name. */
    public final String name;
    /** The branch type. */
    public final String branchType;

    /**
     * Constructs a new DataBranch object.
     * @param index int - The sequential record number.
     * @param branchID int - The branch ID.
     * @param name String - The branch name.
     * @param branchType String - The branch type.
     */
    public DataBranch(int index, int branchID, String name, String branchType) {
        super(index);
        this.branchID = branchID;
        this.name = name;
        this.branchType = branchType;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataBranch{" +
                "recordNumber=" + recordNumber +
                ", branchID=" + branchID +
                ", name='" + name + '\'' +
                ", branchType='" + branchType + '\'' +
                '}';
    }
}
