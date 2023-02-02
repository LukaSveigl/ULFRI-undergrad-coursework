import java.util.ArrayList;

public class DN3 {
    public static void main(String[] args) {
        //System.out.println("Hello World");

        /*HTB ht = new HTB(7, 13, 5, 11);

        ht.insert(19); ht.insert(11); ht.insert(23);
        System.out.println(ht.find(19));
        System.out.println("--");
        ht.delete(19); ht.delete(29);
        System.out.println(ht.find(19));
        System.out.println("--");

        ht.insert(17); ht.insert(2); ht.insert(33);

        ht.printKeys();*/


        /*HTB ht = new HTB(7, 13, 5, 11);

        ht.insert(19); ht.insert(11); ht.insert(23); ht.insert(19); ht.insert(29);
        ht.insert(17); ht.insert(2); ht.insert(33); ht.insert(99); ht.insert(129);

        ht.printKeys();
        System.out.println("--");
        ht.printCollisions();*/

        /*HTB ht = new HTB(7, 3, 5, 7);

        ht.insert(19);
        ht.insert(11);
        ht.insert(23);
        ht.insert(19);
        ht.insert(29);
        ht.insert(17);
        ht.insert(2);
        ht.insert(33);
        ht.insert(99);
        ht.insert(129);

        ht.printKeys();
        System.out.println("--");
        ht.printCollisions();*/

        HTB ht = new HTB(7, 3, 5, 7);
        ht.insert(19);
        ht.printKeys();
        System.out.println("--");
        ht.insert(11);
        ht.printKeys();
        System.out.println("--");
        ht.insert(23);
        ht.printKeys();
        System.out.println("--");
        ht.insert(29);
        ht.printKeys();
        System.out.println("--");
        ht.insert(17);
        ht.printKeys();
        System.out.println("--");
        ht.insert(2);
        ht.printKeys();
        System.out.println("--");
        ht.printCollisions();

    }
}

/**
 * A hash table using hash function h(k) = (k * p) % m and
 * collision resolution using function h'(k, i) = h(k) + c1 * i + c2 * i^2.
 */
class HTB {
    /** Size of the array. */
    private int m;
    /** The coefficient used in calculating the hash. */
    private final int p;
    /** The constants used in calculating the hash used for collision resolution. */
    private final int c1;
    private final int c2;

    /** The collision counter. */
    private int collisions;

    /** The array that holds the keys at indices decided by hashing. */
    private ArrayList<Integer> table;

    HTB(int p, int m, int c1, int c2) {
        this.p = p;
        this.m = m;
        this.c1 = c1;
        this.c2 = c2;
        this.collisions = 0;
        this.table = new ArrayList<Integer>(m);
        for (int i = 0; i < m; i++) {
            this.table.add(null);
        }
    }

    /**
     * Insert key into the hash table, ignore duplicate keys, count collisions and resize table if necessary.
     *
     * @param key The key to insert.
     */
    public void insert(int key) {
        // Check if key is already in the table.
        for (Integer k : table) {
            if (k != null && k == key) {
                return;
            }
        }

        // Calculate the hash.
        int h = hash(key);
        // Check if table has space at hash index.
        if (table.get(h) == null) {
            table.set(h, key);
        }
        // If not, resolve collision.
        else {
            // Loop until an empty spot is found.
            for (int i = 1; i < m; i++) {
                // Calculate the hash for the collision resolution.
                h = hash(key, i);
                collisions++;
                // Check if table has space at hash index and end insertion if so.
                if (table.get(h) == null) {
                    table.set(h, key);
                    return;
                }
            }
            // If no space found, resize and rehash.
            resize();
            // Reattempt insertion.
            insert(key);
        }
    }

    /**
     * Checks if the key is in the hash table.
     *
     * @param key The key to check.
     * @return True if the key is in the hash table, false otherwise.
     */
    public boolean find(int key) {
        int i = 0;
        int h = hash(key);
        while (table.get(h) != null) {
            if (table.get(h) == key) {
                return true;
            }
            if (i > m) {
                return false;
            }
            h = hash(key, i);
            i++;
        }
        return false;
    }

    /**
     * Delete the key from the hash table.
     *
     * @param key The key to delete.
     */
    public void delete(int key) {
        int i = 0;
        int h = hash(key);
        while (table.get(h) != null) {
            if (table.get(h) == key) {
                table.set(h, null);
                return;
            }
            h = hash(key, i);
            i++;
        }
    }

    /**
     * Print the keys in the hash table in format i: key.
     */
    public void printKeys() {
        for (int i = 0; i < table.size(); i++) {
            if (table.get(i) != null) {
                System.out.println(i + ": " + table.get(i));
            }
        }
    }

    /**
     * Print collisions.
     */
    public void printCollisions() {
        System.out.println(collisions);
    }

    // UTILS

    /**
     * Main hash function.
     *
     * @param k The key to hash.
     * @return The hash value.
     */
    private int hash(int k) {
        return (k * p) % m;
    }

    /**
     * Hash function used for collision resolution.
     *
     * @param k The key to hash.
     * @param i The index of the key.
     * @return The hash value.
     */
    private int hash(int k, int i) {
        return (hash(k) + (c1 * i) + (c2 * i * i)) % m;
    }

    /**
     * Resize the hash table to size 2 * current size + 1 and rehash elements.
     */
    private void resize() {
        ArrayList<Integer> oldTable = table;
        table = new ArrayList<Integer>(2 * m + 1);
        for (int i = 0; i < 2 * m + 1; i++) {
            table.add(null);
        }
        m = 2 * m + 1;
        for (int i = 0; i < oldTable.size(); i++) {
            if (oldTable.get(i) != null) {
                int h = hash(oldTable.get(i));
                int l = 0;
                while (table.get(h) != null) {
                    h = hash(oldTable.get(i), l);
                    collisions++;
                    l++;
                }
                table.set(h, oldTable.get(i));
            }
        }
    }
}
