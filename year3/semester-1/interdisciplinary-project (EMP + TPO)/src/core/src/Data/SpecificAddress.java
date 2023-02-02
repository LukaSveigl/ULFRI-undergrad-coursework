package Data;

/**
 * The specific address, includes all info.
 */
public class SpecificAddress extends GeneralAddress {
    /** The address street name. */
    public final String streetName;
    /** The address street number. */
    public final int streetNumber;

    /**
     * Constructs a new SpecificAddress object.
     * @param index int - The sequential record number.
     * @param streetName String - The address street name.
     * @param streetNumber int - The address street number.
     * @param postCode String - The address post code.
     * @param cityName String - The address city name.
     * @param countryISO String - The country ISO3 code.
     */
    public SpecificAddress(int index, String streetName, int streetNumber, String postCode, String cityName, String countryISO) {
        super(index, postCode, cityName, countryISO);
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "SpecificAddress{" +
                "recordNumber=" + recordNumber +
                ", postCode='" + postCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", streetName='" + streetName + '\'' +
                ", streetNumber=" + streetNumber +
                '}';
    }
}
