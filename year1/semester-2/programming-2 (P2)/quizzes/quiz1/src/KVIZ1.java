import java.util.Locale;

public class KVIZ1 {
    public static void main(String [] args) {
        java();

        System.out.println();

        kalkulator(42, 13);

        System.out.println();

        nicli(1,-7, 12);

        System.out.println();

        krog(7.5, 3);

        System.out.println();

        System.out.println(pretvoriSekunde(-12));

        System.out.println();

        javaJavaJava(3);

        System.out.println();
        System.out.println();

        System.out.println(jeFibonaccijevo(14));

        System.out.println();
        System.out.println();

        izrisiZastavo(2);

        System.out.println();
        System.out.println();

        vDesetisko(129);

        System.out.println();
        System.out.println();

        System.out.println(pretvoriVDesetisko("101010", 2));
        System.out.println(pretvoriVDesetisko("FF", 16));
        System.out.println(pretvoriVDesetisko("103", 8));
        System.out.println(pretvoriVDesetisko("10201", 2));
        System.out.println(pretvoriVDesetisko("FG", 16));

        System.out.println();
        System.out.println();

        narisiDrevo(6);

        System.out.println();
        System.out.println();

        izracunajRazliko("14:23:10", "11:10:05");

        System.out.println();
        System.out.println();

        System.out.println(pretvoriVMorse("Na FRIju nam je res lepo"));

        System.out.println();
        System.out.println();

        trikotnik(5, 1);
        System.out.println();
        trikotnik(5, 2);
        System.out.println();
        trikotnik(5,3);
        System.out.println();
        trikotnik(5, 4);
        System.out.println();
        trikotnik(5,5);
        System.out.println();
        trikotnik(5, 6);
        System.out.println();
        trikotnik(15, 7);

        System.out.println();
        System.out.println();

        metulj(5, 1);
        System.out.println();
        metulj(5, 2);
        System.out.println();
        metulj(5, 3);

    }

    static void java() {
        System.out.println("   J    a   v     v  a");
        System.out.println("   J   a a   v   v  a a");
        System.out.println("J  J  aaaaa   V V  aaaaa");
        System.out.println(" JJ  a     a   V  a     a");

    }

    static void kalkulator(int a, int b) {
        if (b == 0) {
            System.out.println("Napaka: deljenje z 0");
        }
        else {
            System.out.printf("%d + %d = %d\n", a, b, a + b);
            System.out.printf("%d - %d = %d\n", a, b, a - b);
            System.out.printf("%d x %d = %d\n", a, b, a * b);
            System.out.printf("%d / %d = %d\n", a, b, a / b);
            System.out.println(Integer.toString(a) + " % " + Integer.toString(b) + " = " + Integer.toString(a % b));
        }
    }

    static void nicli(int a, int b, int c) {
        double D = b*b - 4 * a * c;

        if (D < 0) {
            System.out.println("Napaka: nicli enacbe ne obstajata");
        }
        else {
            double rootD = Math.sqrt(D);

            double x1 = (-b + rootD) / (2 * a);
            double x2 = (-b - rootD) / (2 * a);

            System.out.printf("x1=%.2f, x2=%.2f", x1, x2);
        }
    }

    static void krog(double r, int d) {
        if (r < 0) {
            System.out.println("Napaka: negativen polmer");
        }
        else if (d < 0) {
            System.out.println("Napaka: negativen d");
        }
        else {
            double circumference = 2 * Math.PI * r;
            double area = Math.PI * r * r;

            String formatCirc = "Obseg kroga s polmerom r=%.2f je %." + Integer.toString(d) + "f\n";
            String formatArea = "Ploscina kroga s polmerom r=%.2f je %." + Integer.toString(d) + "f";

            System.out.printf(formatCirc, r, circumference);
            System.out.printf(formatArea, r, area);
        }
    }

    static String pretvoriSekunde(int sekunde) {
        if (sekunde < 0) {
            System.out.println("Število sekund ne more biti negativno");
        }
        else {
            int sec = sekunde % 60;
            int min = (sekunde / 60);
            int hr = min / 60;
            min %= 60;

            String time = String.format("%02d:%02d:%02d", hr, min, sec);

            return time;

        }

        return "";
    }

    static void javaJavaJava(int n) {
        if (n < 0) {
            System.out.println("Napaka: negativen n");
        }
        else {
            for(int i = 0; i < n; i++) {
                System.out.print("  ");
                System.out.print("   J    a   v     v  a   ");
            }
            System.out.println();

            for(int i = 0; i < n; i++) {
                System.out.print("  ");
                System.out.print("   J   a a   v   v  a a  ");
            }
            System.out.println();

            for(int i = 0; i < n; i++) {
                System.out.print("  ");
                System.out.print("J  J  aaaaa   V V  aaaaa ");
            }
            System.out.println();

            for(int i = 0; i < n; i++) {
                System.out.print("  ");
                System.out.print(" JJ  a     a   V  a     a");
            }
        }
    }

    static boolean jeFibonaccijevo(int n) {
        int first = 1;
        int second = 1;
        int third = 0;

        while (third < n) {
            third = first + second;
            if (third == n) {
                return true;
            }
            first = second;
            second = third;
        }
        return false;
    }

    static boolean jePrastevilo(int n) {
        if (n <= 0) {
            return false;
        }
        else {
            int count = 0;

            for(int i = 1; i <= n; i++) {
                if (n % i == 0) {
                    count++;
                }
            }

            if (count == 2) {
                return true;
            }
        }
        return false;
    }

    static void izrisiZastavo(int n) {
        int stars = n * 2;
        int colors = (n * 12) - (n - 1);

        int full = (stars * 2) + colors;

        int height = n * 3;

        for (int h = 0; h < height; h++) {
            if (h % 2 == 0) {
                for (int s = 0; s < stars; s++) {
                    System.out.print("* ");
                }
            }
            else {
                System.out.print(" ");
                for (int s = 0; s < stars - 1; s++) {
                    System.out.print("* ");
                }
                System.out.print(" ");
            }
            for(int c = 0; c < colors; c++) {
                System.out.print("=");
            }
            System.out.println();
        }
        for (int c = 0; c < 2 * n; c++) {
            for (int f = 0; f < full; f++) {
                System.out.print("=");
            }
            System.out.println();
        }
    }

    static void vDesetisko(int n) {
        String octal = Integer.toString(n);
        int decimal = 0;

        int len = octal.length();

        boolean wrong = false;

        for (int d = 0; d < octal.length(); d++) {
            int value = Character.getNumericValue(octal.charAt(d));
            if (value >= 8) {
                System.out.printf("Število %d ni število v osmiškem sistemu (števka %d)", n, value);
                wrong = true;
                break;
            }
            double power = Math.pow(8, (len - d - 1));

            decimal += value * power;
        }
        if (!wrong) {
            System.out.printf("%d(8) = %d(10)", n, decimal);
        }
    }

    static String pretvoriVDesetisko(String n, int b) {
        boolean wrong = false;
        int wrongValue = 0;

        int decimal = 0;

        if (b == 2) {
            for (int c = 0; c < n.length(); c++) {
                if (Character.getNumericValue(n.charAt(c)) > 1) {
                    wrong = true;
                    wrongValue = Character.getNumericValue(n.charAt(c));
                    return "Napaka pri pretvorbi sistema - števka " + wrongValue;
                }
            }
            if (!wrong) {
                decimal = Integer.parseInt(n, b);
            }
        }
        else if (b == 5) {
            for (int c = 0; c < n.length(); c++) {
                if (Character.getNumericValue(n.charAt(c)) > 4) {
                    wrong = true;
                    wrongValue = Character.getNumericValue(n.charAt(c));
                    return "Napaka pri pretvorbi sistema - števka " + wrongValue;
                }
            }
            if (!wrong) {
                decimal = Integer.parseInt(n, b);
            }
        }
        else if (b == 8) {
            for (int c = 0; c < n.length(); c++) {
                if (Character.getNumericValue(n.charAt(c)) > 7) {
                    wrong = true;
                    wrongValue = Character.getNumericValue(n.charAt(c));
                    return "Napaka pri pretvorbi sistema - števka " + wrongValue;
                }
            }
            if (!wrong) {
                decimal = Integer.parseInt(n, b);
            }

        }
        else if (b == 16) {
            for (int c = 0; c < n.length(); c++) {
                if (Character.getNumericValue(n.charAt(c)) > 15) {
                    wrong = true;
                    return "Napaka pri pretvorbi sistema - števka " + n.charAt(c);
                }
            }
            if (!wrong) {
                decimal = Integer.parseInt(n, b);
            }

        }

        if (!wrong) {
            return String.format("%s(%d)=%d(%d)", n, b, decimal, 10);
        }
        else {
            return "";
        }
    }

    static int vsotaPrvih(int n) {
        int count = 0;
        int number = 1;
        int sum = 0;

        while(count < n) {
            boolean pra = false;

            int cnt = 0;

            for(int i = 1; i <= number; i++) {
                if (number % i == 0) {
                    cnt++;
                }
            }

            if (cnt == 2) {
                pra = true;
            }

            if (pra) {
                count++;
                sum += number;
            }
            number++;
        }

        return sum;
    }

    static void pitagoroviTrojcki(int x) {
        for (int i = 3; i < x; i++) {
            for (int j = i; j < x + 1; j++) {
                for (int k = j; k < x + 1; k++) {
                    if (i * i + j * j == k * k) {
                        System.out.printf("%d %d %d\n", i, j, k);
                    }
                }
            }
        }
    }

    static void narisiDrevo(int n) {
        if (n < 1) {
            System.out.println(" . ");
        }
        else {
            int printed = 0;

            if (n % 2 == 0) {
                System.out.println("* *");
                printed += 2;
            }
            else {
                System.out.println(" * ");
                printed += 1;
            }

            while(printed != n) {
                if (n - printed > 2) {
                    System.out.println("* *");
                    printed += 2;
                }
                else {
                    System.out.println(" | ");
                    printed += 1;
                }
            }
        }
    }

    static String izracunajRazliko(String prviCas, String drugiCas) {
        int firstSeconds = 0;
        int secondSeconds = 0;

        firstSeconds += Integer.parseInt(prviCas.substring(0, prviCas.indexOf(":"))) * 60 * 60;
        firstSeconds += Integer.parseInt(prviCas.substring(prviCas.indexOf(":") + 1, prviCas.indexOf(":", prviCas.indexOf(":") + 1))) * 60;
        firstSeconds += Integer.parseInt(prviCas.substring(prviCas.indexOf(":", prviCas.indexOf(":") + 1) + 1));

        secondSeconds += Integer.parseInt(drugiCas.substring(0, drugiCas.indexOf(":"))) * 60 * 60;
        secondSeconds += Integer.parseInt(drugiCas.substring(drugiCas.indexOf(":") + 1, drugiCas.indexOf(":", drugiCas.indexOf(":") + 1))) * 60;
        secondSeconds += Integer.parseInt(drugiCas.substring(drugiCas.indexOf(":", drugiCas.indexOf(":") + 1) + 1));

        int difference = Math.abs(firstSeconds - secondSeconds);

        if (difference < 0) {
            System.out.println("Število sekund ne more biti negativno");
        }
        else {
            int sec = difference % 60;
            int min = (difference / 60);
            int hr = min / 60;
            min %= 60;

            String time = String.format("%02d:%02d:%02d", hr, min, sec);

            return time;

        }

        return "";
    }

    static String pretvoriVMorse(String sporocilo) {
        String message = "";

        String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                            "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                ".", ",", "?"};

        String[] morseChars = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..",
                                "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-",
                                    "-.--", "--..", ".----", "..---", "...--", "....-", ".....", "-....", "--...",
                                        "---..", "----.", "-----", ".-.-.-", "--..--", "..--.."};

        String wordMsg = sporocilo.toLowerCase();

        for (int count = 0; count < wordMsg.length(); count++) {
            if (wordMsg.charAt(count) == ' ') {
                message += "  ";
            }
            else {
                boolean exists = false;
                for (int countChars = 0; countChars < chars.length; countChars++) {
                    if (wordMsg.charAt(count) == chars[countChars].charAt(0)) {
                        message += morseChars[countChars] + " ";
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    message += "? ";
                }
            }
        }

        return message;
    }

    static void praDvojcek(int n) {
        int count1, count2 = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                if (i - j == 2) {
                    count1 = 0;
                    count2 = 0;
                    for (int c1 = 1; c1 <= i; c1++) {
                        if (i % c1 == 0){
                            count1++;
                        }
                    }
                    for (int c2 = 1; c2 <= j; c2++) {
                        if (j % c2 == 0) {
                            count2++;
                        }
                    }

                    if (count1 == 2 && count2 == 2) {
                        System.out.printf("(%d, %d)\n", j, i);
                    }
                }
            }
        }
    }

    static void trikotnik(int n, int tip) {
        if (tip == 1) {
            int x = 0;
            for (int c = 1; c <= n; c++){
                for (int c2 = 1; c2 <= c; c2++) {
                    x = c2;
                    if (x >= 10) {
                        x -= 10;
                    }
                    System.out.printf("%d ", x);
                }
                System.out.println();
            }
        }
        else if (tip == 2) {
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int s = 0; s < n - x; s++) {
                    System.out.print("  ");
                }
                for (int c2 = 1; c2 <= x; c2++) {
                    y = c2;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                System.out.println();
            }
        }
        else if (tip == 3) {
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int s = 0; s < x - 1; s++) {
                    System.out.print("  ");
                }
                for (int c2 = n - (x - 1); c2 >= 1; c2--) {
                    y = c2;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                System.out.println();
            }
        }
        else if (tip == 4) {
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int c2 = x; c2 >= 1; c2--) {
                    y = c2;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                System.out.println();
            }
        }
        else if (tip == 5) {
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int s = 0; s < x - 1; s++) {
                    System.out.print("  ");
                }
                for (int c2 = 1; c2 <= n - (x - 1); c2++) {
                    y = c2;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int c3 = n - (x - 1) - 1; c3 >= 1; c3--) {
                    y = c3;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                System.out.println();
            }
        }
        else if (tip == 6) {
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int s = 0; s < n - x; s++) {
                    System.out.print("  ");
                }
                for (int c2 = 1; c2 <= x; c2++) {
                    y = c2;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int c3 = x - 1; c3 >= 1; c3--) {
                    y = c3;
                    if (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                System.out.println();
            }
        }
        else if (tip == 7) {
            int start = 1;
            int max = 1;
            int x = n;
            int y = 0;
            for (int c = 1; c <= n; c++) {
                for (int s = 0; s < x - 1; s++) {
                    System.out.print("  ");
                }
                for (int c2 = start; c2 <= max; c2++ ) {
                    y = c2;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int c3 = max - 1; c3 >= start; c3--) {
                    y = c3;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                max += 2;
                x--;
                start++;
                System.out.println();
            }
        }
    }

    static void metulj(int n, int tip) {
        // Ta metoda je tut katastrofa, dal bi se izbolsat pa znebit
        // odvecnih spremenljivk
        if (tip == 1) {
            int start = 3 * n - 1;
            int x = n;
            int f = 1;
            int y = 0;
            for (int c = 0; c < n; c++) {
                for (int c2 = 1; c2 <= n - (x - 1); c2++) {
                    y = c2;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int s = 0; s < start; s++) {
                    System.out.print(" ");
                }
                for (int c3 = f; c3 >= 1; c3--) {
                    y = c3;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                if (f + 1 < n) {
                    f++;
                }
                start -= 4;
                System.out.println();
            }
        }
        else if (tip == 2) {
            int start = 0;
            int x = n;
            int f = 1;
            int y = 0;
            int k = 1;
            for (int c = 0; c < n; c++) {
                for (int c2 = 1; c2 <= x; c2++) {
                    y = c2;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int s = 0; s < start; s++) {
                    System.out.print(" ");
                }
                for (int c3 = n - f ; c3 >= 1; c3--) {
                    y = c3;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                if (x != n) {
                    f++;
                }
                x--;
                start = k * 2;
                k += 2;
                System.out.println();
            }
        }
        else if (tip == 3) {
            int start = 3 * n - 1;
            int x = n;
            int f = 1;
            int y = 0;
            for (int c = 0; c < n; c++) {
                for (int c2 = 1; c2 <= n - (x - 1); c2++) {
                    y = c2;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int s = 0; s < start; s++) {
                    System.out.print(" ");
                }
                for (int c3 = f; c3 >= 1; c3--) {
                    y = c3;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                if (f + 1 < n) {
                    f++;
                }
                start -= 4;
                System.out.println();
            }
            start = 2;
            x = n;
            y = 0;
            int k = 3;
            for (int c = 0; c < n - 1; c++) {
                for (int c2 = 1; c2 <= x - 1; c2++) {
                    y = c2;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                for (int s = 0; s < start; s++) {
                    System.out.print(" ");
                }
                for (int c3 = x - 1 ; c3 >= 1; c3--) {
                    y = c3;
                    while (y >= 10) {
                        y -= 10;
                    }
                    System.out.printf("%d ", y);
                }
                x--;
                start = k * 2;
                k += 2;
                System.out.println();
            }
        }
    }
}
