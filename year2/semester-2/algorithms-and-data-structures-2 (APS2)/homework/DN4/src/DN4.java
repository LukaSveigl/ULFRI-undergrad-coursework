public class DN4 {
    public static void main(String[] args) {
        /*binheap bh = new binheap();

        bh.insert(10);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(20);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(30);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(5);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(15);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(7);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(25);
        bh.printComparisons();
        System.out.println("--");
        bh.printElements();
        bh.printMin();
        bh.printComparisons();
        bh.deleteMin();
        bh.printElements();
        bh.printComparisons();*/

        /*binheap bh = new binheap();

        bh.printElements();
        bh.printMin();
        bh.printComparisons();
        bh.insert(30);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(20);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(10);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(7);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(25);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(5);
        bh.printComparisons();
        System.out.println("--");
        bh.insert(15);
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();             // <-- problem
        bh.printElements();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");
        bh.deleteMin();
        bh.printComparisons();
        System.out.println("--");*/

        binheap bh = new binheap();

        bh.printElements();
        bh.printMin();
        bh.printComparisons();
        bh.insert(30); bh.insert(20); bh.insert(10);
        bh.insert(7); bh.insert(25); bh.insert(5); bh.insert(15);
        bh.printElements();
        bh.printMin();
        bh.printComparisons();
        bh.deleteMin();
        bh.printElements();
        bh.printComparisons();
        bh.deleteMin();
        bh.printElements();
        bh.deleteMin();
        bh.printElements();
        bh.deleteMin(); bh.deleteMin(); bh.deleteMin();
        bh.printElements();
        bh.deleteMin();
        bh.printElements();
        bh.printComparisons();
    }
}

/**
 * A priority queue implementation using a binary heap.
 */
class binheap {

    /** The binary heap. */
    private Integer[] heap;
    /** The number of elements in the heap. */
    private int size;
    /** The number of comparisons made. */
    private int comparisons;

    /**
     * Constructs a new binary heap.
     */
    public binheap() {
        heap = new Integer[1];
        size = 0;
        comparisons = 0;
    }

    /**
     * Inserts a new element into the end of the heap and performs a siftUp operation.
     *
     * @param value The value to insert.
     */
    public void insert(int value) {
        // If the heap is full, resize.
        if (size == heap.length) {
            resize();
        }
        heap[size] = value;
        iterativeSiftUp(size);
        size++;
    }

    /**
     * Deletes the minimum (first) element from the heap by swapping it with the last element
     * and performs a siftDown operation.
     */
    public void deleteMin() {
        if (size == 0) {
            System.out.println("false");
            return;
        }
        System.out.println("true: " + heap[0]);
        // Swap the first element with the last, and set the last element to null.
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        iterativeSiftDown(0);
    }

    /**
     * Prints the elements in the heap.
     */
    public void printElements() {
        if (size == 0) {
            System.out.println("empty");
        }
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i]);
            if (i != size - 1) {
                System.out.print(", ");
            }
            else {
                System.out.println();
            }
        }
    }

    /**
     * Prints the minimum element in the heap.
     */
    public void printMin() {
        if (size == 0) {
            System.out.println("MIN: none");
        } else {
            System.out.println("MIN: " + heap[0]);
        }
    }

    /**
     * Prints the number of comparisons made.
     */
    public void printComparisons() {
        System.out.println("COMPARISONS: " + comparisons);
    }

    // Utils

    /**
     * Performs an iterative siftUp operation on the heap.
     *
     * @param index The index of the element to sift up.
     */
    private void iterativeSiftUp(int index) {
        // Loop until the element is in the correct position.
        while (index > 0) {
            this.comparisons++;
            // If the parent is greater than the child, swap them.
            if (heap[(index - 1) / 2] > heap[index]) {
                swap(index, (index - 1) / 2);
            }
            else {
                break;
            }
            // Move up one level.
            index = (index - 1) / 2;
        }
    }

    /**
     * Performs an iterative siftDown operation on the heap.
     *
     * @param index The index of the element to sift down.
     */
    private void iterativeSiftDown(int index) {
        int smallerChildIndex = index;
        // Iteratively sift down until the element is in the correct position.
        while (index * 2 + 1 < size) {
            if (index * 2 + 2 < size) {
                this.comparisons++;
                // Find the smaller child.
                if (heap[index * 2 + 1] < heap[index * 2 + 2]) {
                    smallerChildIndex = index * 2 + 1;
                }
                if (heap[index * 2 + 2] < heap[index * 2 + 1]) {
                    smallerChildIndex = index * 2 + 2;
                }
                if (index != 0)
                    this.comparisons++;
                // If the parent is greater than the smaller child, swap.
                if (heap[index] > heap[smallerChildIndex]) {
                    swap(index, smallerChildIndex);
                    index = smallerChildIndex;
                }
                else {
                    break;
                }
            }
            else {
                this.comparisons++;
                // If the parent is greater than the left child, swap.
                if (heap[index] > heap[index * 2 + 1]) {
                    swap(index, index * 2 + 1);
                    index = index * 2 + 1;
                }
                else {
                    break;
                }
            }
        }
    }

    /**
     * Swaps the elements at the given indices.
     *
     * @param i The index of the first element.
     * @param j The index of the second element.
     */
    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * Resizes the heap array.
     */
    private void resize() {
        Integer[] newHeap = new Integer[heap.length * 2];
        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

}