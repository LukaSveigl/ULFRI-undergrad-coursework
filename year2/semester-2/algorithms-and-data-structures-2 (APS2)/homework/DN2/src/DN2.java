import java.util.ArrayList;

public class DN2 {
    public static void main(String[] args) {
        /*BST b = new BST();

        b.insert(19);
        b.insert(11);
        b.insert(23);
        b.insert(19);
        b.insert(29);
        b.printPreorder();
        System.out.println("--");

        b.printInorder();
        System.out.println("--");

        b.printPostorder();*/

        /*BST b = new BST();

        b.insert(19);
        b.printNodeComparisons();
        System.out.println("--");

        b.insert(11);
        b.insert(23);
        b.insert(31);
        b.insert(42);
        b.insert(29);
        System.out.println(b.find(29));
        System.out.println("--");

        b.insert(23);
        b.insert(29);
        b.delete(31);
        System.out.println(b.find(31));
        System.out.println(b.find(23));
        System.out.println("--");
        b.printInorder();
        System.out.println("--");

        b.printNodeComparisons();*/

        /*BST b = new BST();
        b.insert(19);
        b.printPreorder();
        b.delete(19);
        b.printPreorder();*/


        /*BST b = new BST();

        b.insert(19); b.insert(19); b.insert(19);
        b.insert(11); b.insert(9); b.insert(11);
        b.printInorder();
        System.out.println("--");
        b.delete(23); b.insert(23);
        b.insert(17); b.insert(20); b.insert(18); b.insert(25);
        b.printInorder();
        System.out.println("--");
        b.delete(19); b.delete(19);
        b.delete(11); b.delete(11); b.delete(23);
        System.out.println( b.find(19) );
        System.out.println("--");
        b.printPreorder();*/

        /*BST b = new BST();
        b.insert(19);
        b.insert(20);
        b.insert(18);
        b.printPreorder();
        System.out.println("--");
        b.delete(21);
        b.printPreorder();
        System.out.println("--");
        b.printNodeComparisons();*/

        /*BST b = new BST();
        b.insert(19);
        b.insert(20);
        b.insert(18);
        b.insert(21);
        b.insert(27);
        b.insert(21);
        b.insert(22);
        b.insert(23);
        b.printPreorder();
        System.out.println("--");
        b.delete(22);
        b.printPreorder();*/

        /*BST b = new BST();
        b.insert(25);
        b.insert(25);
        b.insert(25);
        b.insert(25);
        b.insert(25);
        b.insert(25);
        b.printPreorder();
        System.out.println("--");
        //b.printNodeComparisons();
        System.out.println("--");
        b.delete(25);
        //System.out.println(b.root.getValue() + " " + b.root.getOccurrences());
        b.printPreorder();
        System.out.println("--");
        b.delete(25);
        b.delete(25);
        b.delete(25);
        b.delete(25);
        b.delete(25);
        b.printPreorder();
        System.out.println("--");
        //b.printNodeComparisons();*/

        /*BST b = new BST();
        b.insert(25);
        b.insert(24);
        b.insert(23);
        b.insert(22);
        b.insert(21);
        b.insert(20);
        b.insert(19);
        b.insert(18);
        b.insert(17);
        b.insert(16);
        b.insert(15);
        b.insert(14);
        b.insert(13);
        b.insert(12);
        b.printPreorder();
        System.out.println("--");
        b.delete(25);
        b.printPreorder();
        System.out.println("--");
        b.insert(25);
        b.printPreorder();*/

        /*"+20+10+30+5+15+4+7+6+17" +
                "?20?10?30?5?15?4?7?6?17" +
                "?16?1?3" +
                "-10?10?4?6?7",
                "true //
                true //
                true //
                true //
                true //
                true //
                true //
                true //
                true //
                false
                false
                false
                false
                true
                true
                true"*/

        /*BST bst = new BST();
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        bst.insert(5);
        bst.insert(15);
        bst.insert(4);
        bst.insert(7);
        bst.insert(6);
        bst.insert(17);
        //bst.printInorder();
        //System.out.println("--");
        System.out.println(bst.find(20));
        System.out.println(bst.find(10));
        System.out.println(bst.find(30));
        System.out.println(bst.find(5));
        System.out.println(bst.find(15));
        System.out.println(bst.find(4));
        System.out.println(bst.find(7));
        System.out.println(bst.find(6));
        System.out.println(bst.find(17));
        System.out.println(bst.find(16));
        System.out.println(bst.find(1));
        System.out.println(bst.find(3));
        bst.delete(10);
        //System.out.println("--");
        //bst.printPreorder();
        System.out.println(bst.find(10));
        System.out.println(bst.find(4));
        System.out.println(bst.find(6));
        System.out.println(bst.find(7));*/

        /*
        * test("+20+10+30+5+15+17+16+25+35-20p",
             "17\n" +
             "10\n" +
             "5\n" +
             "15\n" +
             "16\n" +
             "30\n" +
             "25\n" +
             "35");
             */

        /* BST b = new BST();
        b.insert(20);
        b.insert(10);
        b.insert(30);
        b.insert(5);
        b.insert(15);
        b.insert(17);
        b.insert(16);
        b.insert(25);
        b.insert(35);
        b.delete(20);
        b.printPreorder();
*/
        /*BST b = new BST();

        b.insert(19);
        b.printNodeComparisons();
        System.out.println("--");

        b.insert(11); b.insert(23); b.insert(31); b.insert(42); b.insert(29);
        System.out.println( b.find(29) );
        System.out.println("--");

        b.insert(23); b.insert(29); b.delete(31);
        System.out.println( b.find(31) );
        System.out.println( b.find(23) );
        System.out.println("--");
        b.printInorder();
        System.out.println("--");

        b.printNodeComparisons();*/

        /*BST b = new BST();
        b.insert(20);
        System.out.println("--");
        b.insert(10);
        System.out.println("--");
        b.insert(30);
        System.out.println("--");
        b.insert(5);
        System.out.println("--");
        b.insert(15);
        System.out.println("--");
        b.insert(17);
        System.out.println("--");
        b.insert(16);
        System.out.println("--");
        b.insert(25);
        System.out.println("--");
        b.insert(35);
        System.out.println("--");
        b.insert(1);
        System.out.println("--");
        b.insert(20);
        System.out.println("--");
        b.insert(1);
        System.out.println("--");
        b.insert(17);
        System.out.println("--");

        b.printInorder();
        System.out.println("--");
        b.printPreorder();
        System.out.println("--");
        b.printPostorder();
        System.out.println("--");*/

        /*BST b = new BST();
        b.insert(20);
        b.insert(10);
        b.insert(30);
        b.insert(5);
        b.printPreorder();
        System.out.println("--");
        //b.delete(5); // Expected: 20, 10, 30
        //b.delete(10); // Expected: 20, 5, 30
        //b.delete(30); // Expected: 20, 10, 5
        // b.delete(20); // Expected: 10, 5, 30
        b.delete(20);
        b.printPreorder();
        System.out.println("--");
        b.delete(10);
        b.printPreorder();
        System.out.println("--");
        b.delete(30);
        b.printPreorder();
        System.out.println("--");
        b.delete(5);
        b.printPreorder();*/

        BST b = new BST();
        b.insert(20);
        b.insert(10);
        b.insert(30);
        b.printPreorder();
        System.out.println("--");
        b.delete(20);
        b.printPreorder();
        System.out.println("--");
    }
}

/**
 * A binary tree node.
 */
class Element {
    /**
     * The element value.
     */
    private int value;
    /**
     * The counter of occurrences.
     */
    private int occurrences;
    /**
     * The left child.
     */
    private Element left;
    /**
     * The right child.
     */
    private Element right;

    /**
     * Constructs a new node.
     *
     * @param value the element value
     */
    public Element(int value) {
        this.value = value;
        this.occurrences = 1;
    }


    /**
     * Getters and setters.
     */

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOccurrences() {
        return this.occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public Element getLeft() {
        return this.left;
    }

    public Element getRight() {
        return this.right;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    public void setRight(Element right) {
        this.right = right;
    }

    /**
     * Occurrences manipulators.
     */

    public void incrementOccurrences() {
        this.occurrences++;
    }

    public void decrementOccurrences() {
        this.occurrences--;
    }
}

/**
 * A binary search tree.
 */
class BST {
    /**
     * The root node.
     */
    private Element root;
    /**
     * The number of comparisons that happened in the lifespan of the tree.
     */
    private int nodeComparisons;
    /**
     * Flag that indicates if upon deletion, the left subtree should be chosen.
     */
    private boolean left;

    /**
     * Constructs a new tree.
     */
    BST() {
        this.root = null;
        this.nodeComparisons = 0;
        this.left = true;
    }

    /**
     * Public facing insert. It initializes the insertion process of the value.
     *
     * @param value the value to be inserted
     */
    public void insert(int value) {
        if (this.root == null) {
            this.root = new Element(value);
        }
        else {
            this.insert(this.root, value);
        }
    }

    /**
     * Inserts a value in the tree.
     *
     * @param element the current node of the tree
     * @param value the value to be inserted
     */
    private void insert(Element element, int value) {
        this.nodeComparisons++;
        if (element.getValue() == value) {
            element.incrementOccurrences();
            return;
        }

        // Check which subtree to insert into
        if (value < element.getValue()) {
            if (element.getLeft() == null) {
                element.setLeft(new Element(value));
            }
            else {
                this.insert(element.getLeft(), value);
            }
        }
        else {
            if (element.getRight() == null) {
                element.setRight(new Element(value));
            }
            else {
                this.insert(element.getRight(), value);
            }
        }
    }

    /**
     * Public facing delete. It initializes the deletion process of the value.
     *
     * @param value the value to be deleted
     */
    public void delete(int value) {
        if (this.root == null) {
            return;
        }

        this.root = this.delete(this.root, value);
    }

    /**
     * Deletes a value from the tree.
     *
     * @param element the current node of the tree
     * @param value the value to be deleted
     * @return the new root of the tree
     */
    private Element delete(Element element, int value) {
        if (element == null) {
            return null;
        }

        this.nodeComparisons++;
        if (value < element.getValue()) {
            // Find the element in the left subtree.
            element.setLeft(this.delete(element.getLeft(), value));
        }
        else if (value > element.getValue()) {
            // Find the element in the right subtree.
            element.setRight(this.delete(element.getRight(), value));
        }
        else {
            // Found the element.
            element.decrementOccurrences();

            // Delete element if it has no occurrences.
            if (element.getOccurrences() == 0) {
                // If the element has no children return null.
                if (element.getLeft() == null && element.getRight() == null) {
                    return null;
                }
                // If element has only one child, automatically chose that subtree.
                else if (element.getLeft() == null) {
                    this.deleteNode(element, false);
                }
                else if (element.getRight() == null) {
                    this.deleteNode(element, true);
                }
                // If element has both children, the flag indicates which subtree should be chosen.
                else {
                    if (this.left) {
                        this.deleteNode(element, true);
                    }
                    else {
                        this.deleteNode(element, false);
                    }
                    this.left = !this.left;
                }
            }
        }
        return element;
    }

    /**
     * Deletes a specific node from the tree. It does so by replacing the node with its max or min successor.
     *
     * @param element the node to be deleted
     * @param left the flag that indicates if the left or right subtree should be chosen
     * @return the new root of the tree
     */
    private Element deleteNode(Element element, boolean left) {
        if (element == null) {
            return null;
        }

        if (left) {
            // Get left element of the node to be deleted.
            Element iterator = element.getLeft();
            // Get rightmost node in the subtree.
            while (iterator.getRight() != null) {
                iterator = iterator.getRight();
            }

            // Replace element with rightmost node.
            element.setValue(iterator.getValue());
            element.setOccurrences(iterator.getOccurrences());

            // Rebind the children of the rightmost node to current element.
            this.rebind(element, iterator, true);
        }
        else {
            // Get left element of the node to be deleted.
            Element iterator = element.getRight();
            // Get leftmost node in the subtree.
            while (iterator.getLeft() != null) {
                iterator = iterator.getLeft();
            }

            // Replace element with leftmost node.
            element.setValue(iterator.getValue());
            element.setOccurrences(iterator.getOccurrences());

            // Rebind the children of the rightmost node to current element.
            this.rebind(element, iterator, false);
        }

        return element;
    }

    /**
     * Rebinds the nodes passed as parameters.
     *
     * @param start the node to which target should be bound
     * @param target the node which should be bound to start
     * @param movingRight the flag that indicates if the target was acquired from the right or left subtree
     */
    private void rebind(Element start, Element target, boolean movingRight) {
        if (movingRight) {
            // Get left element of the node to be rebound.
            Element first = start.getLeft();
            // Find parent of target element.
            if (first.getRight() != null) {
                while (first.getRight().getValue() != target.getValue()) {
                    first = first.getRight();
                }
            }
            // Bind target to start.
            if (first.getRight() != null) {
                first.setRight(target.getLeft());
            }
            else if (target.getLeft() != null) {
                start.setLeft(target.getLeft());
            }
            else {
                start.setLeft(null);
            }
        }
        else {
            // Get left element of the node to be rebound.
            Element first = start.getRight();
            // Find parent of target element.
            if (first.getLeft() != null) {
                while (first.getLeft().getValue() != target.getValue()) {
                    first = first.getLeft();
                }
            }
            // Bind target to start.
            if (first.getLeft() != null) {
                first.setLeft(target.getRight());
            }
            else if (target.getRight() != null) {
                start.setRight(target.getRight());
            }
            else {
                start.setRight(null);
            }
        }
    }

    /**
     * Public facing find. It initializes the search and returns the result.
     *
     * @param value the value to be found
     * @return the result of the search
     */
    public boolean find(int value) {
        return this.find(this.root, value);
    }

    /**
     * Finds a specific value in the tree.
     *
     * @param element the current node in the tree
     * @param value the value to be found
     * @return the result of the search
     */
    private boolean find(Element element, int value) {
        if (element == null) {
            return false;
        }
        this.nodeComparisons++;
        if (value < element.getValue()) {
            return this.find(element.getLeft(), value);
        }
        else if (value > element.getValue()) {
            return this.find(element.getRight(), value);
        }
        else {
            return true;
        }
    }

    /**
     * Different print methods.
     */

    public void printInorder() {
        this.printInorder(this.root);
    }

    private void printInorder(Element element) {
        if (element == null) {
            return;
        }

        this.printInorder(element.getLeft());
        System.out.println(element.getValue());
        this.printInorder(element.getRight());
    }

    public void printPreorder() {
        this.printPreorder(this.root);
    }

    private void printPreorder(Element element) {
        if (element == null) {
            return;
        }

        System.out.println(element.getValue());
        this.printPreorder(element.getLeft());
        this.printPreorder(element.getRight());
    }

    public void printPostorder() {
        this.printPostorder(this.root);
    }

    private void printPostorder(Element element) {
        if (element == null) {
            return;
        }

        this.printPostorder(element.getLeft());
        this.printPostorder(element.getRight());
        System.out.println(element.getValue());
    }

    /**
     * Print the number of comparisons that happened until now.
     */
    public void printNodeComparisons() {
        System.out.println(this.nodeComparisons);
    }
}
