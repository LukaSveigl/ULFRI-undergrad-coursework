import java.util.Arrays;
import java.util.Random;

public class Izziv4 {
    public static void main(String[] args) throws CollectionException {
        Random rand = new Random();

        ArrayPQ<Integer> apq = new ArrayPQ<>();
        ArrayHeapPQ<Integer> ahpq = new ArrayHeapPQ<>();

        long startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            apq.enqueue(rand.nextInt(2000));
        }

        for (int i = 0; i < 500; i++) {
            apq.dequeue();
            apq.enqueue(rand.nextInt(2000));
            apq.front();
        }

        long endTime = System.nanoTime();

        long apqTime = endTime - startTime;

        startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            ahpq.enqueue(rand.nextInt(2000));
        }

        for (int i = 0; i < 500; i++) {
            ahpq.dequeue();
            ahpq.enqueue(rand.nextInt(2000));
            ahpq.front();
        }

        endTime = System.nanoTime();

        long ahpqTime = endTime - startTime;

        System.out.printf("Objekti: Integer\n");
        System.out.printf("Operacije: 100 enqueue + 500 (dequeue+enqueue+front)\n");
        System.out.printf("Implementacija                         Cas[ms]          Premiki         Primerjave\n");
        System.out.printf("----------------------------------------------------------------------------------\n");
        System.out.printf("Neurejeno polje  (64,2x) %18d %17d %20d\n", apqTime / 1000000, apq.moves, apq.compares);
        System.out.printf("Implicitna kopica  (64,2x) %16d %17d %20d\n", ahpqTime / 1000000, ahpq.moves, ahpq.compares);
    }
}

class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}
interface Collection {
    static final String ERR_MSG_EMPTY = "Collection is empty.";

    boolean isEmpty();
    int size();
    String toString();
}
interface Queue<T> extends Collection {
    T front() throws CollectionException;
    void enqueue(T x);
    T dequeue() throws CollectionException;
}
interface PriorityQueue<T extends Comparable> extends Queue<T> {
}

class ArrayPQ<T extends Comparable> implements PriorityQueue<T> {

    private T[] arr;

    private int capacity = 64;
    private int size = 0;

    int compares = 0;
    int moves = 0;

    // Constructors

    ArrayPQ() {
        this.arr = (T[])new Comparable[this.capacity];
    }


    // Utility methods

    private void resize() {
        this.capacity *= 2;

        T[] tmp = (T[])new Comparable[this.capacity];

        for (int i = 0; i < this.size; i++) {
            tmp[i] = this.arr[i];
        }

        this.arr = (T[])new Comparable[this.capacity];

        for (int i = 0; i < this.size; i++) {
            this.arr[i] = tmp[i];
        }

    }


    // Collection methods

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public String toString() {
        return Arrays.toString(this.arr);
    }


    // Queue methods

    @Override
    public T front() throws CollectionException {
        if (this.size == 0) {
            throw new Error(ERR_MSG_EMPTY);
        }
        else {
            return this.arr[this.findHighest()];
        }
    }

    @Override
    public void enqueue(T x) {
        if (this.size + 1 == this.capacity - 1) {
            this.resize();
        }

        this.moves++;

        this.arr[this.size] = x;

        this.size++;
    }


    @Override
    public T dequeue() throws CollectionException {
        if (this.size == 0) {
            throw new Error(ERR_MSG_EMPTY);
        }
        else {
            T[] tmp = (T[]) new Comparable[this.size - 1];

            int ind = this.findHighest();

            T el = this.arr[ind];

            this.arr[ind] = this.arr[this.size - 1];

            this.size--;

            for (int i = 0; i < this.size; i++) {
                tmp[i] = this.arr[i];
            }

            for (int i = 0; i < tmp.length; i++) {
                this.arr[i] = tmp[i];
            }

            return el;
        }
    }


    // Utility methods

    private int findHighest() {
        T highest = this.arr[0];
        int highestIndex = 0;

        for (int i = 0; i < this.size; i++) {
            this.compares++;
            if (highest.compareTo(this.arr[i]) < 0) {
                highest = this.arr[i];
                highestIndex = i;
            }
        }
        return highestIndex;
    }
}




class ArrayHeapPQ<T extends Comparable> implements PriorityQueue<T> {

    private T[] arr;

    private int capacity = 64;
    private int size = 0;


    int moves = 0;
    int compares = 0;

    // Constructors

    ArrayHeapPQ() {
        this.arr = (T[])new Comparable[this.capacity];
    }


    // Utility methods

    private void resize() {
        this.capacity *= 2;

        T[] tmp = (T[])new Comparable[this.capacity];

        for (int i = 0; i < this.size; i++) {
            tmp[i] = this.arr[i];
        }

        this.arr = (T[])new Comparable[this.capacity];

        for (int i = 0; i < this.size; i++) {
            this.arr[i] = tmp[i];
        }

    }


    // Collection methods

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    public String toString() {
        return Arrays.toString(this.arr);
    }


    // Queue methods

    @Override
    public T front() throws CollectionException {
        if (this.size == 0) {
            throw new Error(ERR_MSG_EMPTY);
        }

        return this.arr[0];
    }

    @Override
    public void enqueue(T x) {
        if (this.size + 1 == this.capacity) {
            resize();
        }

        this.moves++;

        this.arr[this.size] = x;

        this.siftUp(this.size);

        this.size++;
    }


    @Override
    public T dequeue() throws CollectionException {
        if (this.size == 0) {
            throw new Error(ERR_MSG_EMPTY);
        }

        T el = this.arr[0];

        this.arr[0] = this.arr[this.size - 1];
        this.size--;

        this.siftDown(this.size);

        return el;
    }


    // Heap methods

    private int parent(int i) {
        if (i > 0) {
            return i / 2;
        }

        return 0;
    }

    private int rightChild(int i) {
        if (2 * i + 1 < this.size) {
            return 2 * i + 1;
        }
        return -1;
    }

    private int leftChild(int i) {
        if (2 * i + 2 < this.size) {
            return 2 * i + 2;
        }
        return -1;
    }

    private void siftUp(int i) {
        if (this.size > 1) {
            int p = this.parent(i);

            this.compares++;
            if (this.arr[i].compareTo(this.arr[p]) > 0) {
                T tmp = this.arr[p];
                this.arr[p] = this.arr[i];
                this.arr[i] = tmp;

                this.moves += 3;

                this.siftUp(p);
            }
        }
    }

    private void siftDown(int i) {
        int l = leftChild(i);
        int r = rightChild(i);

        if (l != -1) {
            this.compares++;
            if (this.arr[i].compareTo(this.arr[l]) > 0) {
                T tmp = this.arr[l];
                this.arr[l] = this.arr[i];
                this.arr[i] = tmp;

                this.moves += 3;
            }

        }

        if (r != -1) {
            this.compares++;
            if (this.arr[i].compareTo(this.arr[r]) > 0) {
                T tmp = this.arr[r];
                this.arr[r] = this.arr[i];
                this.arr[i] = tmp;

                this.moves += 3;
            }
        }

        if (i != 0) {
            siftDown(this.parent(i));
        }
    }
}




