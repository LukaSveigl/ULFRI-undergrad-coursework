package Utils;

import DBCore.DBAPI;

import Data.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Data.DataParcelCenter;
import Data.GeneralAddress;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.*;

/**
 * The core utilities.
 */
public class Utils {
    /** The maximum allowed distance between two parcel centers. */
    private static final double maxDistanceBetweenCenters = 200;

    /**
     * Generates a random alphanumeric string of given length.
     * @param targetLength int - The target length of string.
     * @return String - The random alphanumeric string.
     */
    private static String generateRandomAlphanumericString(int targetLength) {
        final int lowerLimit = 48; // Character '0'.
        final int upperLimit = 122; // Character 'z'.

        return new Random()
                .ints(lowerLimit, upperLimit)
                // Leave out non-alphanumeric characters.
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Calculates the distance in km between two geographical locations using the Haversine formula.
     * Link to explanation: https://en.wikipedia.org/wiki/Haversine_formula
     * @param source Coordinates - The source coordinates.
     * @param destination Coordinates - The destination coordinates.
     * @return double - The distance in km.
     */
    private static double calculateDistanceFromCoordinates(Coordinates source, Coordinates destination) {
        final double earthRadiusKm = 6371;
        final double latitudeDifference = Math.toRadians(destination.latitude) - Math.toRadians(source.latitude);
        final double longitudeDifference = Math.toRadians(destination.longitude) - Math.toRadians(source.longitude);
        final double a = Math.pow(Math.sin(latitudeDifference / 2), 2)
                       + Math.cos(Math.toRadians(source.latitude)) * Math.cos(Math.toRadians(destination.latitude))
                       * Math.pow(Math.sin(longitudeDifference / 2), 2);
        final double c = 2 * Math.asin(Math.sqrt(a));
        return c * earthRadiusKm;
    }

    /**
     * Generates an 8 character parcel ID in format: <ISOCODE>xxxxx where x means random alphanumeric character.
     * @param dbapi DBAPI - The database api.
     * @param country String - The country ISO code.
     * @return String - The 8 character parcel ID or null if timed out.
     * @throws Exception If country ISO code is in wrong format.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateParcelID(DBAPI dbapi, String country) throws Exception {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
        }
        if (country.length() != 3) {
            throw new Exception("Country ISO code length is " + country.length() + " instead of 3 characters long.");
        }

        StringBuilder result;
        int identicalIDs;
        int attemptCount = 0;

        do {
            result = new StringBuilder(country);
            result.append(Utils.generateRandomAlphanumericString(5));

            identicalIDs = dbapi.getCountOfIdenticalParcelIDs(result.toString()).value;

            attemptCount++;
            if (attemptCount > 20) {
                return null;
            }
        } while (identicalIDs != 0);

        return result.toString();
    }

    /**
     * Generates a 10 character username based on name and surname. If name and surname are blank,
     * generate only reference number.
     * @param dbapi DBAPI - The database api.
     * @param name String - The user's name.
     * @param surname String - The user's surname.
     * @return String - The 10 character username or null if timed out.
     * @throws NullPointerException If database API pointer is null.
     */
    public static String generateUsername(DBAPI dbapi, String name, String surname) throws NullPointerException {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
        }

        StringBuilder result;
        int identicalIDs;
        int attemptCount = 0;

        do {
            if (name.equals("") || surname.equals("")) {
                result = new StringBuilder();
            }
            else {
                if (name.length() < 2 || surname.length() < 2) {
                    result = new StringBuilder();
                }
                else {
                    result = new StringBuilder(name.substring(0, 2));
                    result.append(surname, 0, 2);
                }
            }

            result.append(Utils.generateRandomAlphanumericString(10 - result.length()));

            identicalIDs = dbapi.getCountOfIdenticalUsernames(result.toString()).value;

            attemptCount++;
            if (attemptCount > 20) {
                return null;
            }
        } while (identicalIDs != 0);

        return result.toString();
    }

    /**
     * Checks if the given post code exists in the database.
     * @param dbapi DBAPI - The database api.
     * @param postCode String - The post code.
     * @return boolean - True if post code exists in database, false otherwise.
     * @throws NullPointerException If database API pointer is null.
     */
    public static boolean validatePostCode(DBAPI dbapi, String postCode) throws NullPointerException {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
        }
        int samePostCodes = dbapi.getCountOfIdenticalPostCodes(postCode).value;
        return samePostCodes > 0;
    }

    /**
     * Finds the shortest path (via parcel centers) between the sender and recipient address.
     * @param dbapi DBAPI - The database api.
     * @param senderAddress GeneralAddress - The sender address.
     * @param recipientAddress GeneralAddress - The recipient address.
     * @return ArrayList<DataParcelCenter> - The list of parcel centers on path.
     * @throws NullPointerException If database API pointer is null.
     */
    public static ArrayList<DataParcelCenter> shortestPath(DBAPI dbapi,
                                                  GeneralAddress senderAddress,
                                                  GeneralAddress recipientAddress) throws NullPointerException {
        if (dbapi == null) {
            throw new NullPointerException("Database API is null.");
        }

        ArrayList<DataParcelCenter> data = dbapi.getAllParcelCenterData();
        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  graph =
                new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Populate graph with parcel center data as vertices
        for (DataParcelCenter parcelCenter : data) {
            graph.addVertex(parcelCenter.id);
        }

        // Create both-ways connections between all parcel centers. This is needed due to the limitations of the used
        // version of JGraphT library, as it does not include a functional undirected graph variant.
        double distance;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if (i != j) {
                    distance = calculateDistanceFromCoordinates(
                            data.get(i).coordinates,
                            data.get(j).coordinates
                    );
                    if (distance < Utils.maxDistanceBetweenCenters) {
                        DefaultWeightedEdge edge = graph.addEdge(data.get(i).id, data.get(j).id);
                        graph.setEdgeWeight(edge, distance);
                    }
                }
            }
        }


        // Find the closest parcel center to sender in same country. This can be guaranteed as each country has at least
        // one parcel center.
        DataParcelCenter closestCenterToSender = null;
        Coordinates senderCoordinates;
        double minDistance = Double.MAX_VALUE;
        double curDistance;
        for (DataParcelCenter parcelCenter : data) {
            if (parcelCenter.countryISO.equals(senderAddress.countryISO)) {
                senderCoordinates = dbapi.getCoordinatesFromAddress(senderAddress);
                curDistance = calculateDistanceFromCoordinates(senderCoordinates, parcelCenter.coordinates);
                if (curDistance < minDistance) {
                    minDistance = curDistance;
                    closestCenterToSender = parcelCenter;
                }
            }
        }

        // Find the closest parcel center to recipient in same country. This can be guaranteed as each country has
        // at least one parcel center.
        DataParcelCenter closestCenterToRecipient = null;
        Coordinates recipientCoordinates;
        minDistance = Double.MAX_VALUE;
        for (DataParcelCenter parcelCenter : data) {
            if (parcelCenter.countryISO.equals(recipientAddress.countryISO)) {
                recipientCoordinates = dbapi.getCoordinatesFromAddress(recipientAddress);
                curDistance = calculateDistanceFromCoordinates(recipientCoordinates, parcelCenter.coordinates);
                if (curDistance < minDistance) {
                    minDistance = curDistance;
                    closestCenterToRecipient = parcelCenter;
                }
            }
        }

        assert closestCenterToSender != null;
        assert closestCenterToRecipient != null;
        List<DefaultWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(
                graph,
                closestCenterToSender.id,
                closestCenterToRecipient.id
        );

        // Assemble result list.
        ArrayList<DataParcelCenter> result = new ArrayList<>();
        int count = 0;
        for (DefaultWeightedEdge edge : shortestPath) {
            if (count == 0) {
                String edgeString = edge.toString();
                String firstID = edgeString.substring(1, edgeString.indexOf(' '));
                String secondID = edgeString.substring(edgeString.lastIndexOf(' ') + 1, edgeString.length() - 1);
                System.out.println(secondID);
                for (DataParcelCenter parcelCenter : data) {
                    if (parcelCenter.id.equals(firstID)) {
                        result.add(parcelCenter);
                    }
                    if (parcelCenter.id.equals(secondID)) {
                        result.add(parcelCenter);
                    }
                }
            }
            else {
                String edgeString = edge.toString();
                String secondID = edgeString.substring(edgeString.lastIndexOf(' ') + 1, edgeString.length() - 1);
                System.out.println(secondID);
                for (DataParcelCenter parcelCenter : data) {
                    if (parcelCenter.id.equals(secondID)) {
                        result.add(parcelCenter);
                    }
                }
            }
            count++;
        }

        return result;
    }
}


