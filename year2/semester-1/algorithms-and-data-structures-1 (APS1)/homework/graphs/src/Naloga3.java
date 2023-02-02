import java.util.Scanner;

public class Naloga3 {
    public static void main(String[] args) {
        /*System.out.println("Hello world");

        Graph graph = new Graph(10);
        graph.addNodes(0, 1);
        graph.addNodes(1, 2);
        graph.addNodes(1, 3);
        graph.addNodes(1, 4);
        graph.addNodes(2, 4);
        graph.addNodes(2, 5);
        graph.addNodes(3, 0);
        graph.addNodes(4, 3);
        graph.addNodes(4, 5);
        graph.addNodes(6, 7);
        graph.addNodes(7, 9);

        graph.info(true);

        graph.walks(true, 3);

        graph.BFS(true);
        System.out.println("\n");
        graph.DFS(false);
        System.out.println("\n");
        graph.shortestPaths(false, 4);
        System.out.println("\n");
        graph.comp(false);*/

        Scanner sc = new Scanner(System.in);

        String commands = sc.nextLine();
        String arrSizeString = sc.nextLine().strip();
        int arrSize = Integer.parseInt(arrSizeString);

        Graph graph = new Graph(arrSize);

        while(sc.hasNextLine()) {
            String link = sc.nextLine();
            if(link.equals("")) {
                break;
            }

            String[] nodeStrings = link.strip().split(" ");

            for (int i = 0; i < nodeStrings.length; i++) {
                nodeStrings[i] = nodeStrings[i].strip();
            }

            graph.addNodes(Integer.parseInt(nodeStrings[0]), Integer.parseInt(nodeStrings[1]));
        }

        String[] cmd = commands.split(" ");

        boolean directed = false;

        if (cmd[0].equals("directed")) {
            directed = true;
        }
        else {
            directed = false;
        }

        if (cmd[1].equals("info")) {
            graph.info(directed);
        }
        if (cmd[1].equals("walks")) {
            int size = Integer.parseInt(cmd[2]);
            graph.walks(directed, size);
        }
        if (cmd[1].equals("dfs")) {
            graph.DFS(directed);
        }
        if (cmd[1].equals("bfs")) {
            graph.BFS(directed);
        }
        if (cmd[1].equals("sp")) {
            int size = Integer.parseInt(cmd[2]);
            graph.shortestPaths(directed, size);
        }
        if (cmd[1].equals("comp")) {
            graph.comp(directed);
        }
    }
}

@SuppressWarnings("unchecked")
class uVector<T> {
    private int capacity = 10;
    private int size = 0;
    private T[] arr = (T[]) new Object[this.capacity];

    // Constructors
    uVector() {

    }

    uVector(T[] values) {
        // Fill vector with values passed in from array
        for (int i = 0; i < values.length; i++) {
            if (i == this.arr.length) {
                this.resize();
            }
            this.arr[i] = values[i];
            this.size++;
        }
    }

    // Utility methods
    private void resize() {
        // Resize vector when capacity hit
        this.capacity *= 2;

        T[] tmp = (T[]) new Object[this.capacity];
        int counter = 0;

        for (int i = 0; i < this.arr.length; i++) {
            tmp[i] = this.arr[i];
            counter++;
        }

        this.arr = (T[]) new Object[this.capacity];

        for (int i = 0; i < counter; i++) {
            this.arr[i] = tmp[i];
        }
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        String st = "[";
        for (int i = 0; i < this.size; i++) {
            st += this.arr[i].toString() + ", ";
        }
        if (st.length() > 2) {
            st = st.substring(0, st.length() - 2);
        }
        st += "]";

        return st;
    }

    // Adders
    public int add(T element) {
        // Try to add element, if successful return 0, else return -1
        try {
            // Resize if needed
            if (this.size + 1 == this.capacity) {
                this.resize();
            }

            this.arr[this.size] = element;
            this.size++;

            return 0;
        } catch (Error e) {
            return -1;
        }
    }

    public int add(T element, int index) {
        // Try to add element, if successful return 0, else return -1
        try {
            // Check if index out of bounds
            if (index > this.size - 1) {
                throw new Error("Index out of bounds!");
            }
            this.arr[index] = element;
            return 0;

        } catch (Error e) {
            return -1;
        }
    }

    public int extend(uVector<T> vec) {
        try {
            for (int i = 0; i < vec.size(); i++) {
                this.add(vec.get(i));
            }
            return 1;

        } catch (Error e) {
            return -1;
        }
    }

    // Removers
    public T pop() {
        // Return and remove last element in vector

        // Check if vector already empty
        if (this.size == 0) {
            throw new Error("Cannot perform pop on empty vector!");
        }
        T element = this.arr[this.size - 1];

        T[] tmp = (T[]) new Object[this.size];

        for (int i = 0; i < this.size; i++) {
            tmp[i] = this.arr[i];
        }

        this.size--;
        this.arr = (T[]) new Object[this.size];

        for (int i = 0; i < this.size; i++) {
            this.arr[i] = tmp[i];
        }

        return element;
    }

    public int remove(int index) {
        // Remove element at index

        // Check if index out of bounds
        if (index > this.size - 1) {
            throw new Error("Index out of bounds!");
        }
        // Check if vector already empty
        if (this.size == 0) {
            throw new Error("Cannot perform remove on empty vector!");
        }

        // Try to remove element, if successful return 0, else return -1
        try {
            T[] tmp = (T[]) new Object[this.size - 1];

            for (int i = 0; i < index; i++) {
                tmp[i] = this.arr[i];
            }

            for (int j = index + 1; j < this.size; j++) {
                tmp[j - 1] = this.arr[j];
            }

            this.arr = (T[]) new Object[this.capacity];
            this.size = 0;

            for (int k = 0; k < tmp.length; k++) {
                this.arr[k] = tmp[k];
                this.size++;
            }

            return 0;
        } catch (Error e) {
            return -1;
        }
    }

    // Finders
    public int indexOf(T element) {
        // Find index of element
        for (int i = 0; i < this.size; i++) {
            if (this.arr[i] == element) {
                return i;
            }
        }
        return -1;
    }

    public T get(int index) {
        // Get element at index
        if (index > this.size - 1) {
            throw new Error("Index out of bounds!");
        }
        if (index < 0) {
            return this.arr[this.size - Math.abs(index)];
        }
        return this.arr[index];
    }
}

class Matrix {
    int[][] matrix;
    int cols;
    int rows;

    Matrix(int cols, int rows) {
        this.matrix = new int[rows][cols];

        this.cols = cols;
        this.rows = rows;

        for (int rs = 0; rs < this.rows; rs++) {
            for (int cs = 0; cs < this.cols; cs++) {
                this.matrix[rs][cs] = 0;
            }
        }
    }

    @Override
    public String toString() {
        String st = "";
        for (int rs = 0; rs < this.rows; rs++) {
            for (int cs = 0; cs < this.cols; cs++) {
                st += this.matrix[rs][cs] + " ";
            }
            st = st.substring(0, st.length() - 1);
            st += "\n";
            //st += "\b\n";
        }
        return st;
    }

    public void multiply(Matrix m) {
        int[][] result = new int[this.rows][m.cols];

        for (int rs = 0; rs < this.rows; rs++) {
            for (int cs = 0; cs < m.cols; cs++) {
                result[rs][cs] = 0;
            }
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                for (int k = 0; k < m.rows; k++) {
                    result[i][j] += this.matrix[i][k] * m.matrix[k][j];
                }
            }
        }

        this.matrix = result;
    }

    public void transpose() {
        int[][] tmp = new int[this.rows][this.cols];
        for (int rs = 0; rs < this.rows; rs++) {
            for (int cs = 0; cs < this.cols; cs++) {
                tmp[cs][rs] = this.matrix[rs][cs];
            }
        }
        this.matrix = tmp;
    }
}

@SuppressWarnings("unchecked")
class Graph {
    int[] nodes;
    uVector<Integer>[] inNodes;
    uVector<Integer>[] outNodes;
    Matrix m;

    String st = "";

    // Assemble graph of numberOfNodes nodes
    Graph(int numberOfNodes) {
        this.nodes = new int[numberOfNodes];
        this.inNodes = new uVector[numberOfNodes];
        this.outNodes = new uVector[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            this.inNodes[i] = new uVector<>();
            this.outNodes[i] = new uVector<>();
            this.nodes[i] = i;
        }

        this.m = new Matrix(numberOfNodes, numberOfNodes);
    }


    // Add node to graph
    public void addNodes(int node, int nextNode) {
        // Set node's in node and out node
        this.outNodes[node].add(nextNode);
        this.inNodes[nextNode].add(node);
    }

    public void generateAdjacencyMatrix(boolean directed) {
        // Create empty matrix
        Matrix m = new Matrix(this.nodes.length, this.nodes.length);

        // If directed, fill matrix only with outgoing connections
        if (directed) {
            for (int i = 0; i < this.nodes.length; i++) {
                for (int j = 0; j < this.outNodes[i].size(); j++) {
                    m.matrix[this.outNodes[i].get(j)][i] = 1;
                }
            }
        }
        // If undirected, fill matrix with outgoing and incoming connections
        else {
            for (int i = 0; i < this.nodes.length; i++) {
                for (int j = 0; j < this.outNodes[i].size(); j++) {
                    m.matrix[this.outNodes[i].get(j)][i] = 1;
                }
                for (int j = 0; j < this.inNodes[i].size(); j++) {
                    m.matrix[this.inNodes[i].get(j)][i] = 1;
                }
            }

        }

        // Overwrite internal matrix
        this.m = m;
    }

    // Print info of graph
    public void info(boolean directed) {
        // If graph directed, output only incoming connections, output all connections (there are no permuted duplicates)
        if (directed) {
            int links = 0;
            // Calculate number of connections in graph
            for (int i = 0; i < this.nodes.length; i++) {
                links += this.inNodes[i].size();
            }
            System.out.println(this.nodes.length + " " + links);

            // Print all connections
            for (int i = 0; i < this.nodes.length; i++) {
                System.out.println(i + " " + this.outNodes[i].size() + " " + this.inNodes[i].size());
            }
        }
        else {
            // Create vector of encountered connections
            uVector<Integer[]> uniques = new uVector<>();
            for (int i = 0; i < this.nodes.length; i++) {
                for (int j = 0; j < this.inNodes[i].size(); j++) {
                    Integer[] item = {nodes[i], inNodes[i].get(j)};
                    boolean found = false;
                    for (int k = 0; k < uniques.size(); k++) {
                        Integer[] unique = uniques.get(k);
                        if (unique[0] == item[0] && unique[1] == item[1]) {
                            found = true;
                            break;
                        }
                        if (unique[0] == item[1] && unique[1] == item[0]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        uniques.add(item);
                    }
                }
            }
            System.out.println(this.nodes.length + " " + uniques.size());

            // Print power of node in graph
            for (int i = 0; i < this.nodes.length; i++) {
                //int links = this.inNodes[i].size() + this.outNodes[i].size();
                int links = 0;

                for (int j = 0 ; j < uniques.size(); j++) {
                    if (i == uniques.get(j)[0] || i == uniques.get(j)[1]) {
                        links++;
                    }
                }

                System.out.println(i + " " + links);
            }
        }
    }

    public void walks(boolean directed, int walkLength) {
        // Generate adjacency matrix for graph
        this.generateAdjacencyMatrix(directed);

        System.out.println(this.m);

        // Create empty matrix
        Matrix m = new Matrix(this.nodes.length, this.nodes.length);

        // Copy values from adjacency matrix to new matrix
        for (int rs = 0; rs < this.m.rows; rs++) {
            for (int cs = 0; cs < this.m.cols; cs++) {
                m.matrix[rs][cs] = this.m.matrix[rs][cs];
            }
        }

        // Multiply enough times - example: If we need walks of length 3, we multiply A^1 * A^1 * A^1 (multiply adjacency
        // matrix 3 times)
        for (int i = 0; i < walkLength - 1; i++) {
            m.multiply(this.m);
        }

        // Transpose matrix to get correct result
        m.transpose();

        System.out.println(m);
    }

    public void BFS(boolean directed) {
        // Generate adjacency matrix for this graph
        this.generateAdjacencyMatrix(directed);

        // Generate array of visited nodes
        boolean[] visited = new boolean[this.nodes.length];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        // First node is visited
        visited[0] = true;

        // Add first node to queue
        uVector<Integer> queue = new uVector<>();
        queue.add(this.nodes[0]);

        // Go through all nodes in graph
        for (int i = 0; i < this.nodes.length; i++) {
            // If node not already visited
            if (!visited[this.nodes[i]]) {
                // If queue is empty, add node (needed to not skip queue while adding new node - only add when it's this
                // node's turn to be checked)
                if (queue.size() == 0) {
                    queue.add(this.nodes[i]);
                    visited[this.nodes[i]] = true;
                }

                // Do until queue empty
                while(queue.size() > 0) {
                    // Dequeue first node in queue
                    int vertex = queue.get(0);
                    queue.remove(0);
                    // Print node
                    System.out.print(vertex + " ");

                    // Go through all nodes
                    for (int j = 0; j < this.nodes.length; j++) {
                        // If directed, check only outgoing connections
                        if (directed) {
                            // Check if nodes are neighbours and new node is not visited yet
                            if (this.m.matrix[j][vertex] == 1 && !visited[j]) {
                                // Add node to queue and set it as visited
                                queue.add(j);
                                visited[j] = true;
                            }
                        }
                        // If not directed, check outgoing and incoming connections
                        else {
                            // Check if nodes are neighbours and new node is not visited yet
                            if (this.m.matrix[j][vertex] == 1 && !visited[j]) {
                                // Add node to queue and set it as visited
                                queue.add(j);
                                visited[j] = true;
                            }
                            if (this.m.matrix[vertex][j] == 1 && !visited[j]) {
                                // Add node to queue and set it as visited
                                queue.add(j);
                                visited[j] = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public void DFS(boolean directed) {
        // Generate adjacency matrix for this graph
        this.generateAdjacencyMatrix(directed);

        // Generate array of visited nodes
        boolean[] visited = new boolean[this.nodes.length];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        this.st = "";

        // Visit all incoming connections first
        for(int i = 0; i < this.nodes.length; i++) {
            if (!visited[i]) {
                this.DFSSearch(directed, true, visited, this.nodes[i]);
            }
        }

        this.st = this.st.strip();

        System.out.println(this.st);

        // Reset visited nodes
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        this.st = "";

        // Visit all outgoing connections
        for(int i = 0; i < this.nodes.length; i++) {
            if (!visited[i]) {
                this.DFSSearch(directed, false, visited, this.nodes[i]);
            }
        }

        this.st = this.st.strip();

        System.out.println(this.st);

        //System.out.print("\b");
    }

    private void DFSSearch(boolean directed, boolean in, boolean[] visited, int node) {
        // Mark nodes as visited
        visited[node] = true;

        // If printing incoming nodes
        if (in) {
            this.st += node + " ";
        }

        // Go through all nodes in graph
        for (int j = 0; j < this.nodes.length; j++) {
            // If directed, check only outgoing connections
            if (directed) {
                // Check if nodes are neighbours and new node is not visited yet
                if (this.m.matrix[j][node] == 1 && !visited[j]) {
                    // Recursively visit next node
                    this.DFSSearch(directed, in, visited, this.nodes[j]);
                }
            }
            // If undirected, check only incoming and outgoing connections
            else {
                // Check if nodes are neighbours and new node is not visited yet
                if (this.m.matrix[j][node] == 1 && !visited[j]) {
                    this.DFSSearch(directed, in, visited, this.nodes[j]);
                }
                if (this.m.matrix[node][j] == 1 && !visited[j]) {
                    this.DFSSearch(directed, in, visited, this.nodes[j]);
                }
            }
        }

        // If printing outgoing nodes
        if (!in) {
            this.st += node + " ";
        }
    }

    public void shortestPaths(boolean directed, int node) {
        // Generate adjacency matrix for this graph
        this.generateAdjacencyMatrix(directed);

        int[] distances = new int[this.nodes.length];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = -1;
        }

        for (int i = 0; i < this.nodes.length; i++) {
            // Generate array of visited nodes
            boolean[] visited = new boolean[this.nodes.length];
            for (int v = 0; v < visited.length; v++) {
                visited[i] = false;
            }

            visited[this.nodes[i]] = true;

            int distance = -1;

            uVector<Integer> queue = new uVector<>();
            queue.add(this.nodes[i]);

            boolean found = false;

            while(queue.size() > 0) {
                int vertex = queue.get(0);
                queue.remove(0);
                distance++;

                if (vertex == node) {
                    found = true;
                    break;
                }

                // Go through all nodes
                for (int j = 0; j < this.nodes.length; j++) {
                    // If directed, check only outgoing connections
                    if (directed) {
                        // Check if nodes are neighbours and new node is not visited yet
                        if (this.m.matrix[vertex][j] == 1 && !visited[j]) {
                            // Add node to queue and set it as visited
                            queue.add(j);
                            visited[j] = true;

                            if (j == node) {
                                distance++;
                                found = true;
                                break;
                            }
                        }
                    }
                    // If not directed, check outgoing and incoming connections
                    else {
                        // Check if nodes are neighbours and new node is not visited yet
                        if (this.m.matrix[j][vertex] == 1 && !visited[j]) {
                            // Add node to queue and set it as visited
                            queue.add(j);
                            visited[j] = true;

                            if (j == node) {
                                distance++;
                                found = true;
                                break;
                            }
                        }
                        if (this.m.matrix[vertex][j] == 1 && !visited[j]) {
                            // Add node to queue and set it as visited
                            queue.add(j);
                            visited[j] = true;

                            if (j == node) {
                                distance++;
                                found = true;
                                break;
                            }
                        }
                    }
                }
                if (found) {
                    break;
                }
            }

            if (found) {
                distances[i] = distance;
            }

            found = false;
        }

        for (int i = 0; i < distances.length; i++) {
            System.out.println(i + " " + distances[i]);
        }
    }

    public void comp(boolean directed) {
        this.generateAdjacencyMatrix(directed);

        if (directed) {

        }
        else {
            uVector<uVector<Integer>> connected = new uVector<>();
            for (int i = 0; i < this.nodes.length; i++) {
                connected.add(new uVector<>());
            }

            boolean[] found = new boolean[this.nodes.length];
            for (int i = 0; i < this.nodes.length; i++) {
                found[i] = false;
            }

            for (int i = 0; i < this.nodes.length; i++) {
                if (!found[i]) {
                    // Generate array of visited nodes
                    boolean[] visited = new boolean[this.nodes.length];
                    for (int v = 0; v < visited.length; v++) {
                        visited[v] = false;
                    }

                    this.DFSComp(i, connected, found, this.nodes[i], visited);
                }
            }

            connected = this.processComponents(connected);

            for (int i = 0; i < connected.size(); i++) {
                for (int j = 0; j < connected.get(i).size(); j++) {
                    System.out.print(connected.get(i).get(j));

                    if (j != connected.get(i).size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    private void DFSComp(int count, uVector<uVector<Integer>> connected, boolean[] found, int node, boolean[]visited) {
        found[count] = true;
        found[node] = true;
        visited[node] = true;

        connected.get(count).add(node);

        // Go through all nodes in graph
        for (int j = 0; j < this.nodes.length; j++) {
            // Check if nodes are neighbours and new node is not visited yet
            if (this.m.matrix[j][node] == 1 && !visited[j] && !found[j]) {
                this.DFSComp(count, connected, found, this.nodes[j], visited);
            }
            if (this.m.matrix[node][j] == 1 && !visited[j] && !found[j]) {
                this.DFSComp(count, connected, found, this.nodes[j], visited);
            }
        }
    }

    private uVector<uVector<Integer>> processComponents(uVector<uVector<Integer>> connected) {
        uVector<uVector<Integer>> nonEmpty = new uVector<>();

        for (int i = 0; i < connected.size(); i++) {
            if (connected.get(i).size() != 0) {
                nonEmpty.add(connected.get(i));
            }
        }

        for (int i = 0; i < nonEmpty.size(); i++) {
            int n = nonEmpty.get(i).size();
            for (int j = 0; j < n - 1; j++) {
                for (int k = 0; k < n - i - 1; k++) {
                    if (nonEmpty.get(i).get(k) > nonEmpty.get(i).get(k + 1)) {
                        int tmp = nonEmpty.get(i).get(k);
                        nonEmpty.get(i).add(nonEmpty.get(i).get(k + 1), k);
                        nonEmpty.get(i).add(tmp, k + 1);
                    }
                }
            }
        }

        for (int i = 0; i < nonEmpty.size() - 1; i++) {
            for (int j = 0; j < nonEmpty.size() - i - 1; j++) {
                if (nonEmpty.get(j).get(0) > nonEmpty.get(j + 1).get(0)) {
                    uVector<Integer> tmp = nonEmpty.get(j);
                    nonEmpty.add(nonEmpty.get(j + 1), j);
                    nonEmpty.add(tmp, j + 1);
                }
            }
        }


        return nonEmpty;
    }
}