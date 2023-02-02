import java.util.Arrays;
import java.util.Locale;

public class Kviz3 {
    public static void main(String[] args) {
        int [] a = {1, 2, 3};
        int [] b = {4, 5, 6};
        System.out.println(Arrays.toString(sestejPolinoma(a, b)));

        System.out.println(Arrays.toString(zmnoziPolinoma(a, b)));

        System.out.println(najdaljsiPalindrom("perica reze raci rep", false));

        System.out.println(Tocka.tabelaToString(Tocka.preberiTocke("tocke.txt")));

        Tocka [] tocke = Tocka.preberiTocke("tocke.txt");

        for (Tocka t : tocke) {
            System.out.println(Integer.toString(t.x) + " " + Integer.toString(t.y));
        }

        Matrika m = Matrika.preberiMatriko("m1.txt");
        m.izpisi();

        System.out.println();

        Matrika m2 = Matrika.preberiMatriko("m2.txt");
        m2.izpisi();

        System.out.println();

        Matrika m3 = m.zmnozi(m2);
        m3.izpisi();
    }

    public static int [] sestejPolinoma(int [] a, int [] b) {
        int [] result = (a.length >= b.length) ? a : b;
        boolean aFirst = a.length >= b.length;

        if (aFirst) {
            for (int i = 0; i < b.length; i++) {
                result[i] += b[i];
            }
        }
        else {
            for (int i = 0; i < a.length; i++) {
                result[i] += b[i];
            }
        }

        return result;
    }

    public static int [] zmnoziPolinoma(int [] a, int [] b) {
        int [] result = new int[a.length + b.length - 1];

        java.util.Arrays.fill(result, 0);

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                result[i + j] += a[i] * b[j];
            }
        }

        return result;
    }

    public static boolean jeAnagram(String firstWord, String secondWord, boolean notCaseSensitive) {

        if (firstWord.length() != secondWord.length()) {
            return false;
        }

        if (notCaseSensitive) {
            firstWord = firstWord.toLowerCase();
            secondWord = secondWord.toLowerCase();
        }
        char [] charFirstWord = firstWord.toCharArray();
        char [] charSecondWord = secondWord.toCharArray();

        java.util.Arrays.sort(charFirstWord);
        java.util.Arrays.sort(charSecondWord);

        String sortedFirstWord = new String(charFirstWord);
        String sortedSecondWord = new String(charSecondWord);

        for (int i = 0; i < sortedFirstWord.length(); i++) {
            if (sortedFirstWord.charAt(i) != sortedSecondWord.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static String najdaljsiPalindrom(String word, boolean countSpaces) {
        String result = "";

        if (countSpaces) {
            for (int i = 0; i < word.length(); i++) {
                for (int j = i; j < word.length(); j++) {
                    boolean found = true;

                    for (int k = 0; k < (j - i + 1) / 2; k++) {
                        if (word.charAt(i + k) != word.charAt(j - k)) {
                            found = false;
                        }
                    }

                    if(found && (j - i + 1) > result.length()) {
                        result = word.substring(i, i + (j - i + 1));
                    }
                }
            }
        }
        else {
            for (int i = 0; i < word.length(); i++) {
                for (int j = i; j < word.length(); j++) {
                    boolean found = true;

                    String substr = word.substring(i, i + (j - i + 1)).replace(" ", "");
                    for (int k = 0; k < (substr.length() / 2); k++) {
                       if (substr.charAt(k) != substr.charAt(substr.length() - 1 - k)) {
                           found = false;
                       }
                    }

                    if(found && (j - i + 1) > result.length()) {
                        result = word.substring(i, i + (j - i + 1));
                    }
                }
            }
        }
        return result;
    }

}

class Tocka {
    public int x, y;

    public static Tocka[] preberiTocke(String imeDatoteke) {
        java.util.ArrayList<Tocka> tocke = new java.util.ArrayList<Tocka>();

        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(imeDatoteke));

            while(sc.hasNextInt()) {
                Tocka temp = new Tocka();

                temp.x = sc.nextInt();
                temp.y = sc.nextInt();

                tocke.add(temp);
            }
            sc.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        Tocka [] points = tocke.toArray(new Tocka[0]);
        return points;
    }

    public static String tabelaToString(Tocka [] tocke) {
        String [] result = new String[tocke.length];
        int count = 0;
        for (int i = 0; i < tocke.length; i++) {
            result[i] = String.format("(%d,%d)", tocke[i].x, tocke[i].y);
        }

        return java.util.Arrays.toString(result);
    }

    public static void najblizji(Tocka [] t1, Tocka [] t2) {
        Tocka close1 = new Tocka();
        Tocka close2 = new Tocka();

        if (t1.length == 0) {
            System.out.println("Prva tabela ne vsebuje točk");
            return;
        }
        if (t2.length == 0) {
            System.out.println("Druga tabela ne vsebuje točk");
            return;
        }

        double maxDist = 100;

        for (int i = 0; i < t1.length; i++) {
            for (int j = 0; j < t2.length; j++) {
                double dist = Math.sqrt((Math.abs(t1[i].x - t2[j].x))+(Math.abs(t1[i].y - t2[j].y)));
                if (dist < maxDist) {
                    maxDist = dist;
                    close1 = t1[i];
                    close2 = t2[j];
                }
            }
        }

        System.out.printf("Najbližji točki sta Tocka (%d, %d) in Tocka (%d, %d), razdalja med njima je %.2f", close1.x, close1.y, close2.x, close2.y, maxDist);
    }
}

class Matrika {

    public int[][] matrix = {};

    public static Matrika preberiMatriko(String imeDatoteke) {
        Matrika mat = new Matrika();
        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(imeDatoteke));

            int size = sc.nextInt();

            mat.matrix = new int[size][size];

            int count = 0;
            int countLine = 0;
            while(sc.hasNextInt()) {
                int val = sc.nextInt();

                mat.matrix[countLine][count] = val;
                count++;
                if (count == 4) {
                    count = 0;
                    countLine++;
                }
            }

            sc.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        return mat;
    }

    public Matrika zmnozi(Matrika b) {
        Matrika result = new Matrika();

        if(b.matrix.length != this.matrix.length) {
            System.out.println("Matrike napacnih dolzin");
            return result;
        }

        result.matrix = new int[b.matrix.length][b.matrix.length];

        for (int i = 0; i < b.matrix.length; i++) {
            for (int j = 0; j < b.matrix[0].length; j++) {
                for (int k = 0; k < b.matrix.length; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * b.matrix[k][j];
                }
            }
        }

        return result;
    }

    public void izpisi() {
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                System.out.printf(" %2d", this.matrix[i][j]);
            }
            System.out.println();
        }
    }
}
