import java.util.Scanner;
import java.io.File;

public class DN09 {
    public static void main(String[] args) {
        if (args.length == 2) {
            String [] planetNames = args[1].split("\\+");

            Planet [] planets = readPlanetsData(args[0]);

            double area = planetAreas(planets, planetNames);

            System.out.printf("Povrsina planetov \"%s\" je %d milijonov km2", args[1], (int) (area / 1000000));
        }
        else {
            System.out.println("Illegal number of arguments!");
        }
    }

    public static Planet[] readPlanetsData(String path) {
        Planet[] planets = new Planet[8];

        try {
            Scanner sc = new Scanner(new File(path));

            int count = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String [] data = line.split(":");

                Planet planet = new Planet(data[0], Integer.parseInt(data[1]));
                planets[count] = planet;

                count++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return planets;
    }

    public static double planetAreas(Planet[] planets, String[] planetNames) {
        double area = 0;

        for (int i = 0; i < planetNames.length; i++) {
            for (int j = 0; j < planets.length; j++) {
                if (planets[j].getIme().toLowerCase().equals(planetNames[i].toLowerCase())) {
                    area += planets[j].povrsina();
                }
            }
        }
        return area;
    }
}

class Planet {
    private String ime;
    private int radij;

    Planet(String ime, int radij) {
        this.ime = ime;
        this.radij = radij;
    }


    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getRadij() {
        return radij;
    }

    public void setRadij(int radij) {
        this.radij = radij;
    }


    double povrsina() {
        return ( 4 * Math.PI * radij * radij );
    }
}
