import java.util.Arrays;
import java.util.Scanner;

public class Naloga2 {
    public static void main(String[] args) {
        // Strings that hold commands and elements of array
        String commands = "";
        String array = "";
        Scanner sc = new Scanner(System.in);

        // Create new instance of class Sorter, which contains sort implementations
        Sorter sort = new Sorter();

        boolean up = false;

        //do {
        while(sc.hasNextLine()) {
            up = false;

            commands = sc.nextLine();
            array = sc.nextLine();

            // Squeeze spaces into 1
            commands = commands.replaceAll("\\s+", " ");
            array = array.replaceAll("\\s+", " ");

            // Split strings by spaces
            String[] cArr = commands.split(" ");
            String[] sArr = array.split(" ");

            Integer[] eArr = new Integer[sArr.length];

            // Parse elements into integers
            for (int i = 0; i < eArr.length; i++) {
                eArr[i] = Integer.parseInt(sArr[i]);
            }

            // Create new array type
            Arr<Integer> arr = new Arr<>();

            arr.clear();

            // Fill array
            arr.fromArray(eArr);

            sort.arr.clear();

            // Set array to be sorted
            sort.setArray(arr);

            // If sorting is ascending
            if (cArr[2].equals("up")) {
                up = true;
            }

            // If sorting counts operations performed
            if (cArr[0].equals("count")) {
                sort.count(cArr[1], up);
            }
            // If sorting prints trace
            else {
                sort.trace(cArr[1], up);
            }
        }
        //} while (!commands.equals(""));
    }
}

@SuppressWarnings("unchecked")
class Arr<T extends Comparable> {
    T[] arr;
    private int size;
    private int capacity = 64;


    // Constructors

    Arr() {
        this.arr = (T[]) new Comparable[this.capacity];
        this.size = 0;
    }


    // Utility methods

    @Override
    public String toString() {
        String arr = "[";
        for (T el : this.arr) {
            if (el != null) {
                arr += el.toString() + ", ";
            }
        }
        arr += "]";

        return arr;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }


    // Reshaping methods

    public void clear() {
        this.arr = (T[]) new Comparable[this.capacity];
        this.size = 0;
    }

    void resize() {
        this.capacity *= 2;

        T[] tmp = (T[]) new Comparable[this.capacity];

        for (int i = 0; i < this.arr.length; i++) {
            tmp[i] = this.arr[i];
        }

        this.arr = (T[]) new Comparable[this.capacity];

        for (int i = 0; i < this.size; i++) {
            this.arr[i] = tmp[i];
        }
    }


    // Data manipulation methods

    public void set(int index, T el) {
        // Method that sets value at some index
        if (index > this.size) {
            throw new Error("Index out of bounds!");
        } else {
            this.arr[index] = el;
        }

    }

    public void push(T el) {
        // Method that adds element to end of array, increasing it's size
        if (this.size + 1 == this.capacity) {
            this.resize();
        }
        this.arr[this.size] = el;

        this.size++;
    }

    public T get(int index) {
        // Method that returns value at certain index
        if (index > this.size) {
            throw new Error("Index out of bounds!");
        } else {
            return this.arr[index];
        }
    }


    // Creation/dispatch methods

    public Arr<T> copy() {
        Arr<T> tmp = new Arr<>();

        for (int i = 0; i < this.size; i++) {
            tmp.push(this.arr[i]);
        }

        return tmp;
    }


    public void fromArray(T[] arr) {
        this.arr = (T[]) new Comparable[this.capacity];

        while (this.arr.length < arr.length) {
            this.resize();
        }

        for (int i = 0; i < arr.length; i++) {
            this.arr[i] = arr[i];
            this.size++;
        }
    }

    public T[] toArray() {
        return this.arr;
    }
}

@SuppressWarnings("unchecked")
class Sorter {
    Arr<Integer> arr;

    int[] counter;

    // Internal flags that control program execution
    boolean trace = false;
    boolean up = false;

    Sorter() {
        this.arr = new Arr<>();
        this.counter = new int[2];
    }

    public void setArray(Arr arr) {
        this.arr = arr;
    }

    public void setUp(boolean flag) {
        this.up = flag;
    }


    // Sort methods

    public void insert(Arr<Integer> arr) {
        int n = arr.size();

        boolean found = false;
        for (int i = 1; i < n; i++) {
            found = false;
            //this.counter[0]++;
            int k = arr.get(i);
            int j = i;
            if (this.up) {
                while(j > 0 && arr.get(j - 1) > k) {
                    this.counter[1]++;
                    this.counter[0]++;
                    arr.set(j, arr.get(j - 1));
                    j--;
                    found = true;
                }
            }
            else {
                while(j > 0 && arr.get(j - 1) < k) {
                    this.counter[1]++;
                    this.counter[0]++;
                    arr.set(j, arr.get(j - 1));
                    j--;
                    found = true;
                }
            }
            if (j > 0) {
                this.counter[1]++;
            }

            //if (!found) {
            //    this.counter[1]++;
            //}
            this.counter[0]+=2;
            arr.set(j, k);

            if (this.trace) {
                for (int l = 0; l < i + 1; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int s = i + 1; s < arr.size(); s++) {
                    System.out.print(arr.get(s));

                    if (s != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void select() {

        if (this.trace) {
            for (int i = 0; i < arr.size(); i++) {
                System.out.print(arr.get(i));
                if (i != arr.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        for (int i = 0; i < this.arr.size() - 1; i++) {
            int m = i;

            for (int j = i + 1; j < this.arr.size(); j++) {
                if (this.up) {
                    this.counter[1]++;
                    if (this.arr.get(j) < this.arr.get(m)) {
                        m = j;
                    }
                } else {
                    this.counter[1]++;
                    if (this.arr.get(j) > this.arr.get(m)) {
                        m = j;
                    }
                }

            }
            this.swap(i, m, this.arr);

            if (this.trace) {
                for (int l = 0; l < i + 1; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int s = i + 1; s < arr.size(); s++) {
                    System.out.print(arr.get(s));

                    if (s != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    /*public void bubble() {
        int n = this.arr.size();
        int lSwap = this.arr.size() - 1;
        boolean swapped = false;
        for (int i = 1; i < n; i++) {
            System.out.flush();
            lSwap = this.arr.size() - 1;
            swapped = false;
            for (int j = n - 1; j >= i; j--) {
                if (j != i) {
                    this.counter[1]++;
                }
                if (this.up) {
                    if (this.arr.get(j - 1) > this.arr.get(j)) {
                        this.swap(j-1, j, this.arr);
                        swapped = true;
                        lSwap = j;
                    }
                }
                else {
                    if (this.arr.get(j - 1) < this.arr.get(j)) {
                        this.swap(j-1, j, this.arr);
                        swapped = true;
                        lSwap = j;
                    }
                }
            }

            this.counter[1]++;
            if (!swapped || lSwap == this.arr.size() - 1) {
                break;
            }

            if (this.trace) {
                for (int l = 0; l < lSwap; l++) {
                    System.out.print(arr.get(l) + " ");
                    System.out.flush();
                }
                System.out.print("| ");
                for (int s = lSwap; s < arr.size(); s++) {
                    System.out.print(arr.get(s));
                    System.out.flush();
                    if (s != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }

        }
        System.out.flush();
        if (this.trace) {
            for (int l = 0; l < lSwap; l++) {
                System.out.print(arr.get(l) + " ");
            }
            System.out.print("| ");
            for (int s = lSwap; s < arr.size(); s++) {
                System.out.print(arr.get(s));

                if (s != arr.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }*/

    public void bubble() {
        int counter = this.arr.size() - 1;
        int ordered = -1;

        while(counter >= 0) {
            int swapCounter = this.arr.size() - 1;
            int nextOrdered = this.arr.size() - 1;

            for (int i = swapCounter - 1; i > ordered; i--) {
                if (this.up) {
                    this.counter[1]++;
                    if (this.arr.get(swapCounter) < this.arr.get(i)) {
                        this.swap(swapCounter, i, this.arr);
                        nextOrdered = i;
                    }
                    swapCounter = i;
                }
                else {
                    this.counter[1]++;
                    if (this.arr.get(swapCounter) > this.arr.get(i)) {
                        this.swap(swapCounter, i, this.arr);
                        nextOrdered = i;
                    }
                    swapCounter = i;
                }
            }
            ordered = nextOrdered;

            if (this.trace) {
                int separator = nextOrdered;

                if (nextOrdered == this.arr.size() - 1) {
                    separator = this.arr.size() - 2;
                }

                for (int l = 0; l < separator + 1; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int s = separator + 1; s < arr.size(); s++) {
                    System.out.print(arr.get(s));

                    if (s != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }

            if (nextOrdered > this.arr.size() - 3) {
                break;
            }

            counter--;
        }
    }

    public void heap() {
        int n = this.arr.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            this.siftDown(n, i);
        }


        if (this.trace) {
            for (int l = 0; l < this.arr.size(); l++) {
                System.out.print(arr.get(l) + " ");
            }
            System.out.print("| ");
            System.out.println();
        }

        for (int i = n - 1; i > 0; i--) {
            this.swap(i, 0, this.arr);

            siftDown(i, 0);

            if (this.trace) {
                for (int l = 0; l < i; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int s = i; s < arr.size(); s++) {
                    System.out.print(arr.get(s));

                    if (s != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void merge(Arr<Integer> arr, int low, int high) {
        if (high <= low) return;


        int mid = (low + high) / 2;

        if (this.trace) {
            for (int l = low; l < mid + 1; l++) {
                System.out.print(arr.get(l) + " ");
            }
            System.out.print("| ");
            for (int s = mid + 1; s < high + 1; s++) {
                System.out.print(arr.get(s));

                if (s != high) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        merge(arr, low, mid);
        merge(arr, mid + 1, high);



        combine(arr, low, mid, high);

        if (this.trace) {
            for (int l = low; l < high + 1; l++) {
                System.out.print(arr.get(l) + " ");
            }

            System.out.println();
        }


    }

    public void quick(Arr<Integer> arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);

            if (this.trace) {
                for (int l = low; l < p; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int l = p; l < p + 1; l++) {
                    System.out.print(arr.get(l) + " ");
                }
                System.out.print("| ");
                for (int s = p + 1; s < high + 1; s++) {
                    System.out.print(arr.get(s));

                    if (s != high) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }

            quick(arr, low, p - 1);
            quick(arr, p + 1, high);
        }
    }

    public void radix() {
        int max = this.arr.get(0);

        if (this.trace) {
            for (int i = 0; i < this.arr.size(); i++) {
                System.out.print(arr.get(i));
                if (i != this.arr.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        for (int i = 1; i < this.arr.size(); i++) {
            if (max < this.arr.get(i)) {
                max = this.arr.get(i);
            }
        }

        for (int s = 1; max / s > 0; s *= 10) {
            this.countSort(this.arr, s);

            if (this.trace) {
                for (int i = 0; i < this.arr.size(); i++) {
                    System.out.print(arr.get(i));
                    if (i != this.arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    public void bucket() {
        if (this.trace) {
            for (int i = 0; i < this.arr.size(); i++) {
                System.out.print(arr.get(i));
                if (i != this.arr.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        int k = this.arr.size() / 2;

        Arr<Integer>[] buckets = new Arr[k];

        int max = this.arr.get(0);
        int min = this.arr.get(0);

        for (int i = 0; i < this.arr.size(); i++) {
            this.counter[1]++;
            if (this.arr.get(i) < min) {
                min = this.arr.get(i);
            }
            else if (this.arr.get(i) > max) {
                max = this.arr.get(i);
            }
        }

        int v = (max - min + 1) / k;


        for (int i = 0; i < k; i++) {
            this.counter[0]++;
            buckets[i] = new Arr<Integer>();
        }


        for (int i = 0; i < this.arr.size(); i++) {
            this.counter[0]++;
            this.counter[1]++;
            if (this.up) {
                buckets[this.hash(this.arr.get(i), min, v, k)].push(this.arr.get(i));
            }
            else {
                buckets[k - this.hash(this.arr.get(i), min, v, k) - 1].push(this.arr.get(i));
            }
        }

        if (this.trace) {
            for (int b = 0; b < buckets.length; b++) {
                for (int bl = 0; bl < buckets[b].size(); bl++) {
                    System.out.print(buckets[b].get(bl));
                    if (bl != buckets[b].size()) {
                        System.out.print(" ");
                    }
                }
                if (b != buckets.length - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }

        int ind = 0;

        Arr<Integer> tmp = new Arr<>();

        for (int i = 0; i < k; i++) {
            this.counter[0]++;
            for (int j = 0; j < buckets[i].size(); j++) {
                //this.counter[1]++;
                tmp.push(buckets[i].get(j));
            }
        }

        this.insert(tmp);

        this.arr = tmp.copy();



    }

    void swap(int i1, int i2, Arr<Integer> tmp) {
        int tmpEl = tmp.get(i2);
        tmp.set(i2, tmp.get(i1));
        tmp.set(i1, tmpEl);
        this.counter[0] += 3;
    }


    // Sort utility methods


    void siftDown(int n, int ind) {
        int l = 2 * ind + 1;
        int r = 2 * ind + 2;

        int max = ind;

        this.counter[1]++;
        if (this.up) {
            if (l < n && this.arr.get(l) > this.arr.get(max)) {
                max = l;
            }
            if (r < n && this.arr.get(r) > this.arr.get(max)) {
                max = r;
            }
        }
        else {
            if (l < n && this.arr.get(l) < this.arr.get(max)) {
                max = l;
            }
            if (r < n && this.arr.get(r) < this.arr.get(max)) {
                max = r;
            }
        }

        if (max != ind) {
            this.swap(ind, max, this.arr);
            this.siftDown(n, max);
        }
    }

    void combine(Arr<Integer> arr, int low, int mid, int high) {
        Arr<Integer> lArr = new Arr<>();
        Arr<Integer> rArr = new Arr<>();

        for (int i = 0; i < mid - low + 1; i++) {
            this.counter[0]++;
            lArr.push(arr.get(low + i));
        }
        for (int i = 0; i < high - mid; i++) {
            this.counter[0]++;
            rArr.push(arr.get(mid + i + 1));
        }

        int lInd = 0;
        int rInd = 0;

        for (int i = low; i < high + 1; i++) {
            if (lInd < lArr.size() && rInd < rArr.size()) {
                if (this.up) {
                    this.counter[1]++;
                    if (lArr.get(lInd) <= rArr.get(rInd)) {
                        this.counter[0]++;
                        arr.set(i, lArr.get(lInd));
                        lInd++;
                    } else {
                        this.counter[0]++;
                        arr.set(i, rArr.get(rInd));
                        rInd++;
                    }
                } else {
                    this.counter[1]++;
                    if (lArr.get(lInd) >= rArr.get(rInd)) {
                        this.counter[0]++;
                        arr.set(i, lArr.get(lInd));
                        lInd++;
                    } else {
                        this.counter[0]++;
                        arr.set(i, rArr.get(rInd));
                        rInd++;
                    }
                }
            } else if (lInd < lArr.size()) {
                this.counter[0]++;
                arr.set(i, lArr.get(lInd));
                lInd++;
            } else if (rInd < rArr.size()) {
                this.counter[0]++;
                arr.set(i, rArr.get(rInd));
                rInd++;
            }
        }
    }

    int partition(Arr<Integer> arr, int low, int high) {
        this.counter[0]++;
        int p = arr.get(low);
        int l = low;
        int r = high + 1;

        while (true) {
            if (this.up) {
                do {
                    l++;
                    this.counter[1]++;
                } while (arr.get(l) < p && l < high);

                do {
                    r--;
                    this.counter[1]++;
                } while (arr.get(r) > p);

                if (l >= r) {
                    break;
                }
            } else {
                do {
                    l++;
                    this.counter[1]++;
                } while (arr.get(l) > p && l < high);

                do {
                    r--;
                    this.counter[1]++;
                } while (arr.get(r) < p);

                if (l >= r) {
                    break;
                }
            }


            this.swap(l, r, arr);
        }

        this.swap(low, r, arr);
        return r;
    }

    void countSort(Arr<Integer> arr, int s) {
        int[] countArr = new int[10];

        for (int i = 0; i < countArr.length; i++) {
            countArr[i] = 0;
        }

        for (int i = 0; i < arr.size(); i++) {
            if (this.up) {
                this.counter[1]++;
                countArr[(arr.get(i) / s) % 10]++;
            } else {
                this.counter[1]++;
                countArr[9 - (arr.get(i) / s) % 10]++;
            }

        }

        for (int i = 1; i < countArr.length; i++) {
            countArr[i] += countArr[i - 1];
        }

        int[] outArr = new int[arr.size()];

        for (int i = arr.size() - 1; i >= 0; i--) {
            if (this.up) {
                this.counter[0]++;
                this.counter[1]++;
                outArr[--countArr[(arr.get(i) / s) % 10]] = arr.get(i);
            } else {
                this.counter[0]++;
                this.counter[1]++;
                outArr[--countArr[9 - (arr.get(i) / s) % 10]] = arr.get(i);
            }
        }

        for (int i = 0; i < arr.size(); i++) {
            this.counter[0]++;
            arr.set(i, outArr[i]);
        }

    }

    int hash(int el, int min, int v, int k) {
        this.counter[1]++;
        int h = (el - min) / v;
        if (h >= k) {
            return k - 1;
        }
        return (el - min) / v;
    }


    // Driver methods

    public void count(String method, boolean up) {
        StringBuilder result = new StringBuilder();

        this.trace = false;

        this.up = up;

        switch (method) {
            case "insert":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    if (i == 0) {
                        this.insert(this.arr);
                    } else if (i == 1) {
                        this.insert(this.arr);
                    } else {
                        this.up = !this.up;
                        this.insert(this.arr);
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "select":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    if (i == 0) {
                        this.select();
                    } else if (i == 1) {
                        this.select();
                    } else {
                        this.up = !this.up;
                        this.select();
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "bubble":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    if (i == 0) {
                        this.bubble();
                    } else if (i == 1) {
                        this.bubble();
                    } else {
                        this.up = !this.up;
                        this.bubble();
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "heap":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;


                    if (i == 0) {
                        this.heap();
                    } else if (i == 1) {
                        this.heap();
                    } else {
                        this.up = !this.up;
                        this.heap();
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "merge":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    //Arr<Integer> tmp = this.arr.copy();
                    if (i == 0) {
                        this.merge(this.arr, 0, this.arr.size() - 1);
                    } else if (i == 1) {
                        this.merge(this.arr, 0, this.arr.size() - 1);
                    } else {
                        this.up = !this.up;
                        this.merge(this.arr, 0, this.arr.size() - 1);
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "quick":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;


                    if (i == 0) {
                        this.quick(this.arr, 0, this.arr.size() - 1);
                    } else if (i == 1) {
                        this.quick(this.arr, 0, this.arr.size() - 1);
                    } else {
                        this.up = !this.up;
                        this.quick(this.arr, 0, this.arr.size() - 1);
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "radix":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    if (i == 0) {
                        this.radix();
                    } else if (i == 1) {
                        this.radix();
                    } else {
                        this.up = !this.up;
                        this.radix();
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            case "bucket":
                for (int i = 0; i < 3; i++) {
                    this.counter[0] = 0;
                    this.counter[1] = 0;

                    if (i == 0) {
                        this.bucket();
                    } else if (i == 1) {
                        this.bucket();
                    } else {
                        this.up = !this.up;
                        this.bucket();
                    }

                    result.append(this.counter[0]).append(" ").append(this.counter[1]).append(" | ");
                }
                break;
            default:
                throw new Error("Unknown sorting method!");

        }

        result.setLength(result.length() - 3);

        System.out.println(result);
    }

    public void trace(String method, boolean up) {
        this.trace = true;

        this.up = up;

        switch (method) {
            case "insert":
                for (int i = 0; i < arr.size(); i++) {
                    System.out.print(arr.get(i));
                    if (i != arr.size() - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println();

                this.insert(this.arr);
                break;
            case "select":
                this.select();
                break;
            case "bubble":
                if (this.trace) {
                    for (int i = 0; i < arr.size(); i++) {
                        System.out.print(arr.get(i));
                        if (i != arr.size() - 1) {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                }
                this.bubble();
                break;
            case "heap":
                if(this.trace) {
                    for (int i = 0; i < arr.size(); i++) {
                        System.out.print(arr.get(i));
                        if (i != arr.size() - 1) {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                }

                this.heap();
                break;
            case "merge":
                Arr<Integer> tmp = this.arr.copy();

                for (int i = 0; i < this.arr.size(); i++) {
                    System.out.print(arr.get(i));
                    if (i != this.arr.size()) {
                        System.out.print(" ");
                    }
                }
                System.out.println();


                this.merge(this.arr, 0, this.arr.size() - 1);
                break;
            case "quick":
                Arr<Integer> tmp1 = this.arr.copy();

                for (int i = 0; i < this.arr.size(); i++) {
                    System.out.print(arr.get(i));
                    if (i != this.arr.size()) {
                        System.out.print(" ");
                    }
                }
                System.out.println();

                this.quick(this.arr, 0, this.arr.size() - 1);

                for (int i = 0; i < this.arr.size(); i++) {
                    System.out.print(arr.get(i));
                    if (i != this.arr.size()) {
                        System.out.print(" ");
                    }
                }
                System.out.println();
                break;
            case "radix":
                this.radix();
                break;
            case "bucket":
                this.bucket();
                break;
            default:
                throw new Error("Unknown sorting method!");

        }
    }
}
