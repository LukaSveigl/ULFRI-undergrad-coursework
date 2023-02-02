import java.util.Scanner;
import java.util.Random;
import java.io.File;

public class DN03 {
    public static void main(String [] args) throws Exception {
        if (args.length == 3) {
            String[] codeArray = generateArray(args[0]);

            String password = generatePassword(Integer.parseInt(args[1]), Integer.parseInt(args[2]), codeArray);

            System.out.println(password);

            x++;
        }
    }

    static String[] generateArray(String path) throws Exception {
        // Method that generates an array from a file: each line in file is
        // an array entry

        // Scanner used to read file
        Scanner sc = new Scanner(new File(path));

        // First line in file has size of array (count of other lines)
        int size = Integer.parseInt(sc.nextLine());

        String[] arr = new String[size];

        int counter = 0;

        while(sc.hasNextLine()) {
            arr[counter] = sc.nextLine();
            counter++;
        }
        sc.close();

        return arr;
    }

    static String generatePassword(int length, int seed, String [] codeArray) {
        Random rnd = new Random(seed);

        String password = "";

        for (int count = 0; count < length; count++) {
            // Get random line of array
            int yIndex = rnd.nextInt(codeArray.length);

            // Get random character in string in certain line
            int xIndex = rnd.nextInt(codeArray[yIndex].length());

            // Add new character to password
            password += codeArray[yIndex].charAt(xIndex);
        }

        return password;
    }
}
