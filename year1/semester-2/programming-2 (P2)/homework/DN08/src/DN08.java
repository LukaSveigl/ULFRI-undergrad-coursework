import java.util.Locale;
import java.util.Scanner;
import java.io.File;

public class DN08 {

    private static final String[] tipiParcel = {"TRAVNATA POVRSINA", "GOZDNA POVRSINA", "OBDELOVALNA POVRSINA", "BIVALNO POSLOPJE", "INDUSTRIJSKO POSLOPJE"};
    private static final double[] vrednostiTipovParcel = {500.0, 750.0, 1000.0, 10000.0, 50000.0};

    private static final char[] odtenki = {' ', '.', ':', '-', '=', '+', '*', '#', '%', '@'};

    public static void main(String[] args) {
        Locale.setDefault(Locale.ITALY);

        if (args.length > 1) {
            if (args[0].equals("analiza")) {
                int[][] teren = preberiTeren(args[1]);
                int[] stetje = prestejVisine(teren);

                izrisTerena(teren);

                for (int i = 0; i < stetje.length; i++) {
                    System.out.printf("Visina %d: %dx\n", i, stetje[i]);
                }

                System.out.printf("Povprecna visina: %.2f\n", povprecnaVisina(teren));

            }
            else if (args[0].equals("izrisi_poplavo")) {
                if (args[1].equals("visinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = visinskaPoplava(teren, Double.parseDouble(args[3]));
                    izrisiPoplavo(teren, poplava);

                }
                else if(args[1].equals("kolicinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = kolicinskaPoplava(teren, Double.parseDouble(args[3]));
                    izrisiPoplavo(teren, poplava);
                }
            }
            else if (args[0].equals("porocilo_skode")) {
                if (args[1].equals("visinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = visinskaPoplava(teren, Double.parseDouble(args[4]));
                    int [][] tipParcel = preberiTipParcel(args[3]);

                    porociloSkode(teren, tipParcel, poplava);

                }
                else if(args[1].equals("kolicinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = kolicinskaPoplava(teren, Double.parseDouble(args[4]));
                    int [][] tipParcel = preberiTipParcel(args[3]);

                    porociloSkode(teren, tipParcel, poplava);
                }
            }
            else if (args[0].equals("nacrt_pobega")) {
                if (args[1].equals("visinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = visinskaPoplava(teren, Double.parseDouble(args[4]));
                    int [][] tipParcel = preberiTipParcel(args[3]);

                    nacrtPobega(teren, tipParcel, poplava);

                }
                else if(args[1].equals("kolicinska")) {
                    int [][] teren = preberiTeren(args[2]);
                    boolean [][] poplava = kolicinskaPoplava(teren, Double.parseDouble(args[4]));
                    int [][] tipParcel = preberiTipParcel(args[3]);

                    nacrtPobega(teren, tipParcel, poplava);
                }
            }
        }
        else {
            System.out.println("Illegal number of arguments!");
        }
    }

    // Naloga 1:

    public static int[][] preberiTeren(String datoteka) {
        int x = 0;
        int y = 0;

        int [][]teren = {};

        try {
            File file = new File(datoteka);
            Scanner sc = new Scanner(file);

            x = sc.nextInt();
            y = sc.nextInt();

            teren = new int[y][x];

            int countX = 0;
            int countY = 0;

            while (sc.hasNextInt()) {
                int read = sc.nextInt();

                teren[countY][countX] = read;

                countX++;
                if (countX >= x) {
                    countY++;
                    countX = 0;
                }
            }

            sc.close();
        }
        catch (Exception e) {
            System.out.println("Couldn't open file!");
        }

        return teren;
    }

    public static void izrisTerena(int [][] teren) {
        for (int i = 0; i < teren.length; i++) {
            for (int j = 0; j < teren[i].length; j++) {
                System.out.print(odtenki[teren[i][j]]);
            }
            System.out.println();
        }
    }

    public static double povprecnaVisina(int [][] teren) {
        double avgHeight = 0;

        for (int i = 0; i < teren.length; i++) {
            for (int j = 0; j < teren.length; j++) {
                avgHeight += teren[i][j];
            }
        }

        return avgHeight/(teren.length * teren[0].length);
    }

    public static int[] prestejVisine(int [][] teren) {
        int [] stetje = new int [10];

        for (int f = 0; f < stetje.length; f++) {
            stetje[f] = 0;
        }

        for (int i = 0; i < teren.length; i++) {
            for (int j = 0; j < teren[i].length; j++) {
                stetje[teren[i][j]]++;
            }
        }

        return stetje;
    }

    // Naloga 2:

    public static int[][] preberiTipParcel(String datoteka) {
        int x = 0;
        int y = 0;

        int [][]tipParcele = {};

        try {
            File file1 = new File(datoteka);
            Scanner sc = new Scanner(file1);

            x = sc.nextInt();
            y = sc.nextInt();

            tipParcele = new int[x][y];

            int countX = 0;
            int countY = -1;

            while (sc.hasNextLine()) {
                String read = sc.nextLine();
                //System.out.print("Y: " + countY + ", ");
                for (int i = 0; i < read.length(); i++) {
                    char readC = read.charAt(i);
                    //System.out.print("I: " + i + ", ");
                    for (int j = 0; j < tipiParcel.length; j++) {
                        if (readC == tipiParcel[j].charAt(0)) {
                            tipParcele[countY][i] = j;
                        }
                    }
                }

                countY++;
            }
            sc.close();
        }
        catch (Exception e) {
            System.out.println("Couldn't open file - Tipi parcel!");
            e.printStackTrace();
        }

        return tipParcele;
    }

    public static boolean[][] visinskaPoplava(int [][] teren, double visinaPoplave) {
        boolean [][]poplava = new boolean[teren.length][teren[0].length];

        for (int i = 0; i < poplava.length; i++) {
            for (int j = 0; j < poplava[i].length; j++) {
                if (teren[i][j] < visinaPoplave) {
                    poplava[i][j] = true;
                }
                else {
                    poplava[i][j] = false;
                }
            }
        }

        return poplava;
    }

    public static void izrisiPoplavo(int [][] teren, boolean [][] poplava) {
        for (int i = 0; i < poplava.length; i++) {
            for (int j = 0; j < poplava[i].length; j++) {
                if(poplava[i][j]) {
                    System.out.print("~");
                }
                else {
                    System.out.print(odtenki[teren[i][j]]);
                }
            }
            System.out.println();
        }
    }

    public static void porociloSkode(int [][] teren, int [][] tipParcele, boolean [][] poplava) {
        double [] skodaNaTip = new double[tipiParcel.length];

        int elements = 0;

        for (int i = 0; i < tipParcele.length; i++) {
            for (int j = 0; j < tipParcele[i].length; j++) {
                if (poplava[i][j]) {
                    elements++;
                    skodaNaTip[tipParcele[i][j]] += vrednostiTipovParcel[tipParcele[i][j]];
                }
            }
        }

        double sum = 0;

        System.out.printf("%21s %10s %20s\n", "Tip parcele", "Stevilo", "Ocenjena skoda");
        System.out.printf("-----------------------------------------------------\n");
        for (int i = 0; i < skodaNaTip.length; i++) {
            System.out.printf("%21s %10d %,16.2f EUR\n", tipiParcel[i], (int)(skodaNaTip[i]/vrednostiTipovParcel[i]), skodaNaTip[i]);
            sum += skodaNaTip[i];
        }
        System.out.printf("-----------------------------------------------------\n");
        System.out.printf("%21s %10d %,16.2f EUR\n", "SKUPAJ", elements, sum);
    }

    // Naloga 3:

    public static boolean[][] kolicinskaPoplava(int [][] teren, double kolicina) {
        int[] visine = prestejVisine(teren);

        double tmpkol = kolicina;
        double tmpvis = 0;

        double visina = 0;

        for (int k = 0; k < visine.length; k++) {
            for (int z = 0; z <= k; z++) {
                tmpvis += visine[z];
            }
            if (tmpkol - tmpvis > 0) {
                tmpkol -= tmpvis;
                tmpvis = 0;
                visina++;
            }
            else {
                visina += tmpkol / tmpvis;
                break;
            }
        }

        return visinskaPoplava(teren, visina);
    }

    // Naloga 4:

    public static void nacrtPobega(int [][] teren, int [][] tipParcele, boolean [][] poplava) {
        // Varne hise:

        int varne = 0;
        int resljive = 0;
        int neresljive = 0;

        for (int i = 0; i < tipParcele.length; i++) {
            for (int j = 0; j < tipParcele[i].length; j++) {
                if (tipParcele[i][j] == 3) {
                    if (!poplava[i][j]) {
                        varne++;
                    }
                    else {
                        if (lahkoPobegne(i, j, teren, poplava)) {
                            resljive++;
                        }
                        else {
                            neresljive++;
                        }
                    }
                }
            }
        }

        System.out.printf("Varne hise: %d\n", varne);
        System.out.printf("Lahko pobegnejo: %d\n", resljive);
        System.out.printf("Potrebujejo pomoc: %d\n", neresljive);
    }

    public static boolean lahkoPobegne(int x, int y, int [][] teren, boolean [][] poplava) {
        boolean nextPossible = true;

        while (nextPossible) {
            // Find max height:
            nextPossible = false;
            int max = teren[x][y];

            if (y - 1 > -1) {
                if (teren[x][y - 1] > max) {
                    max = teren[x][y - 1];
                    y = y - 1;
                    nextPossible = true;
                }
            }
            if (x + 1 < teren.length) {
                if (teren[x + 1][y] > max) {
                    max = teren[x + 1][y];
                    x = x + 1;
                    nextPossible = true;
                }
            }
            if (y + 1 < teren[0].length) {
                if (teren[x][y + 1] > max) {
                    max = teren[x][y + 1];
                    y = y + 1;
                    nextPossible = true;
                }
            }
            if (x - 1 > -1) {
                if (teren[x - 1][y] > max) {
                    max = teren[x - 1][y];
                    x = x - 1;
                    nextPossible = true;
                }
            }
        }

        return !poplava[x][y];
    }
}