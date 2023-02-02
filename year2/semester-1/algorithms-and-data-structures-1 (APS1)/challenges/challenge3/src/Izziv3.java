import java.util.Arrays;

public class Izziv3 {
    public static void main(String[] args) {
        BinTree b = new BinTree(Integer.parseInt(args[0]));
        b.initDraw();
        //b.drawLevelorder();
        //b.drawPreorder(0);
        //b.drawInorder(0);
        b.drawPostorder(0);
    }
}



class BinTree {
    int size = 0;
    String [] elements = null;
    int [] x = null;
    int [] y = null;

    int maxX = 0;
    int maxY = 0;

    String [] inOrder = null;

    int inOrderCount = 0;

    BinTree(int size) {
        this.size = size;

        this.elements = new String[this.size];

        this.x = new int[this.size];
        this.y = new int[this.size];

        this.inOrder = new String[this.size];

        this.init();

        this.resolveX();
        this.resolveY();

        this.maxX = this.size - 1;
        this.maxY = this.y[this.y.length - 1];
    }

    void init() {
        for (int i = 0; i < this.size; i++) {
            this.elements[i] = Integer.toString(i);
        }
    }


    void findInOrder(int index) {
        if (2 * index + 1 < this.size) {
            findInOrder(2 * index + 1);
        }

        this.inOrder[this.inOrderCount] = this.elements[index];
        this.inOrderCount++;

        if (2 * index + 2 < this.size) {
            findInOrder(2 * index + 2);
        }
    }


    int floorLog2(int x) {
        return (int) Math.floor(Math.log10(x) / Math.log10(2));
    }


    void resolveX() {
        this.findInOrder(0);

        for (int i = 0; i < this.size; i++) {
            this.x[i] = Arrays.asList(this.inOrder).indexOf(this.elements[i]);
        }
    }

    void resolveY() {
        for (int i = 0; i < this.size; i++) {
            int item = Integer.parseInt(this.elements[i]);
            this.y[i] = floorLog2(item + 1);
        }
    }

    void drawNode(int index) {
        int xPos = this.x[index];
        int yPos = this.y[index];

        double cellWidth = 75;
        double xCellPosition = xPos * cellWidth + (cellWidth / 2);

        double cellHeight = 100;
        double yCellPosition = (this.size * 50) - (yPos * cellHeight) - (cellHeight);


        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.filledEllipse(xCellPosition, yCellPosition, cellWidth / 2, cellHeight / 4);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(xCellPosition, yCellPosition, this.elements[index]);

        StdDraw.show(500);
    }

    void drawConnection(int index) {
        int prev = (index - 1) / 2;

        int xPosCurr = this.x[index];
        int yPosCurr =  this.y[index];

        int xPosPrev = this.x[prev];
        int yPosPrev = this.y[prev];

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        double cellWidth = 75;
        double cellHeight = 100;


        double xCellPositionCurr = xPosCurr * cellWidth + (cellWidth / 2);
        double yCellPositionCurr = (this.size * 50) - (yPosCurr * cellHeight) - (cellHeight);

        double xCellPositionPrev = xPosPrev * cellWidth + (cellWidth / 2);
        double yCellPositionPrev = (this.size * 50) - (yPosPrev * cellHeight) - (cellHeight);

        StdDraw.line(xCellPositionCurr, yCellPositionCurr, xCellPositionPrev, yCellPositionPrev);

        StdDraw.show(500);
    }

    void initDraw() {
        StdDraw.setCanvasSize(this.size * 75, this.size * 50);
        StdDraw.setXscale(0, this.size * 75);
        StdDraw.setYscale(0, this.size * 50);
        StdDraw.clear(StdDraw.WHITE);
    }

    void drawLevelorder() {
        for (int i = 0; i < this.size; i++) {
            drawConnection(i);
        }
        for (int j = 0; j < this.size; j++) {
            drawNode(j);
        }
    }

    void drawPreorder(int index) {
        drawConnection(index);
        drawNode(index);

        if (2 * index + 1 < this.size) {
            drawPreorder(2 * index + 1);
        }

        if (2 * index + 2 < this.size) {
            drawPreorder(2 * index + 2);
        }

    }

    void drawInorder(int index) {
        if (2 * index + 1 < this.size) {
            drawInorder(2 * index + 1);
        }

        drawConnection(index);
        drawNode(index);

        if (2 * index + 2 < this.size) {
            drawInorder(2 * index + 2);
        }
    }

    void drawPostorder(int index) {
        if (2 * index + 1 < this.size) {
            drawPostorder(2 * index + 1);
        }

        if (2 * index + 2 < this.size) {
            drawPostorder(2 * index + 2);
        }

        drawConnection(index);
        drawNode(index);
    }

}