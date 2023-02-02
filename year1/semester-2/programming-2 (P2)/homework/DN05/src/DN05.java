public class DN05 {
    public static void main(String[] args) {
        if (args.length > 0) {
            int[] chars = new int[26];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = 0; // Nastavimo vse el. v tabeli na 0 - ni pojavitev znakov
            }

            int idx = 0;

            for (int countWords = 0; countWords < args.length; countWords++) {
                for (int countChars = 0; countChars < args[countWords].length(); countChars++) {
                    idx = args[countWords].charAt(countChars) - 'a';
                    if (idx >= 0 && idx <= 26) {
                        chars[idx]++;
                    }
                }
            }

            boolean najdene = false;
            for (int k = 0; k < chars.length; k++){
                if (chars[k] != 0) {
                    najdene = true;
                    break;
                }
            }

            if (najdene) {
                boolean first = true;
                System.out.print("V nizu '" + String.join(" ", args) + "' se pojavijo crke ");
                for (int c = 0; c < chars.length; c++) {
                    if (chars[c] != 0) {
                        if (first) {
                            first = false;
                        }
                        else {
                            System.out.print(", ");
                        }
                        char character = (char) ('a' + c);
                        System.out.printf("%c(%d)", character, chars[c]);
                    }
                }
                System.out.print(".");
            }
            else {
                System.out.print("V nizu '" + String.join(" ", args) + "' ni malih crk angleske abecede.");
            }

        }
        else {
            System.out.println("V nizu '' ni malih crk angleske abecede.");
        }
    }
}
