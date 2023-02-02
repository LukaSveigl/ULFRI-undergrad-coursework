public class DN04 {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments!");
            System.exit(0);
        }
        String message = "";
        int current = 0;

        for (int c = 0; c < args[0].length() / 8; c++) {
            current = Integer.parseInt(args[0].substring(c * 8, (c + 1) * 8), 2);
            message += (char) current;
        }

        System.out.println(message);
    }
}
