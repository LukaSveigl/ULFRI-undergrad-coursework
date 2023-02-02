package Data;

/**
 * Data representation of the staff information.
 */
public class DataStaff extends Data {
    /** The staff username. */
    public final String username;
    /** The staff name. */
    public final String name;
    /** The staff surname. */
    public final String surname;
    /** The staff role name.*/
    public final String role;

    /**
     * Constructs a new DataStaff object.
     * @param index int - The sequential record number.
     * @param username String - The staff username.
     * @param name String - The staff name.
     * @param surname String - The staff surname.
     * @param role String - The staff role name.
     */
    public DataStaff(int index, String username, String name, String surname, String role) {
        super(index);
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    /**
     * Converts object to String.
     * @return String - The String representation of the object.
     */
    @Override
    public String toString() {
        return "DataStaff{" +
                "recordNumber=" + recordNumber +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    /**
     * Gets the full name of staff.
     * @return String - The full name.
     */
    public String getFullName() {
        return this.name.strip() + " " + this.surname.strip();
    }

    /**
     * Gets the staff first name and initial.
     * @return String - The staff first name and initial.
     */
    public String getFirstNameAndInitial() {
        return this.name.strip() + " " + this.surname.strip().charAt(0) + ".";
    }

}
