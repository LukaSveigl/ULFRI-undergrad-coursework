import java.util.Arrays;

public class Kviz1 {
    public static void main(String[] args) {
        System.out.println(prepleti("Kako si kaj danes?", "Dobro, hvala!"));
    }

    public static int[] unija(int[] arr1, int[] arr2) {
        int [] union = new int[arr1.length + arr2.length];
        for (int i = 0; i < arr1.length; i++) {
            union[i] = arr1[i];
        }
        for (int i = 0; i < arr2.length; i++) {
            union[arr1.length + i] = arr2[i];
        }

        return union;
    }

    public static int[] presek(int[] arr1, int[] arr2) {
        int[] intersectMax = new int[arr1.length + arr2.length];

        int count = 0;
        boolean found = false;
        for (int i = 0; i < arr1.length; i++) {
            found = false;
            for (int j = 0; j < arr2.length; j++) {
                if (arr1[i] == arr2[j]) {
                    for (int k = 0; k < intersectMax.length; k++) {
                        if (arr1[i] == intersectMax[k]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        intersectMax[count] = arr1[i];
                        count++;
                    }
                }
            }
        }
        int[] inter = new int[count];
        for (int i = 0; i < inter.length; i++){
            inter[i] = intersectMax[i];
        }
        return inter;
    }

    public static int[] range(int a, int b, int c) {
        int [] list = new int[100];
        int sum = 0;
        int count = 0;
        while (true) {
            sum = a + (count * c);
            if (sum >= b) {
                break;
            }
            list[count] = sum;
            count++;
        }

        int [] realArr = new int [count - 1];
        for (int i = 0; i < realArr.length; i++) {
            realArr[i] = list[i];
        }

        return realArr;
    }

    public static void rotiraj(int[] tab, int k) {
        for (int i = 0; i < k; i++) {
            int tmp = 0;
            tmp = tab[0];

            for (int j = 0; j < tab.length - 1; j++) {
                tab[j] = tab[j + 1];
            }
            tab[tab.length - 1] = tmp;
        }
    }

    public static int[] duplikati(int [] tab) {
        int [] tempArr = new int[tab.length];

        int count = 0;

        for (int i = 0; i < tab.length; i++) {
            boolean found = false;
            for (int j = 0; j < tempArr.length; j++) {
                if (tempArr[j] == tab[i]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                tempArr[count] = tab[i];
                count++;
            }
        }

        int [] realArr = new int [count];
        for (int i = 0; i < realArr.length; i++) {
            realArr[i] = tempArr[i];
        }

        return realArr;
    }

    public static double koren(int x, int d) {
        double root = 0;

        int c = 0;
        int[] cx = new int[d];

        java.util.Arrays.fill(cx, 0);

        while (true) {
            if (c * c <= x && (c + 1) * (c + 1) > x) {
                break;
            }
            c++;
        }
        int multiplier = 10;
        for (int i = 0; i < cx.length; i++) {
            if (i == 0) {
                for (int j = 1; j <= 9; j++) {
                    int curr = (c + j)/multiplier;
                    if (curr * curr <= x && (curr + 1/10) * (curr + 1/10) > x) {
                        cx[i] = curr;
                    }
                }
            }
            else {
                for (int j = 1; j <= 9; j++) {
                    int curr = (cx[i - 1] + j)/multiplier;
                    if (curr * curr <= x && (curr + 1/10) * (curr + 1/10) > x) {
                        cx[i] = curr;
                    }
                }
            }
            multiplier *= 10;
        }
        String dec = String.join(", ", java.util.Arrays.toString(cx).replace("[", "").replace("]", ""));
        System.out.println(java.util.Arrays.toString(cx));
        return c * (d + 1);
    }

    public static String binarnoSestej(String s, String b) {
        String rezultat = "";

        //String longer = (s.length() > b.length()) ? s : b;
        //String shorter = (s.length() < b.length()) ? b : s;

        String longer, shorter;

        if (s.length() >= b.length()) {
            longer = s;
            shorter = b;
        }
        else {
            longer = b;
            shorter = s;
        }

        longer = new StringBuilder(longer).reverse().toString();
        shorter = new StringBuilder(shorter).reverse().toString();

        boolean carry = false;

        for (int i = 0; i < shorter.length(); i++) {
            if (shorter.charAt(i) == '0' && longer.charAt(i) == '0') {
                rezultat += (carry) ? '1' : '0';
                carry = false;
            }
            else if (shorter.charAt(i) == '1' && longer.charAt(i) == '1') {
                rezultat += (carry) ? '1' : '0';
                carry = true;
            }
            else {
                if (carry) {
                    rezultat += '0';
                    carry = true;
                }
                else {
                    rezultat += '1';
                    carry = false;
                }
            }
        }
        if (rezultat.length() != longer.length()) {
            for (int i = rezultat.length(); i < longer.length(); i++) {
                if (longer.charAt(i) == '1') {
                    rezultat += (carry) ? '0' : '1';
                }
                else {
                    rezultat += (carry) ? '1' : '0';
                    carry = false;
                }
            }
        }
        if (carry) {
            rezultat += '1';
        }


        return new StringBuilder(rezultat).reverse().toString();
    }

    public static int vsotaSkupnihCifer(int a, int b) {
        String as = Integer.toString(a);
        String bs = Integer.toString(b);

        int [] nums = new int[Math.max(as.length(), bs.length())];

        java.util.Arrays.fill(nums, 0);

        int result = 0;

        int count = 0;

        for (int i = 0; i < as.length(); i++) {
            boolean found = false;
            for (int j = 0; j < bs.length(); j++) {
                if (as.charAt(i) == bs.charAt(j)) {
                    for (int k = 0; k < nums.length; k++) {
                        if (Character.getNumericValue(as.charAt(i)) == nums[k]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        nums[count] = Character.getNumericValue(as.charAt(i));
                        count++;
                    }
                }
            }

        }
        System.out.println(a);
        System.out.println(b);
        System.out.println(java.util.Arrays.toString(nums));
        for (int i = 0; i < nums.length; i++) {
            result += nums[i];
        }

        return result;
    }

    public static String prepleti (String first, String second) {
        String result = "";


        int diff = first.length() - second.length();
        if (diff < 0) {
            for (int i = 0; i < diff * -1; i++) {
                first += " ";
            }
        }
        else if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                second += " ";
            }
        }

        for (int j = 0; j < first.length(); j++) {
            result += first.charAt(j);
            result += second.charAt(j);
        }
        return result;
    }

    public static void odpleti(String tied) {
        String first = "";
        String second = "";

        for (int i = 0; i < tied.length(); i++) {
            if (i % 2 == 0) {
                first += tied.charAt(i);
            }
            else {
                second += tied.charAt(i);
            }
        }

        System.out.println(first);
        System.out.println(second);
    }
}
