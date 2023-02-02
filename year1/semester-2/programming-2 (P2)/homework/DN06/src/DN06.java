public class DN06 {
    public static void main(String[] args) {

        int [][] sudoku = createTable( args );
        int [] indices = findZero( sudoku );
        System.out.println(getNumber( sudoku, indices[0], indices[1] ));
    }

    static int[][] createTable(String[] args) {

        int [][] sudoku = new int[(int)Math.sqrt(args.length)][(int)Math.sqrt(args.length)];

        if (Math.sqrt(args.length) == (int)Math.sqrt(args.length)) {

            int countLine = 0;
            int countColl = 0;

            for (int i = 0; i < args.length; i++) {
                sudoku[countLine][countColl] = Integer.parseInt(args[i]);

                countColl++;
                if (countColl >= (int)Math.sqrt(args.length)) {
                    countColl = 0;
                    countLine++;
                }
            }

            return sudoku;
        }
        else {
            System.out.println("Napacno stevilo argumentov.");
            System.exit(0);
        }

        return sudoku;
    }

    static int[] findZero (int [][] sudoku) {
        int [] indices =  {0, 0};

        for (int i = 0; i < sudoku.length; i++) {
            for(int j = 0; j < sudoku.length; j++) {
                if (sudoku[i][j] == 0) {
                    indices[0] = i;
                    indices[1] = j;
                }
            }
        }

        return indices;
    }

    static int getNumber(int [][] sudoku, int x, int y) {
        int [] usedNumbers = new int[sudoku.length + sudoku.length - 1];

        int count = 0;

        for (int line = 0; line < sudoku.length; line++) {
            if (sudoku[x][line] != 0) {
                usedNumbers[count] = sudoku[x][line];
                count++;
            }

            if (sudoku[line][y] != 0) {
                usedNumbers[count] = sudoku[line][y];
                count++;
            }
        }

        int number = 1;

        while (true) {
            boolean broken = false;
            for (int k = 0; k < usedNumbers.length; k++) {
                if (number == usedNumbers[k]) {
                    number++;
                    if (number > sudoku.length) {
                        System.out.println("Naloge se ne da resiti.");
                        System.exit(0);
                    }
                    broken = true;
                    break;
                }
            }
            if (!broken) {
                return number;
            }
        }

    }
}
