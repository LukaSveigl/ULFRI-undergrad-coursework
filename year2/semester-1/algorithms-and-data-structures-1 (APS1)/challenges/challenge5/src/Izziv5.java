import java.util.Random;
import java.util.Scanner;

public class Izziv5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String redo = "";

        System.out.print("Enter size of array: ");
        int size = sc.nextInt();

        Oseba [] osebe = new Oseba[size];

        for (int i = 0; i < osebe.length; i++) {
            osebe[i] = new Oseba();
        }

        do {
            redo = "";

            Oseba [] osebeTmp = new Oseba[size];

            for (int i = 0; i < osebe.length; i++) {
                osebeTmp[i] = new Oseba(osebe[i].getIme(), osebe[i].getPriimek(), osebe[i].getLetoR());
            }

            System.out.println();

            System.out.println("Current array is: ");
            for (int i = 0; i < osebeTmp.length; i++) {
                System.out.print(osebeTmp[i]  + " ");
            }

            System.out.print("\n");


            String atr = "";
            String direction = "";

            do  {
                System.out.print("Enter attribute name [valid names are: ime, priimek, letoR]: ");
                atr = sc.nextLine().trim();
                System.out.println();
            } while(!atr.equals("ime") && !atr.equals("priimek") && !atr.equals("letoR"));

            Oseba.setAtr(atr);

            do {
                System.out.print("Enter direction [valid names are: up, down]: ");
                direction = sc.nextLine().trim();
                System.out.println();
            } while(!direction.equals("up") && !direction.equals("down"));

            bubblesort(osebeTmp, direction);

            System.out.print("Redo [enter y to redo]: ");
            redo = sc.nextLine().trim();

            atr = "";
            direction = "";

        } while(redo.equals("y"));
    }

    public static void bubblesort(Oseba[] arr, String direction) {
        int swaps = 0;

        int n = arr.length - 1;

        int last_swap = 0;

        do {
            swaps = 0;
            for (int i = 0; i < n; i++) {

                if (direction.equals("up")) {
                    if (arr[i].compareTo(arr[i + 1]) > 0) {
                        Oseba o = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = o;

                        last_swap = i + 1;

                        swaps++;
                    }
                } else {
                    if (arr[i].compareTo(arr[i + 1]) < 0) {
                        Oseba o = arr[i];
                        arr[i] = arr[i + 1];
                        arr[i + 1] = o;

                        last_swap = i + 1;

                        swaps++;
                    }
                }
            }

            for (int j = 0; j < last_swap; j++) {
                System.out.print(arr[j]);
            }
            System.out.print("| ");
            for (int j = last_swap; j < arr.length; j++) {
                System.out.print(arr[j]);
            }
            System.out.println();

            n = last_swap;

            if (n <= 1) {
                break;
            }

        } while (swaps != 0);


        System.out.println();

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }
}

class Oseba implements Comparable {
    final private String priimek;
    final private String ime;
    final private int letoR;

    final private String[] names = {"James", "Robert", "John", "Michael", "William",
                                    "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth"};
    final private String[] surnames = {"Smith", "Jones", "Taylor", "Brown", "Williams", "Wilson",
                                       "Davies", "Patel", "Thompson", "Evans", "Green", "Clarke"};
    final private int bottomYear = 1960;
    final private int topYear = 2002;

    static int atr = 0;

    Oseba() {
        Random rand  = new Random();

        this.ime = this.names[rand.nextInt(this.names.length)];
        this.priimek = this.surnames[rand.nextInt(this.surnames.length)];

        this.letoR = rand.nextInt(this.topYear - this.bottomYear) + this.bottomYear;
    }

    Oseba(String name, String surname, int bYear) {
        this.ime = name;
        this.priimek = surname;
        this.letoR = bYear;
    }

    public String toString() {
        return "[" + this.ime + ", " + this.priimek + ", " + this.letoR + "] ";
    }

    @Override
    public int compareTo(Object o) {
        switch (atr) {
            case 0:
                return this.ime.compareTo(((Oseba)o).ime);
            case 1:
                return this.priimek.compareTo(((Oseba)o).priimek);
            case 2:
                if (this.letoR == ((Oseba)o).letoR) {
                    return 0;
                }
                else if (this.letoR > ((Oseba)o).letoR) {
                    return 1;
                }
                else {
                    return -1;
                }
            default:
                return 0;
        }
    }

    public static void setAtr(String attribute) {
        switch (attribute) {
            case "ime" -> atr = 0;
            case "priimek" -> atr = 1;
            case "letoR" -> atr = 2;
            default -> atr = 0;
        }
    }

    public String getIme() {
        return this.ime;
    }

    public String getPriimek() {
        return this.priimek;
    }

    public int getLetoR() {
        return this.letoR;
    }
}
