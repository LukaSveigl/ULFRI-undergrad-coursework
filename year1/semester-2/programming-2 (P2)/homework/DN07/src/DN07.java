import java.util.Scanner;
import java.io.File;

public class DN07 {
    public static void main(String[] args) {
        if (args.length == 2) {
            int[] numbers = generateArray(args[0]);

            for (int i = 0; i < Integer.parseInt(args[1]); i++) {

                int index = findMaxIndex(numbers);

                System.out.println(numbers[index]);
                numbers[index] = -1;
            }
        }
        else {
            System.out.println("Napačno število argumentov!");
        }
    }

    static int[] generateArray(String path) {
        int[] numbers = new int[100];

        int counter = 0;

        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);

            while(sc.hasNextInt()) {
                numbers[counter] = sc.nextInt();
                counter++;
            }

            sc.close();
        } catch (Exception e) {
            System.out.println("Napaka pri odprianju datoteke!");
        }

        for (int i = counter; i < 100; i++) {
            numbers[i] = -1;
        }

        return numbers;
    }

    static int findMaxIndex(int[] numbers) {
        int max = 0;
        int maxIndex = 0;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
