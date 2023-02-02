package Data;

import java.time.LocalDate;

/**
 * Data representation of a snapshot of parcel history.
 */
public class DataParcelHistory extends Data {
    /** The parcel status. */
    public final String status;
    /** The date when the status was changed. */
    public final LocalDate dateCompleted;
    /** The post code where the status was changed. */
    public final String postCode;
    /** The city code where the status was changed. */
    public final String cityName;
    /** The country ISO3 code where the status was changed. */
    public final String countryISO;

    public DataParcelHistory(int index, String status, LocalDate dateCompleted, String postCode, String cityName, String countryISO) {
        super(index);
        this.status = status;
        this.dateCompleted = dateCompleted;
        this.postCode = postCode;
        this.cityName = cityName;
        this.countryISO = countryISO;
    }

    @Override
    public String toString() {
        return "DataParcelHistory{" +
                "status='" + status + '\'' +
                ", dateCompleted=" + dateCompleted +
                ", cityCode='" + postCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", recordNumber=" + recordNumber +
                '}';
    }

}
