import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

interface Lik {
    public double ploscina();
}

class Krog implements Lik {
    double r = 0;

    public Krog(double r) {
        this.r = r;
    }

    @Override
    public double ploscina() {
        return Math.PI * Math.pow(this.r, 2);
    }
}

class Kvadrat implements Lik {
    double a = 0;

    public Kvadrat(double a) {
        this.a = a;
    }

    @Override
    public double ploscina() {
        return Math.pow(this.a, 2);
    }
}

class Pravokotnik implements Lik {
    double a = 0;
    double b = 0;

    public Pravokotnik(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double ploscina() {
        return (this.a * this.b);
    }
}

public class DN10 {
    static ArrayList<Lik> liki = new ArrayList<Lik>();

    public static void main(String[] args) {
        if (args.length == 1) {
            preberi(args[0]);
            System.out.printf("%.2f", ploscina());
        }
        else {
            System.out.println("Illegal number of arguments!");
        }
    }

    static void preberi(String path) {
        try {
            Scanner sc = new Scanner(new File(path));

            while(sc.hasNextLine()) {
                String [] items = sc.nextLine().split(":");

                if (items[0].equals("krog")) {
                    liki.add(new Krog(Double.parseDouble(items[1])));
                }
                else if(items[0].equals("kvadrat")) {
                    liki.add(new Kvadrat(Double.parseDouble(items[1])));
                }
                else {
                    liki.add(new Pravokotnik(Double.parseDouble(items[1]),Double.parseDouble(items[2])));
                }
            }

            sc.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    static double ploscina() {
        double result = 0;

        for (Lik shape : liki) {
            result += shape.ploscina();
        }

        return result;
    }
}
