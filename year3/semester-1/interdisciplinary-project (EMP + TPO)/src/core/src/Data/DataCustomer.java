package Data;

/**
 * Data representation of customer info.
 */
public class DataCustomer extends Data {
    /** The customer username. */
    public final String username;
    /** The customer name. */
    public final String name;
    /** The customer surname. */
    public final String surname;
    /** The customer company name. */
    public final String companyName;
    /** The customer phone number. */
    public final String phoneNumber;
    /** The customer address. */
    public final SpecificAddress address;

    /**
     * Constructs a new DataCustomer object.
     * @param index int - The sequential record number.
     * @param username String - The customer username.
     * @param name String - The customer name.
     * @param surname String - The customer surname.
     * @param companyName String - The company name. Can be null.
     * @param phoneNumber String - The customer phone number.
     * @param address SpecificAddress - The customer address.
     */
    public DataCustomer(int index, String username, String name, String surname, String companyName, String phoneNumber, SpecificAddress address) {
        super(index);
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataCustomer{" +
                "recordNumber=" + recordNumber +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", companyName='" + companyName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                '}';
    }
}
