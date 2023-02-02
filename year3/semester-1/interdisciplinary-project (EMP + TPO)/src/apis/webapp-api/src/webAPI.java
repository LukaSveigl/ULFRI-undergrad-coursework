
import java.util.ArrayList;
import DBCore.DBAPI;
import Data.DataCustomer;
import Data.DataParcel;
import Data.DataParcelHistory;
import Data.Dimensions;
import Data.SpecificAddress;

public class webAPI {

    public static String getSentParcelsJSON (String username, String password) {
        DBAPI api = new DBAPI(false);
        api.login(username, password);
        if(api.isConnectionEstablished()){
            System.out.println("Connection established");
            System.out.println(username + " " + password);
        } else {
            System.out.println("Connection not established");
            System.exit(1);
        }
        ArrayList<DataParcel> parcels = api.getSentParcels(username);
        String json = "[";
        for (DataParcel parcel : parcels) {

            String parcelJSON = String.format("{\n" +
                "\"parcelID\": \"%s\",\n" +
                "\"weight\": \"%s\",\n" +
                "\"width\": \"%s\",\n" +
                "\"height\": \"%s\",\n" +
                "\"depth\": \"%s\",\n" +
                "\"statusID\": \"%s\"\n" +
            "}" 
            , parcel.parcelID, parcel.weight, parcel.dimensions.width, parcel.dimensions.height, parcel.dimensions.depth, parcel.statusID);
            json += parcelJSON;
            json += ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    public static String getOrderedParcelsJSON (String username, String password) {
        DBAPI api = new DBAPI(false);
        api.login(username, password);
        if(api.isConnectionEstablished()){
            System.out.println("Connection established");
        } else {
            System.out.println("Connection not established");
            System.exit(1);
        }
        ArrayList<DataParcel> parcels = api.getOrderedParcels(username);
        String json = "[";
        for (DataParcel parcel : parcels) {

            String parcelJSON = String.format("{\n" +
                "\"parcelID\": \"%s\",\n" +
                "\"weight\": \"%s\",\n" +
                "\"width\": \"%s\",\n" +
                "\"height\": \"%s\",\n" +
                "\"depth\": \"%s\",\n" +
                "\"statusID\": \"%s\"\n" +
            "}" 
            , parcel.parcelID, parcel.weight, parcel.dimensions.width, parcel.dimensions.height, parcel.dimensions.depth, parcel.statusID);
            json += parcelJSON;
            json += ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    public static String VerifyLoginJSON (String username, String password){
        DBAPI api = new DBAPI(false);
        api.login(username, password);

        String json = "{\n";
        if (api.isConnectionEstablished()) {
            json += "\"login\": \"true\",";
        } else {
            json += "\"login\": \"false\",";
        }
        json = json.substring(0, json.length() - 1);
        json += "\n}";
        return json;
    }

    public static String getUserFullNameJSON (String username, String password){
        DBAPI api = new DBAPI(false);
        api.login(username, password);
        String json = "{\n";
        DataCustomer customer = api.getCustomerData(username);
        json += String.format("\"name\": \"%s\",\n", customer.name);
        json += String.format("\"surname\": \"%s\"\n", customer.surname);
        json += "}";
        return json;
    }

    public static String getParcelHistoryJSON (String parcelID){
        DBAPI api = new DBAPI(true);
        api.login("tracker", "password");
        ArrayList<DataParcelHistory> history = api.getParcelHistory(parcelID);
        String json = "[";
        for (DataParcelHistory event : history) {
            String eventJSON = String.format(" {\n" +
                "\"status\": \"%s\",\n" +
                "\"dateCompleted\": \"%s\"\n" +
            "}\n" 
            , event.status, event.dateCompleted);
            json += eventJSON;
            json += ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }

    static void createNewParcel (String username, String password, String index, String parcelID, String statusID, String sender, String recipient, String weight, String length, String width, String height) throws Exception{
        DBAPI api = new DBAPI(true);
        api.login(username, password);
        int _index = Integer.parseInt(index);
        double _weight = Double.parseDouble(weight);
        int _length = Integer.parseInt(length);
        int _width = Integer.parseInt(width);
        int _height = Integer.parseInt(height);
        DataCustomer _sender = api.getCustomerData(sender);
        DataCustomer _recipient = api.getCustomerData(recipient);
        Dimensions dimensions = new Dimensions(0, _length, _width, _height);

        DataParcel parcel = new DataParcel(
                _index, //index
                Utils.Utils.generateParcelID(api, parcelID), // parcelID
                statusID, // status id
                _sender, // sender
                _recipient, // recipient
                _weight, // weight
                dimensions); // dimensions
        api.createParcel(parcel);
    }

    static void createNewUser (String username, String name, String surname, String companyName, String phoneNumber, String streetAddress, String streetNumber, String postCode, String cityName, String countryISO) throws Exception{
        DBAPI api = new DBAPI(false);
        api.login("doadmin", "AVNS_VpALW-F9nAfppGdvLT3");

        if (api.isConnectionEstablished()) {
            System.out.println("Connection established");
        } else {
            System.out.println("Connection not established");
            System.exit(1);
        }

        api.createCustomerInDatabase(username);

        SpecificAddress specificAddress = new SpecificAddress(0, streetAddress, Integer.parseInt(streetNumber), postCode, cityName, countryISO);
        // SpecificAddress specificAddress = new SpecificAddress(0, streetAddress, Integer.parseInt(streetNumber), postCode, cityName, countryISO);

        DataCustomer customer = new DataCustomer(
            0,
            username, 
            name, 
            surname,
            companyName,
            phoneNumber,
            specificAddress 
        );

        api.createCustomer(customer);

    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(getUserFullNameJSON("bla", "password"));
        
    }

            // USER NAME AND SURNAME
            // String customerFullNameJSON = getUserFullNameJSON(db, "aarmatt59");
            // System.out.println(customerFullNameJSON);


            // PARCELS SENT & ORDERED BY THE USER - FETCH
            // ArrayList<DataParcel> sentParcels = db.getSentParcels("aarmatt59");
            // ArrayList<DataParcel> orderedParcels = db.getOrderedParcels("aarmatt59");
            // String parcelsSentJSON = getParcelsJSON(sentParcels);
            // String parcelsOrderedJSON = getParcelsJSON(orderedParcels);

            


            // GET PARCEL HISTORY - FETCH
            // ArrayList<DataParcelHistory> parcelHistory = db.getParcelHistory("SVNXcZjO");
            // String parcelHistoryJSON = getParcelHistoryJSON(parcelHistory);

            // CREATE A NEW PARCEL - CREATE
            // DataCustomer customer = db.getCustomerData("aarmatt59");
            // createNewParcel(db, "aarmatt59", 0, "SVN", "1", customer, customer, 99.0, new Dimensions(0, 1, 1, 2));

        // } else{
        //     System.out.println("Connection failed");
        // }

        // db.logout();


        

}