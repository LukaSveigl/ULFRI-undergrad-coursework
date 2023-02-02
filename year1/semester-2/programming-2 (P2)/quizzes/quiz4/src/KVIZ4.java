import java.util.Locale;

public class KVIZ4 {
    public static void main(String[] args) {
        //izpisiBesedilo("besedilo2.txt", 15, 19);

        preberiRacunInIzpisi("besedilo3.txt");
    }

    /*void dvojnaNagrade(String igralkeFilename, String igralciFilename) {
        java.util.ArrayList<String> igralkeData = new java.util.ArrayList<>();
        java.util.ArrayList<String> igralciData = new java.util.ArrayList<>();
        java.util.ArrayList<String> movies = new java.util.ArrayList<>();

        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(igralkeFilename));

            while(sc.hasNextLine()) {
                igralkeData.add(sc.nextLine());
            }

            sc.close();
            sc = new java.util.Scanner(new java.io.File(igralciFilename));

            while(sc.hasNextLine()) {
                igralciData.add(sc.nextLine());
            }

            sc.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (igralkeData.size() > igralciData.size()) {
            for (int i = 0; i < igralciData.size(); i++) {
                String[] tempIgralec = igralciData.get(i).split(", ");
                String[] tempIgralke = igralkeData.get(i).split(", ");

                if (tempIgralec[tempIgralec.length - 1].equals(tempIgralke[tempIgralke.length - 1])) {
                    movies.add(String.format())
                }
            }
        }
        else {

        }
    }*/

    static void statistikaStavkov(String imeDatoteke) throws IzjemaManjkajocegaLocila {
        java.util.ArrayList<String> stavki = new java.util.ArrayList<>();
        java.util.HashMap<Integer, Integer> counts = new java.util.HashMap<>();

        IzjemaManjkajocegaLocila iz = new IzjemaManjkajocegaLocila("Napaka pri branju datoteke");

        try {

                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(imeDatoteke));
                int ch = 0;
                String statement = "";
                while ((ch = br.read()) != -1) {
                    char chr = (char) ch;

                    if (chr == '.' || chr == '!' || chr == '?') {
                        stavki.add(statement);

                        statement = "";
                    } else if (statement.equals("") && chr == ' ') {
                        continue;
                    } else {
                        statement += chr;
                    }
                }
        } catch (Exception e) {
            System.out.println(e);
            throw iz;

        }

        for (String s : stavki) {
            s = s.replaceAll(" +", " ");
            // System.out.println(s.split(" ").length + " " + s);
            String [] words = s.split(" ");
            if (counts.containsKey(words.length)) {
                counts.put(words.length, counts.get(words.length) + 1);
            }
            else {
                counts.put(words.length, 1);
            }
        }

        for (int i : counts.keySet()) {
            System.out.printf("Stavki dolzine %d se pojavijo: %dx.\n", i, counts.get(i));
        }
    }

    static class IzjemaManjkajocegaLocila extends Exception {
        public IzjemaManjkajocegaLocila(String message) {
            super(message);
        }
    }

    static void izpisiBesedilo(String imeDatoteke, int n, int s) {
        String str = "";

        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(imeDatoteke));

            while(sc.hasNextLine()) {
                str += sc.nextLine() + " ";
            }

            sc.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        str = str.replaceAll(" +", " ");

        String [] words = str.split(" ");
        java.util.ArrayList<String> strs = new java.util.ArrayList<>();

        String strin = "";

        for (String st : words) {

            if ((strin + " " +  st).length() <= n) {
                if (!strin.equals(""))
                    strin += " " + st;
                else {
                    strin += st;
                }
            }
            else {
                strs.add(strin);
                strin = "" + st;
            }
        }
        strs.add(strin);

        for (String st : strs) {

            int missing = s - st.length();

            if(missing % 2 == 0) {
                for (int i = 0; i < missing / 2; i++) {
                    System.out.print(".");
                }

                System.out.print(st);

                for (int i = 0; i < missing / 2; i++) {
                    System.out.print(".");
                }
                System.out.println();

            }
            else {
                for (int i = 0; i < missing / 2; i++) {
                    System.out.print(".");
                }

                System.out.print(st);

                for (int i = 0; i < missing / 2; i++) {
                    System.out.print(".");
                }
                System.out.print(".");
                System.out.println();
            }
        }
    }

    static void preberiRacunInIzpisi(String imeDatoteke) {

        double noDDV = 0;
        double DDV = 0;
        double sum = 0;

        try {
            java.util.Scanner sc = new java.util.Scanner(new java.io.File(imeDatoteke));

            while(sc.hasNextLine()) {
                String [] items = sc.nextLine().split("\t");
                if (items.length == 3) {
                    DDV += Double.parseDouble(items[1].replace(",", "."));
                    noDDV += Double.parseDouble(items[2].replace(",", "."));
                }
            }

            sc.close();

        } catch (Exception e) {
            System.out.println("Napaka pri branju datoteke!");
            return;
        }

        sum = DDV + noDDV;

        System.out.printf("Skupaj brez DDV:  %5.2f\n", noDDV - DDV);
        System.out.printf("DDV:              %5.2f\n", DDV);
        System.out.printf("ZNESEK SKUPAJ:    %5.2f\n", noDDV);
    }

}

interface SkladInterface {

    public boolean isEmpty();   // je sklad prazen?

    public void push(Object o); // doda element na vrh sklada

    public Object pop();        // vrne element z vrha sklada

    public void reverse();      // obrne vrstni red elementov na skladu

}

class Sklad implements SkladInterface {

    java.util.ArrayList<Object> stack = new java.util.ArrayList<>();

    public Sklad() {

    }

    @Override
    public boolean isEmpty() {
        return this.stack.size() <= 0;
    }

    @Override
    public void push(Object o) {
        this.stack.add(o);
    }

    @Override
    public Object pop() {
        Object item = stack.get(stack.size() - 1);
        stack.remove(item);
        return item;

    }

    @Override
    public void reverse() {
        java.util.Collections.reverse(this.stack);
    }
}

class ArrayListPlus extends java.util.ArrayList {
    java.util.ArrayList<Object> arr = new java.util.ArrayList<>();

    public ArrayListPlus() {

    }

    public ArrayListPlus(String elements) {
        String [] items = elements.split(";");
        for (String i : items) {
            arr.add(i);
        }
    }

    @Override
    public Object set(int index, Object element) {
        if (index >= arr.size()) {
            for (int i = arr.size(); i < index; i++) {
                arr.add("");
            }
            arr.add(element);
        }
        else {
            return arr.set(index, element);
        }
        return this;
    }

    @Override
    public String toString() {
        String str = "";

        for (Object o : arr) {
            str += o + ";";
        }
        str = str.substring(0, str.length() - 1);

        return str;
    }
}