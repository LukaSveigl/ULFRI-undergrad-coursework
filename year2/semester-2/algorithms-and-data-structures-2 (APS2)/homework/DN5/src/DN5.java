import java.util.ArrayList;

public class DN5 {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        /*GRPH g = new GRPH(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 3, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(3, 2, 1);
        g.printShortestDistsFrom(0);*/

        GRPH g = new GRPH(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 3, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(1, 3, -1);
        g.addEdge(3, 2, 1);
        g.printShortestDistsFrom(0);

        System.out.println("\n\n");

        /*int cells[][] = new int[][] { { 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0 },
                { 0, 0, 0, 0, 0 } };

        LBR l = new LBR(cells);
        l.printPath(15, 1);*/

        int cells[][] = new int[][] { { 0, 0, 0, 1 },
                { 0, 0, 1, 0 },
                { 0, 1, 0, 0 },
                { 1, 0, 0, 0 } };

        LBR l = new LBR(cells);
        l.printPath(1, 16);
    }
}

/**
 * A class that represents a graph.
 */
class GRPH {
    /** The graph - each edge is represented by 3 values: from, to, cost. */
    private final ArrayList<Integer[]> graph;
    /** The number of vertices. */
    private final int n;
    /** The number of edges. */
    private int size;

    /**
     * Constructs a new graph with the given number of vertices.
     *
     * @param verticesCount The number of vertices.
     */
    public GRPH(int verticesCount) {
        this.graph = new ArrayList<>();
        this.n = verticesCount;
        this.size = 0;
    }

    /**
     * Adds an edge to the graph.
     *
     * @param from The index of the vertex from which the edge starts.
     * @param to The index of the vertex to which the edge goes.
     * @param cost The cost of the edge.
     */
    public void addEdge(int from, int to, int cost) {
        // Check if the edge is valid.
        if (from > 0 || from <= n || to > 0 || to <= n) {
            size++;
            graph.add(new Integer[]{from, to, cost});
        }
    }

    /**
     * Find the shortest path from the source vertex to all other vertices using the Bellman-Ford algorithm.
     *
     * @param from The source vertex.
     */
    public void printShortestDistsFrom(int from) {
        System.out.println("V .. Cena");

        // Initialize the distances array.
        int [] dist = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        // Set the distance of the source vertex to 0.
        dist[from] = 0;

        // Relax all edges.
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < size; j++) {
                if (dist[graph.get(j)[0]] != Integer.MAX_VALUE && dist[graph.get(j)[0]] + graph.get(j)[2] < dist[graph.get(j)[1]]) {
                    dist[graph.get(j)[1]] = dist[graph.get(j)[0]] + graph.get(j)[2];
                }
            }
        }


        // Display the distances.
        for (int i = 0; i < n; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println(i + " .. " + "None");
            }
            else {
                System.out.println(i + " .. " + dist[i]);
            }
        }
    }
}

/**
 * A class that represents a labyrinth.
 */
class LBR {
    /** The labyrinth. */
    private final int[][] labyrinth;

    /**
     * Constructs a new labyrinth with the given cells.
     *
     * @param cells The labyrinth cells.
     */
    LBR(int[][] cells) {
        this.labyrinth = cells;
    }

    /**
     * Prints the shortest path from the source to the destination using BFS.
     * @param from The source vertex.
     * @param to The destination vertex.
     */
    public void printPath(int from, int to) {
        // Adjust the source and destination vertices.
        from = from - 1;
        to = to - 1;

        // Calculate the x and y coordinates of the source and destination vertices.
        int fromX = from % labyrinth[0].length;
        int fromY = from / labyrinth[0].length;
        int toX = to % labyrinth[0].length;
        int toY = to / labyrinth[0].length;

        // Prepare the needed data structures.
        boolean[][] visited = new boolean[labyrinth.length][labyrinth[0].length];
        int [][] prev = new int[labyrinth.length][labyrinth[0].length];

        // Initialize the queue.
        ArrayList<Integer[]> queue = new ArrayList<>();
        queue.add(new Integer[]{fromX, fromY});

        ArrayList<Integer> path = new ArrayList<>();

        int currX = 0;
        int currY = 0;

        // Repeat the BFS until the queue is empty.
        while (!queue.isEmpty()) {
            Integer[] curr = queue.remove(0);
            currX = curr[0];
            currY = curr[1];

            // Check if the current vertex is the destination.
            if (currX == toX && currY == toY) {
                break;
            }

            // Check each next possible move for current vertex.
            if (currX - 1 >= 0 && labyrinth[currY][currX - 1] == 0 && !visited[currY][currX - 1]) {
                queue.add(new Integer[]{currX - 1, currY});
                visited[currY][currX - 1] = true;
                prev[currY][currX - 1] = currX + currY * labyrinth[0].length;
            }

            if (currX + 1 < labyrinth[0].length && labyrinth[currY][currX + 1] == 0 && !visited[currY][currX + 1]) {
                queue.add(new Integer[]{currX + 1, currY});
                visited[currY][currX + 1] = true;
                prev[currY][currX + 1] = currX + currY * labyrinth[0].length;
            }

            if (currY - 1 >= 0 && labyrinth[currY - 1][currX] == 0 && !visited[currY - 1][currX]) {
                queue.add(new Integer[]{currX, currY - 1});
                visited[currY - 1][currX] = true;
                prev[currY - 1][currX] = currX + currY * labyrinth[0].length;
            }

            if (currY + 1 < labyrinth.length && labyrinth[currY + 1][currX] == 0 && !visited[currY + 1][currX]) {
                queue.add(new Integer[]{currX, currY + 1});
                visited[currY + 1][currX] = true;
                prev[currY + 1][currX] = currX + currY * labyrinth[0].length;
            }
        }

        // Generate the path.
        int curr = to;
        while (curr != from) {
            if (curr != 1) {
                path.add(curr + 1);
                curr = prev[currY][currX];
                currX = curr % labyrinth[0].length;
                currY = curr / labyrinth[0].length;
            }

        }

        if (!isPathValid(path)) {
            System.out.println("None");
            return;
        }

        path.add(from + 1);

        System.out.println("Length " + (path.size() - 1) + ": ");

        // Print path in reverse separated by newline.
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.println(path.get(i));
        }
    }

    /**
     * Checks if the given path is valid.
     *
     * @param path The path to check.
     * @return True if the path is valid, false otherwise.
     */
    private boolean isPathValid(ArrayList<Integer> path) {
        boolean valid = true;

        // Check each pair of adjacent vertices.
        for (int i = 0; i < path.size() - 1; i++) {
            Integer [] moves = nextMoves(path.get(i));

            // Check if the next move is valid.
            boolean contains = false;
            for (int j = 0; j < moves.length; j++) {
                if (moves[j] == path.get(i + 1)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Returns the next moves for the given position.
     *
     * @param i The position to check.
     * @return The next moves for the given position.
     */
    private Integer[] nextMoves(int i) {
        Integer[] moves = new Integer[4];

        moves[0] = i - labyrinth[0].length;
        moves[1] = i + 1;
        moves[2] = i + labyrinth[0].length;
        moves[3] = i - 1;

        return moves;
    }
}