package Data;

/**
 * The general address, with no house or street number.
 */
public class GeneralAddress extends Data {
    /** The address post code. */
    public final String postCode;
    /** The city of address. */
    public final String cityName;
    /** The country ISO3 code. */
    public final String countryISO;

    /**
     * Constructs a new GeneralAddress object.
     * @param index int - The sequential record number.
     * @param postCode String - The address post code.
     * @param cityName String - The address city name.
     * @param countryISO String - The country ISO3 code.
     */
    public GeneralAddress(int index, String postCode, String cityName, String countryISO) {
        super(index);
        this.postCode = postCode;
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "Address{" +
                "postCode='" + postCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", recordNumber=" + recordNumber +
                '}';
    }
}
