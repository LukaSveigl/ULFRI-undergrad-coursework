import java.io.File;
import java.io.FileInputStream;
import java.util.*;


public class DN11 {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        if (args.length > 1) {
            if (args[0].equals("mejniki") && args.length == 2) {
                Kataster kat = new Kataster(args[1]);
                kat.readMejniki();
                kat.printMejniki();
            }
            else if(args[0].equals("razdalja") && args.length == 4) {
                Kataster kat = new Kataster(args[1]);
                kat.readMejniki();
                kat.printRazdalja(args[2], args[3]);
            }
            else if (args[0].equals("parcele") && args.length == 3) {
                Kataster kat = new Kataster(args[1], args[2]);
                kat.readMejniki();
                kat.readParcele();
                kat.printParcele();
            }
            else if (args[0].equals("ograja") && args.length == 3) {
                Kataster kat = new Kataster(args[1], args[2]);
                kat.readMejniki();
                kat.readParcele();
                kat.printSmallest();
            }
            else if (args[0].equals("bin") && args.length == 2) {
                Kataster kat = new Kataster(args[1]);
                kat.readBinaryKataster();
                kat.printMejniki();
            }
            else if(args[0].equals("sosed") && args.length == 5) {
                Kataster kat = new Kataster(args[1], args[2]);
                kat.readMejniki();
                kat.readParcele();
                kat.findLargestNeighbor(args[3], args[4]);
            }
            else {
                System.out.println("Illegal number of arguments!");
            }
        }
        else {
            System.out.println("Illegal number of arguments!");
        }
    }
}



class Mejnik implements Comparable<Mejnik>{
    String name      = "";
    double latitude  = 0;  // Zemljepisna širina
    double longitude = 0; // Zemljepisna dolžina

    String fLatitude  = ""; // Formatirana širina
    String fLongitude = ""; // Formatirana dolžina


    // Constructors
    public Mejnik(String name, double latitude, double longitude) {
        this.name      = name;
        this.latitude  = latitude;
        this.longitude = longitude;

        this.fLatitude  = formatLatitude();
        this.fLongitude = formatLongitude();
    }

    public Mejnik(String name, String fLatitude, String fLongitude) {
        this.name       = name;
        this.fLatitude  = fLatitude;
        this.fLongitude = fLongitude;
    }


    // Methods of class Object
    @Override
    public String toString() {
        return String.format("Mejnik %s (%s %s)", this.name, this.fLatitude, this.fLongitude );
    }

    @Override
    public int compareTo(Mejnik o) {
        return this.name.compareTo(o.getName());
    }


    // Getters & setters
    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }


    // Formatters
    String formatLatitude() {
        double absLatitude = Math.abs(this.latitude); // Dobimo pozitivno širino

        int degrees       = (int) absLatitude; // Dobimo stopinje -> celi del
        double tmpMinutes = (absLatitude % 1) * 60; // Dobimo minute + decimalni del
        int minutes       = (int) tmpMinutes; // Dobimo samo minute
        double seconds    = (tmpMinutes % 1) * 60; // Dobimo sekunde

        return String.format("%d*%02d'%04.1f\"%c", degrees, minutes, seconds, this.latitude < 0 ? 'S' : 'N');
    }

    String formatLongitude() {
        double absLongitude = Math.abs(this.longitude); // Dobimo pozitivno širino

        int degrees       = (int) absLongitude; // Dobimo stopinje -> celi del
        double tmpMinutes = (absLongitude % 1) * 60; // Dobimo minute + decimalni del
        int minutes       = (int) tmpMinutes; // Dobimo samo minute
        double seconds    = (tmpMinutes % 1) * 60; // Dobimo sekunde

        return String.format("%d*%02d'%04.1f\"%c", degrees, minutes, seconds, this.longitude < 0 ? 'W' : 'E');
    }


    // Static methods
    static double razdalja(Mejnik m1, Mejnik m2) {
        return Math.sqrt(Math.pow(((m1.getLatitude() * 60) - (m2.getLatitude() * 60)) * 1852, 2) +
                         Math.pow(((m1.getLongitude() * 60) - (m2.getLongitude() * 60)) * 1290, 2));
    }
}



class Kataster {
    String pathMejniki = ""; // Poti do datotek
    String pathParcele = "";

    HashMap<String, Mejnik> mejniki = new HashMap<>();
    ArrayList<Parcela> parcele      = new ArrayList<>();


    // Constructors
    public Kataster(String pathMejniki) {
        this.pathMejniki = pathMejniki;
    }

    public Kataster(String pathMejniki, String pathParcele) {
        this.pathMejniki = pathMejniki;
        this.pathParcele = pathParcele;
    }


    // Getters & setters
    Parcela getParcela(String KO, String ps) {
        for (Parcela p : this.parcele) {
            if(p.getParcelnaSt().equals(ps) && p.getKatastrskaObcina().equals(KO)) {
                return p;
            }
        }
        return null; // V primeru da parcele ni v katastru
    }


    // Main methods
    //      Pairs of methods
    public void readMejniki() {
        try {
            Scanner sc = new Scanner(new File(this.pathMejniki));

            while (sc.hasNextLine()) {
                String[] items = sc.nextLine().split(" "); // Razdelimo prebrano vrstico

                double lat = Double.parseDouble(items[1]);
                double lon = Double.parseDouble(items[2]);

                if (lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180) {
                    if (!this.mejniki.containsKey(items[0])) { // Če mejnik ne obstaja
                        this.mejniki.put(items[0], new Mejnik(items[0], Double.parseDouble(items[1]), Double.parseDouble(items[2])));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readParcele() {
        try {
            Scanner sc = new Scanner(new File(this.pathParcele));

            while (sc.hasNextLine()) {
                String [] items = sc.nextLine().split(" ");

                if (items[0].equals("STAVBA")) {
                    Mejnik [] tmpMejniki;

                    // Ustvarimo seznam 4 mejnikov
                    tmpMejniki = new Mejnik[]{this.mejniki.get(items[3]), this.mejniki.get(items[4]),
                                                this.mejniki.get(items[5]), this.mejniki.get(items[6])};

                    Parcela tmp = new StavbnaParcela(tmpMejniki, // Mejniki
                                                     items[2], // St. parcele
                                                     items[1], // Katastrska obcina
                                                     Double.parseDouble(items[7])); // Povrsina stavbe

                    if (!parcelaExists(tmp)) {
                        // Parcelam dodamo novo parcelo
                        this.parcele.add(tmp);
                    }
                }
                else {
                    Mejnik [] tmpMejniki;
                    // Ustvarimo seznam 4 mejnikov
                    tmpMejniki = new Mejnik[]{this.mejniki.get(items[3]), this.mejniki.get(items[4]),
                                                this.mejniki.get(items[5]), this.mejniki.get(items[6])};

                    Parcela tmp = new KmetijskaParcela(tmpMejniki, // Mejniki
                                                       items[2], // St. parcele
                                                       items[1], // Katastrska obcina
                                                       items[0]); // Namemba

                    if (!parcelaExists(tmp)) {
                        // Parcelam dodamo novo parcelo
                        this.parcele.add(tmp);
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printMejniki() {
        ArrayList<Mejnik> mSorted = new ArrayList<>(this.mejniki.values());

        Collections.sort(mSorted);

        System.out.println("Kataster zajema naslednje mejnike:");
        for (Mejnik m : mSorted) {
            System.out.println(m);
        }
    }

    public void printParcele() {
        System.out.println("Kataster zajema naslednje parcele:");
        for (Parcela p : this.parcele) {
            System.out.println(p);
        }
    }


    // Standalone methods
    public void printRazdalja(String m1, String m2) {
        Mejnik tmpM1 = null; // Naredimo prazne objekte
        Mejnik tmpM2 = null;

        if (this.mejniki.containsKey(m1)) { // Pogledamo, ce prvi obstaja
            tmpM1 = this.mejniki.get(m1);
        }
        if (this.mejniki.containsKey(m2)) { // Pogledamo, ce drugi obstaja
            tmpM2 = this.mejniki.get(m2);
        }

        if (tmpM1 == null || tmpM2 == null) { // Ce kateri ni nastavljen
            System.out.println("NAPAKA: mejnika ni v katastru.");
            return; // Skocimo iz metode -> preprecimo kasnejsi izpis
        }

        System.out.printf("Razdalja med %s in %s je %.1f metrov.", m1, m2, Mejnik.razdalja(tmpM1, tmpM2));
    }

    public void printSmallest() {
        Parcela smallest   = this.parcele.get(0); // Prva parcela kot najmanjsa

        for (Parcela p : this.parcele) {
            if (p.obsegParcele() < smallest.obsegParcele()) {
                smallest = p;
            }
        }

        System.out.printf("Parcela z najkrajso mejo (obseg %.2f m; povrsina %.2f m2) je:\n", smallest.obsegParcele(),
                                                                                             smallest.povrsinaParcele());

        System.out.printf("   %s", smallest);
    }

    public void findLargestNeighbor(String katastrskaObcina, String parcelnaSt) {
        if (parcelaExists(katastrskaObcina, parcelnaSt)) { // Ce iskana parcela obstaja
            ArrayList<Parcela> sosedi = new ArrayList<>(); // Seznam sosedov

            // Pridobimo iskano parcelo
            Parcela current = this.getParcela(katastrskaObcina, parcelnaSt);

            // Poiscemo sosede
            for (Parcela p : this.parcele) {
                if (p != current) {
                    if(current.jeSosednja(p)) {
                        sosedi.add(p);
                    }
                }
            }

            // Nima sosedov
            if(sosedi.isEmpty()) {
                System.out.println("NAPAKA: parcela nima sosednjih parcel.");
            }
            else {
                Parcela maxParc = sosedi.get(0);

                for (Parcela p : sosedi) {
                    if (p.povrsinaParcele() > maxParc.povrsinaParcele()) {
                        maxParc = p;
                    }
                }
                System.out.printf("Najvecja sosednja parcela (povrsine %.2f m2) je:\n", maxParc.povrsinaParcele());
                System.out.printf("   %s\n", maxParc);
            }
        }
        else {
            System.out.println("NAPAKA: podane parcele ni v katastru.");
        }
    }


    // Utility methods
    boolean parcelaExists(Parcela p) {
        // Preverimo ce parcela obstaja, podatke vzamemo iz parcele p
        for (Parcela tmp : this.parcele) {
            if (tmp.getParcelnaSt().equals(p.getParcelnaSt())) {
                if (tmp.getKatastrskaObcina().equals(p.getKatastrskaObcina())) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean parcelaExists(String KO, String ps) {
        // Preoblozitev metode, podatke dobimo kot stringe
        for (Parcela tmp : this.parcele) {
            if (tmp.getParcelnaSt().equals(ps)) {
                if (tmp.getKatastrskaObcina().equals(KO)) {
                    return true;
                }
            }
        }
        return false;
    }

    String [] checkItems(String [] items) {
        String [] newItems = items;

        if (Integer.parseInt(newItems[2]) > 60) {
            newItems[2] = Integer.toString(Integer.parseInt(newItems[2]) - 60);
            newItems[1] = Integer.toString(Integer.parseInt(newItems[1]) + 1);
        }

        if (Integer.parseInt(newItems[2]) > 60) {
            newItems[1] = Integer.toString(Integer.parseInt(newItems[1]) - 60);
            newItems[0] = Integer.toString(Integer.parseInt(newItems[0]) + 1);
        }
        return newItems;
    }


    // File accessors
    public void readBinaryKataster() {
        // Metoda za branje podatkov mejnikov iz binarne datoteke

        try {
            // Odpremo tok za branje podatkov
            FileInputStream fis = new FileInputStream(new File(this.pathMejniki));

            int count = 0; // Stevec prebranih podatkov

            String name = "";

            // Tabeli, v katere shranimo podatke o sirini/dolzini
            String [] latItems = new String[5];
            String [] lonItems = new String[5];

            // Dokler je v datoteki se kaksen byte
            while (fis.available() > 0) {
                int read = fis.read(); // Beremo byte
                count++; // Povecamo stevec prebranih bytov

                // Prvih 10 bytov hrani ime
                if (count <= 10) {
                    name += (char) read;
                }
                // Naslednjih 5 bytov hrani podatke o sirini
                else if (count <= 15) {

                    if(count < 15) {
                        latItems[count - 11] = Integer.toString(read);
                    }
                    // Zadnji prebrani podatek o dolzini je crka, ki doloca smer
                    else {
                        latItems[count - 11] = Character.toString((char) read);
                    }
                }
                // Zadnjih 5 bytov hrani podatke o dolzini
                else if (count <= 20) {
                    if(count < 20) {
                        lonItems[count - 16] = Integer.toString(read);
                    }
                    // Zadnji prebrani podatek o dolzini je crka, ki doloca smer
                    else {
                        lonItems[count - 16] = Character.toString((char) read);
                    }
                }
                // Preverimo, ali je prebranih vseh 20 bytov (vsi podatki o mejniku)
                if (count >= 20) {
                    count = 0; // Resetiramo stevec

                    latItems = checkItems(latItems);
                    lonItems = checkItems(lonItems);

                    // Pravilno formatiramo sirino/dolzino
                    String lat = String.format("%d*%02d'%02d.%d\"%s", Integer.parseInt(latItems[0]),
                                                                      Integer.parseInt(latItems[1]),
                                                                      Integer.parseInt(latItems[2]),
                                                                      Character.getNumericValue(latItems[3].charAt(0)),
                                                                      latItems[4]);

                    String lon = String.format("%d*%02d'%02d.%d\"%s", Integer.parseInt(lonItems[0]),
                                                                      Integer.parseInt(lonItems[1]),
                                                                      Integer.parseInt(lonItems[2]),
                                                                      Character.getNumericValue(lonItems[3].charAt(0)),
                                                                      lonItems[4]);

                    // Ce mejnik s tem imenom ze obstaja (odstranimo odvecne presledke)
                    if(!this.mejniki.containsKey(name.replace(" ", ""))) {
                        this.mejniki.put(name.replace(" ", ""),
                                new Mejnik(name.replace(" ", ""), lat, lon));
                    }
                    name = "";
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}



abstract class Parcela {
    Mejnik[] mejniki        = {};
    String parcelnaSt       = "";
    String katastrskaObcina = "";


    // Constructors
    public Parcela(Mejnik[] mejniki, String parcelnaSt, String katastrskaObcina) {
        this.mejniki          = mejniki;
        this.parcelnaSt       = parcelnaSt;
        this.katastrskaObcina = katastrskaObcina;
    }


    // Methods of class Object
    @Override
    public String toString() {
        return String.format("KO: %s, stevilka: %s, vrednost: %.2f EUR [%s, %s, %s, %s]",
                this.katastrskaObcina, this.parcelnaSt, cenaParcele(),
                this.mejniki[0].getName(),
                this.mejniki[1].getName(),
                this.mejniki[2].getName(),
                this.mejniki[3].getName());
    }

    @Override
    public boolean equals(Object p) {
        if (p == null) {
            return false;
        }

        return this.getKatastrskaObcina().equals(((Parcela)p).getKatastrskaObcina())
                &&
                this.getParcelnaSt().equals(((Parcela)p).getParcelnaSt());
    }


    // Getters & setters
    public String getKatastrskaObcina() {
        return this.katastrskaObcina;
    }

    public String getParcelnaSt() {
        return this.parcelnaSt;
    }

    public Mejnik[] getMejniki() {
        return this.mejniki;
    }


    // Main methods
    abstract double cenaParcele();

    double obsegParcele() {
        double diameter = 0;

        // Obsegu pristejemo razdaljo med mejnikoma
        for (int i = 1; i < this.mejniki.length; i++) {
            diameter += Mejnik.razdalja(this.mejniki[i], this.mejniki[i - 1]);
        }
        // Pristejemo se razdaljo med prvim in zadnjim mejnikom
        diameter += Mejnik.razdalja(this.mejniki[this.mejniki.length - 1], this.mejniki[0]);

        return diameter;
    }

    double povrsinaParcele() {
        // Izracunamo povrsine dveh trikotnikov
        double triangle1 = triangleArea(this.mejniki[0], this.mejniki[1], this.mejniki[2]);
        double triangle2 = triangleArea(this.mejniki[2], this.mejniki[3], this.mejniki[0]);

        // Dva trikotnika = strikotnik
        return triangle1 + triangle2;
    }

    boolean jeSosednja(Parcela p) {
        // Ustvarimo pomozno tabelo mejnikov podane parcele
        Mejnik[] tmpMejniki = p.getMejniki();

        // Stevec enakih mejnikov
        int count = 0;
        for (int i = 0; i < tmpMejniki.length; i++) {
            for (int j = 0; j < this.mejniki.length; j++) {
                if(this.mejniki[j] == tmpMejniki[i]) {
                    count++;
                }
            }
        }
        // 2 ali vec sosednjih mejnikov = parceli sta sosednji
        if (count >= 2) {
            return true;
        }
        return false;
    }


    // Utility methods
    double triangleArea(Mejnik m1, Mejnik m2, Mejnik m3) {
        double area = 0;

        // Izracunamo vse stranice trikotnika
        double a = Mejnik.razdalja(m1, m2);
        double b = Mejnik.razdalja(m2, m3);
        double c = Mejnik.razdalja(m3, m1);

        double p = ( a + b + c ) / 2;

        area = Math.sqrt(p * (p - a) * (p - b) * (p-c) );

        return area;
    }
}




class KmetijskaParcela extends Parcela {
    String rabaParcele = "";


    // Constructors
    public KmetijskaParcela(Mejnik[] mejniki, String parcelnaSt, String katastrskaObcina, String rabaParcele) {
        super(mejniki, parcelnaSt, katastrskaObcina);
        this.rabaParcele = rabaParcele;
    }


    // Inherited methods
    @Override
    public double cenaParcele() {

        double pricePerSqMeter = 0;

        // Dolocimo ceno na kvadratni meter
        if (this.rabaParcele.equals("NJIVA")) {
            pricePerSqMeter = 3.12;
        }
        else if (this.rabaParcele.equals("TRAVNIK")) {
            pricePerSqMeter = 1.42;
        }
        else if (this.rabaParcele.equals("PASNIK")) {
            pricePerSqMeter = 1.69;
        }
        else if (this.rabaParcele.equals("GOZD")) {
            pricePerSqMeter = 0.57;
        }
        else if (this.rabaParcele.equals("DRUGO")) {
            pricePerSqMeter = 0.15;
        }

        // Izracunamo ceno
        return povrsinaParcele() * pricePerSqMeter;
    }
}




class StavbnaParcela extends Parcela {
    double povrsinaStavbe   = 0;
    final double cenaStavbe = 523.45;
    final double cenaVrta   = 117.65;


    // Constructors
    public StavbnaParcela(Mejnik[] mejniki, String parcelnaSt, String katastrskaObcina, double povrsinaStavbe) {
        super(mejniki, parcelnaSt, katastrskaObcina);
        this.povrsinaStavbe = povrsinaStavbe;
    }


    // Inherited methods
    @Override
    double cenaParcele() {
        return (this.cenaStavbe * this.povrsinaStavbe) + (this.cenaVrta * (povrsinaParcele() - povrsinaStavbe) );
    }
}