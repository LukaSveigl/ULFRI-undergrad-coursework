public class Izziv1 {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        ArrayDeque<Integer> testDeque = new ArrayDeque<>();

        try {
            System.out.println("DequeArray before any operations: " + testDeque);

            System.out.println("\n\n--Demonstration of Stack methods--");

            // Change this variable when changing operations executed to see if testDeque equals expected
            // output after operations are applied
            String expectedOutput = "[9, 1, 2, 3, 4, 7, 11]";

            testDeque.push(1);
            testDeque.push(2);
            testDeque.push(3);
            testDeque.push(4);
            testDeque.push(5);
            testDeque.push(6);

            System.out.println("\nDequeArray after pushing 6 times: " + testDeque);

            System.out.println("\nResult of DequeArray.pop():       " + testDeque.pop());
            System.out.println("Result of DequeArray.pop():       " + testDeque.pop());

            System.out.println("\nDequeArray after poping 2 times:  " + testDeque);

            System.out.println("\nResult of DequeArray.top():       " + testDeque.top());



            System.out.println("\n\n--Demonstration of Deque methods--");

            testDeque.enqueue(7);
            testDeque.enqueue(8);

            System.out.println("\nDequeArray after enqueueing 2 times:          " + testDeque);

            testDeque.enqueueFront(9);
            testDeque.enqueueFront(10);

            System.out.println("\nDequeArray after enqueueing at front 2 times: " + testDeque);

            System.out.println("\nResult of DequeArray.dequeue():               " + testDeque.dequeue());
            System.out.println("Result of DequeArray.dequeueBack():           " + testDeque.dequeueBack());

            System.out.println("\nDequeArray after dequeueing 2 times:          " + testDeque);

            System.out.println("\nResult of DequeArray.front():                 " + testDeque.front());
            System.out.println("Result of DequeArray.back():                  " + testDeque.back());



            System.out.println("\n\n--Demonstration of Sequence methods--");

            testDeque.add(11);

            System.out.println("\nDequeArray after adding:     " + testDeque);

            System.out.println("\nResult of DequeArray.get(4): " + testDeque.get(4));

            System.out.println("\n\n--Verify results--");
            if (testDeque.toString().equals(expectedOutput)) {
                System.out.println(testDeque.toString() + " is correct.");
            }
            else {
                System.out.println(testDeque.toString() + " is incorrect.");
            }

        } catch (CollectionException e) {
            System.out.println(e);
        }

    }
}

class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}


interface Collection {
    String ERR_MSG_EMPTY = "Collection is empty.";
    String ERR_MSG_FULL = "Collection is full.";

    boolean isEmpty();

    boolean isFull();

    int size();

    String toString();
}


interface Stack<T> extends Collection {
    T top() throws CollectionException;

    void push(T x) throws CollectionException;

    T pop() throws CollectionException;
}


interface Deque<T> extends Collection {
    T front() throws CollectionException;

    T back() throws CollectionException;

    void enqueue(T x) throws CollectionException;

    void enqueueFront(T x) throws CollectionException;

    T dequeue() throws CollectionException;

    T dequeueBack() throws CollectionException;
}


interface Sequence<T> extends Collection {
    String ERR_MSG_INDEX = "Wrong index in sequence.";

    T get(int i) throws CollectionException;

    void add(T x) throws CollectionException;
}



class ArrayDeque<T> implements Deque<T>, Stack<T>, Sequence<T> {
    private static final int DEFAULT_CAPACITY = 64;

    // Instance of internal array
    private T arr[];

    // Counter of elements currently in array
    private int size;

    private int front;
    private int back;


    /*
    Constructors
     */


    ArrayDeque() {
        this.arr = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;

        this.front = 0;
        this.back = 0;
    }

    ArrayDeque(int capacity) {
        this.arr = (T[]) new Object[capacity];
        this.size = 0;

        this.front = 0;
        this.back = 0;
    }


   /*
   Utility methods
    */


    private void rewrite() {
        // Create instance of temporary array to copy items to
        T tmp_arr[] = (T[]) new Object[this.arr.length];

        if (this.front <= this.back) {
            // Loop through entire array
            for (int i = this.front; i < this.back; i++) {
                tmp_arr[i] = this.arr[i];
            }
        } else {
            // Loop from front index to end of array
            for (int i = this.front; i < this.arr.length; i++) {
                tmp_arr[i] = this.arr[i];
            }
            // Loop from start of array to back index
            for (int j = 0; j < this.back; j++) {
                tmp_arr[j] = this.arr[j];
            }
        }

        // Clone temporary array into internal array
        this.arr = tmp_arr.clone();
    }


    /*
    Methods of interface Collection
     */


    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean isFull() {
        return this.size == this.arr.length;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("[");

        if (this.isEmpty()) {
            builder.append("]");
            return builder.toString();
        }

        if (this.front <= this.back) {
            for (int i = this.front; i < this.back; i++) {
                builder.append(this.arr[i]);
                builder.append(", ");
            }

            builder.delete(builder.length() - 2, builder.length());
            builder.append("]");

        } else {
            for (int i = this.front; i < this.arr.length; i++) {
                builder.append(this.arr[i]);
                builder.append(", ");
            }

            for (int j = 0; j < this.back; j++) {
                builder.append(this.arr[j]);
                builder.append(", ");
            }

            // Remove final instance of ", "
            builder.delete(builder.length() - 2, builder.length());
            builder.append("]");
        }
        return builder.toString();
    }


    /*
    Methods of interface deque
     */


    @Override
    public T front() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.arr[this.front];
    }

    @Override
    public T back() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.arr[this.back - 1];
    }

    @Override
    public void enqueue(T x) throws CollectionException {
        if (this.isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }

        this.back++;

        // If back is at end of array - move back index to start of array
        if (this.back == this.arr.length) {
            this.back = 0;
        }

        this.arr[this.back - 1] = x;

        this.size++;
    }

    @Override
    public void enqueueFront(T x) throws CollectionException {
        if (this.isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }

        this.front--;

        // If front is before first index of array - move front index to last index in array
        if (this.front < 0) {
            this.front = this.arr.length - 1;
        }

        this.arr[front] = x;

        this.size++;
    }

    @Override
    public T dequeue() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }

        // Save instance of needed element
        T el = this.arr[this.front];

        this.front++;

        // If front is at end of array - move front index to start of array
        if (this.front == this.arr.length) {
            this.front = 0;
        }

        // Rewrite array without dequeued element - front was moved forwards so that element is ignored when rewriting
        this.rewrite();

        this.size--;

        return el;
    }

    @Override
    public T dequeueBack() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }

        T el = this.arr[this.back - 1];

        this.back--;

        // If back is at index before start of array - move back index to last index of array
        if (this.back < 0) {
            this.back = this.arr.length - 1;
        }

        // Rewrite array without dequeued element - back was moved backwards so that element is ignored when rewriting
        this.rewrite();

        this.size--;

        return el;
    }


    /*
    Methods of interface stack
     */


    @Override
    public T top() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.back();
    }

    @Override
    public void push(T x) throws CollectionException {
        if (this.isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }

        this.enqueue(x);

    }

    @Override
    public T pop() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }

        return this.dequeueBack();
    }


    /*
    Methods of interface Sequence
     */


    @Override
    public T get(int i) throws CollectionException {
        if (i < 0 || i >= this.arr.length) {
            throw new CollectionException(ERR_MSG_INDEX);
        }
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }

        if (i + this.front < this.arr.length) { 
            return this.arr[i];
        } else {
            return this.arr[i + this.front - this.arr.length];
        }
    }

    @Override
    public void add(T x) throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_FULL);
        }

        this.enqueue(x);
    }

}