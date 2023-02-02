public class Izziv2 {
    public static void main(String[] args) {
        Timer t = new Timer();

        System.out.println(t.timeLinear(20000));
        System.out.println(t.timeBinary(20000));

        System.out.println("");

        t.experimentalAlgorithmEvaluation();
    }
}

class Timer {
    int [] generateTable(int n) {
        int [] arr = new int[n];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        return arr;
    }

    int findLinear(int [] a, int v) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == v) {
                return i;
            }
        }

        return -1;
    }

    int findBinary(int [] a, int l, int r, int v) {

        // If end index smaller than start index - should never happen
        if (r < l) {
            return -1;
        }

        // Calculate middle index in array
        int mid = (l + r) / 2;

        // If element is equal to element on middle index return that element
        if (v == a[mid]) {
            return mid;
        }
        // If element is lower than element on middle index
        else if (v < a[mid]) {
            // Call method again, change upper limit to one less than middle (we know middle isn't the correct element)
            return findBinary(a, l, mid - 1, v);
        }
        // If element is higher than element on middle index
        else if (v > a[mid]) {
            // Call method again, change lower limit to one more than middle (we know middle isn't the correct element)
            return findBinary(a, mid + 1, r, v);
        }

        return -1;
    }

    long timeLinear(int n) {
        int [] arr = generateTable(n);

        long startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            int rand = (int)(Math.random() * n + 1);
            findLinear(arr, rand);
        }

        long executionTime = System.nanoTime() - startTime;

        return executionTime / 1000;
    }

    long timeBinary(int n) {
        int [] arr = generateTable(n);

        long startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            int rand = (int)(Math.random() * n + 1);
            findBinary(arr, 0, n - 1, rand);
        }

        long executionTime = System.nanoTime() - startTime;

        return executionTime / 1000;
    }

    void experimentalAlgorithmEvaluation() {
        int start = 100000;

        System.out.printf("   n %7s|%4s linearno  | %2s dvojisko  |\n", " ", " ", " ");
        System.out.print("------------+---------------+--------------+\n");

        for (int i = start; i <= 1000000; i += 10000) {
            int n = i;
            long tL = timeLinear(i);
            long tB = timeBinary(i);
            System.out.printf("%12d|%15d|%14d|\n", n, tL, tB);
            //System.out.print("|");
            //System.out.printf("%15d", tL);
            //System.out.print("|");
            //System.out.printf("%14d", tB);
            //System.out.print("|");
            //System.out.println("");
        }
    }
}

