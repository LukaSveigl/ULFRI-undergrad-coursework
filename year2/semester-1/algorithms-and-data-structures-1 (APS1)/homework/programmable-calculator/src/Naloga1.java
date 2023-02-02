import java.util.Scanner;


@SuppressWarnings("all")
public class Naloga1 {
    public static void main(String[] args) {
        Calculator calc = new Calculator();

        calc.execute();
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

    @SuppressWarnings("unused")
    boolean isEmpty();

    @SuppressWarnings("unused")
    boolean isFull();

    @SuppressWarnings("unused")
    int size();

    String toString();
}


interface iStack<T> extends Collection {
    @SuppressWarnings("unused")
    T top() throws CollectionException;

    @SuppressWarnings("unused")
    void push(T x) throws CollectionException;

    @SuppressWarnings("unused")
    T pop() throws CollectionException;
}

@SuppressWarnings("all")
interface iSequence<T> extends Collection {
    @SuppressWarnings("all")
    static final String ERR_MSG_INDEX = "Wrong index in sequence.";

    @SuppressWarnings("unused")
    T get(int i) throws CollectionException;

    @SuppressWarnings("unused")
    void add(T x) throws CollectionException;
}

//@SuppressWarnings("all")
class Stack<T> implements iStack<T> {
    // Internal array
    private T[] arr;

    @SuppressWarnings("FieldCanBeLocal")
    private final int DEFAULT_CAPACITY = 64;

    // Counter of current elements in array
    private int size;


    /*
        Constructors
     */


    @SuppressWarnings("unchecked")
    Stack() {
        this.arr = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings({"unchecked", "unused"})
    Stack(int capacity) {
        this.arr = (T[]) new Object[capacity];
        this.size = 0;
    }


    /*
        Utility methods
     */


    @SuppressWarnings("all")
    private void rewrite() {
        // Method to rewrite array without last element, effectively deleting it
        // This is protection against loitering

        // Temporary array to be cloned
        @SuppressWarnings("unchecked")
        T[] clone = (T[]) new Object[this.arr.length];

        // Copy all elements but last (size has to be reduced before call of this method)
        for (int i = 0; i < this.size; i++) {
            clone[i] = this.arr[i];
        }

        // Clone temporary array into internal array
        this.arr = clone.clone();
    }


    /*
        Collection methods
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
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        if (!this.isEmpty()) {
            for (int i = 0; i < this.size; i++) {
                sb.append(this.arr[i]);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");

        return sb.toString();
    }


    /*
        Stack methods
     */


    @Override
    public T top() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        return this.arr[this.size - 1];
    }

    @Override
    public void push(T x) throws CollectionException {
        if (this.isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }
        // Add element to last place
        this.arr[this.size] = x;

        // Increase counter of elements
        this.size++;
    }

    @Override
    public T pop() throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }

        // Get last element in internal array
        T el = this.arr[this.size - 1];

        // Reduce counter of elements in array
        this.size--;

        // Rewrite internal array
        this.rewrite();

        return el;
    }
}

@SuppressWarnings("all")
class Sequence<T> implements iSequence<T> {
    // Internal array
    @SuppressWarnings("FieldMayBeFinal")
    private T[] arr;

    @SuppressWarnings("FieldCanBeLocal")
    private final int DEFAULT_CAPACITY = 64;

    // Counter of current elements in array
    private int size;

    /*
        Constructors
     */

    @SuppressWarnings("unchecked")
    Sequence() {
        this.arr = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    Sequence(int capacity) {
        this.arr = (T[]) new Object[capacity];
        this.size = 0;
    }

    /*
        Collection methods
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
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (int i = 0; i < this.size; i++) {
            sb.append(this.arr[i]);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");

        return sb.toString();
    }


    /*
        Sequence methods
     */


    @Override
    public T get(int i) throws CollectionException {
        if (this.isEmpty()) {
            throw new CollectionException(ERR_MSG_EMPTY);
        }
        if (i < 0 || i > this.size - 1) {
            throw new CollectionException(ERR_MSG_INDEX);
        }
        return this.arr[i];

    }

    @Override
    public void add(T x) throws CollectionException {
        if (this.isFull()) {
            throw new CollectionException(ERR_MSG_FULL);
        }
        // Add element to end of array
        this.arr[this.size] = x;

        // Increase counter of elements
        this.size++;
    }
}

@SuppressWarnings("all")
class Calculator {
    // Internal sequence of stacks used in calculator
    @SuppressWarnings("FieldMayBeFinal")
    private Sequence<Stack<String>> stackSequence;

    // Internal variable to hold condition results
    private boolean conditional;


    /*
        Constructors
     */


    Calculator() {
        // Initialize stackSequence with capacity 42 (1 main stack and 41 auxiliary stacks)
        this.stackSequence = new Sequence<>(42);

        // Create new stack in each space in sequence
        try {
            for (int i = 0; i < 42; i++) {
                this.stackSequence.add(new Stack<>());
            }
        }
        catch (CollectionException e) {
            System.out.println(e.getMessage());
        }

        this.conditional = false;
    }


    /*
        Utility methods
     */


    private void clearAll() throws CollectionException {
        // Method to clear all stacks after execution of command

        for (int i = 0; i < this.stackSequence.size(); i++) {
            while (!this.stackSequence.get(i).isEmpty()) {
                this.stackSequence.get(i).pop();
            }
        }
    }


    /*
        Main methods
     */


    public void execute() {
        Scanner sc = new Scanner(System.in);
        String input;

       do {
            // Print prompt sign and get input
            //System.out.print("> ");
            input = sc.nextLine();

            if (input.isEmpty()) {
                break;
            }
            // If input is empty - end program
            //if (input.equals("")) {
            //    break;
            //}

            // Condense spaces into 1
            input = input.replaceAll("\\s+", " ");

            // Split input string into separate commands
            String[] commands = input.split(" ");

            // Variables to hold index of fun command and amount of operations
            int index = 0;
            int count = 0;
            boolean funSet = false;

            // Loop through commands
            for (int i = 0; i < commands.length; i++) {

                try {
                    if (i == index + count + 1) {
                        funSet = false;
                    }

                    // If command is fun - store index and amount of operations
                    if (commands[i].equals("fun")) {
                        if (funSet == false) {
                            index = i;
                            count = Integer.parseInt(commands[i - 2]);
                            funSet = true;
                        }
                    }

                    // If current index lower than fun index or higher than fun index + amount of operations
                    // This is needed to ignore commands that fun operation stores
                    if (i > index + count || i <= index) {
                        //System.out.println("Command: " + commands[i]);
                        this.executeCommand(commands[i], commands, i);
                    }


                }
                catch (CollectionException e) {
                    System.out.println(e.getMessage());
                }

            }

            // Reset conditional and stacks
            this.conditional = false;

            try {
                this.clearAll();
            }
            catch (CollectionException e) {
                System.out.println(e.getMessage());
            }

        } while (sc.hasNextLine() );
    }

    @SuppressWarnings("all")
    private void executeCommand(String command, String [] commands, int index) throws CollectionException{
        String tmpComm = command;

        // If command starts with condition prefix
        if (command.charAt(0) == '?') {
            // If condition is false - do nothing
            if (!this.conditional) {
                return;
            }
            // If condition is true - strip prefix and proceed
            else {
                tmpComm = command.substring(1);
            }
        }

        /*System.out.println("Stack 0: " + this.stackSequence.get(0));
        System.out.println("Stack 1: " + this.stackSequence.get(1));
        System.out.println("Stack 2: " + this.stackSequence.get(2));
        System.out.println("Stack 3: " + this.stackSequence.get(3));
        System.out.println("Stack 4: " + this.stackSequence.get(4));
        System.out.println("Command: " + command);
        System.out.println("-------------------------------------\n");*/

        // Switch over all possible operations
        switch (tmpComm) {
            case "echo":
                this.echo();
                break;
            case "pop":
                this.pop();
                break;
            case "dup":
                this.dup();
                break;
            case "dup2":
                this.dup2();
                break;
            case "swap":
                this.swap();
                break;
            case "char":
                this.chr();
                break;
            case "even":
                this.even();
                break;
            case "odd":
                this.odd();
                break;
            case "!":
                this.fact();
                break;
            case "len":
                this.len();
                break;
            case "<>":
                this.diff();
                break;
            case "<":
                this.lw();
                break;
            case "<=":
                this.le();
                break;
            case "==":
                this.eq();
                break;
            case ">":
                this.hi();
                break;
            case ">=":
                this.he();
                break;
            case "+":
                this.add();
                break;
            case "-":
                this.sub();
                break;
            case "*":
                this.mul();
                break;
            case "/":
                this.div();
                break;
            case "%":
                this.mod();
                break;
            case ".":
                this.cat();
                break;
            case "rnd":
                this.rnd();
                break;
            case "then":
                this.then();
                break;
            case "else":
                this.neg();
                break;
            case "print":
                this.print();
                break;
            case "clear":
                this.clear();
                break;
            case "run":
                this.run();
                break;
            case "loop":
                this.loop();
                break;
            case "fun":
                // Store all commands from fun command index - needed for fun command to ignore commands that come before
                String [] tmpCommands = new String[commands.length - index];

                for (int i = 0; i < tmpCommands.length - 1; i++) {
                    tmpCommands[i] = commands[i + index + 1];
                }

                this.fun(tmpCommands);
                break;
            case "move":
                this.move();
                break;
            case "reverse":
                this.reverse();
                break;
            default:
                // If no command - add element to main stack
                this.stackSequence.get(0).push(tmpComm);
        }
    }


    /*
        Operation methods
     */

    // Operations that manipulate main stack

    private void echo() throws CollectionException {
        // Method that prints top element of main stack

        if (this.stackSequence.get(0).isEmpty()) {
            System.out.println();
        }
        else {
            System.out.println(this.stackSequence.get(0).top());
        }
    }

    private void pop() throws CollectionException {
        // Method that deletes top element of main stack

        this.stackSequence.get(0).pop();
    }

    private void dup() throws CollectionException {
        // Method that duplicates top element of main stack

        String item = this.stackSequence.get(0).pop();

        this.stackSequence.get(0).push(item);
        this.stackSequence.get(0).push(item);
    }

    private void dup2() throws CollectionException {
        // Method that duplicates top 2 elements of main stack
        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();



        this.stackSequence.get(0).push(item2);
        this.stackSequence.get(0).push(item1);

        this.stackSequence.get(0).push(item2);
        this.stackSequence.get(0).push(item1);
    }

    private void swap() throws CollectionException {
        // Method that swaps top 2 elements of main stack

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        this.stackSequence.get(0).push(item1);
        this.stackSequence.get(0).push(item2);
    }

    // Operations that modify top on main stack based on some value

    private void chr() throws CollectionException {
        // Method that converts top element of main stack to corresponding ASCII character

        String item = this.stackSequence.get(0).pop();

        // If character is unicode
        if (item.charAt(0) == '\\' || item.charAt(0) == 'u' || item.charAt(0) == 'U') {
            // Get Integer value from String on top of stack, parse in hex
            // Convert to char via casting
            // Convert char to String and push to stack
            this.stackSequence.get(0).push(Character.toString( (char) Integer.parseInt(item.substring(2), 16)));
        }
        else {
            // Get Integer value from String on top of stack
            // Convert to char via casting
            // Convert char to String and push to stack
            this.stackSequence.get(0).push(Character.toString( (char) Integer.parseInt(item)));
        }


    }

    private void even() throws CollectionException {
        // Method that replaces top element of main stack with 1 if number is even, or 0 if odd

        String item = this.stackSequence.get(0).pop();
        int itemInt = Integer.parseInt(item);

        if (itemInt % 2 == 0) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void odd() throws CollectionException {
        // Method that replaces top element of main stack with 1 if number is odd, or 0 if even

        String item = this.stackSequence.get(0).pop();
        int itemInt = Integer.parseInt(item);

        if (itemInt % 2 == 0) {
            this.stackSequence.get(0).push("0");
        }
        else {
            this.stackSequence.get(0).push("1");
        }
    }

    private void fact() throws CollectionException {
        // Method that replaces top element of main stack with it's factorial

        String item = this.stackSequence.get(0).pop();
        int itemInt = Integer.parseInt(item);

        int fact = 1;

        for (int i = itemInt; i > 1; i--) {
            fact *= i;
        }

        this.stackSequence.get(0).push(Integer.toString(fact));
    }

    private void len() throws CollectionException {
        // Method that replaces top element of main stack with it's length

        String item = this.stackSequence.get(0).pop();

        this.stackSequence.get(0).push(Integer.toString(item.length()));
    }

    // Operations that perform an action on top 2 elements in main stack and replace them with value

    private void diff() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if they differ, or 0 if they are the same

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        if (item1.equals(item2)) {
            this.stackSequence.get(0).push("0");
        }
        else {
            this.stackSequence.get(0).push("1");
        }
    }

    private void lw() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if top element is lower, otherwise 0

        String item2 = this.stackSequence.get(0).pop();
        String item1 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        if (itemInt1 < itemInt2) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void le() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if top element is lower or equal, otherwise 0

        String item2 = this.stackSequence.get(0).pop();
        String item1 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        if (itemInt1 <= itemInt2) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void eq() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if they are the same, otherwise 0

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        if (item1.equals(item2)) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void hi() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if top element is higher, otherwise 0

        String item2 = this.stackSequence.get(0).pop();
        String item1 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        if (itemInt1 > itemInt2) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void he() throws CollectionException {
        // Method that replaces top 2 elements of main stack with 1 if top element is higher or equal, otherwise 0

        String item2 = this.stackSequence.get(0).pop();
        String item1 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        if (itemInt1 >= itemInt2) {
            this.stackSequence.get(0).push("1");
        }
        else {
            this.stackSequence.get(0).push("0");
        }
    }

    private void add() throws CollectionException {
        // Method that replaces top 2 elements of main stack with their sum

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        this.stackSequence.get(0).push(Integer.toString(itemInt1 + itemInt2));
    }

    private void sub() throws CollectionException {
        // Method that replaces top 2 elements of main stack with their difference
        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        this.stackSequence.get(0).push(Integer.toString(itemInt2 - itemInt1));
    }

    private void mul() throws CollectionException {
        // Method that replaces top 2 elements of main stack with their product

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        this.stackSequence.get(0).push(Integer.toString(itemInt1 * itemInt2));
    }

    private void div() throws CollectionException {
        // Method that replaces top 2 elements of main stack with their quotient

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        this.stackSequence.get(0).push(Integer.toString(itemInt2 / itemInt1));
    }

    private void mod() throws CollectionException {
        // Method that replaces top 2 elements of main stack with reminder in integer division

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        this.stackSequence.get(0).push(Integer.toString(itemInt2 % itemInt1));
    }

    private void cat() throws CollectionException {
        // Method that replaces top 2 elements of main stack with their concatenation

        String item2 = this.stackSequence.get(0).pop();
        String item1 = this.stackSequence.get(0).pop();

        this.stackSequence.get(0).push(item1 + item2);
    }

    private void rnd() throws CollectionException {
        // Method that replaces top 2 elements of main stack with a random value in interval [top, below_top]

        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int itemInt1 = Integer.parseInt(item1);
        int itemInt2 = Integer.parseInt(item2);

        int rnd = (int) ((Math.random() * (itemInt2 - itemInt1)) + itemInt1);

        this.stackSequence.get(0).push(Integer.toString(rnd));
    }

    // Operations that enable conditionals

    private void then() throws CollectionException {
        // Method that sets internal conditional to true if top element in main stack doesn't equal 0, false otherwise

        String item = this.stackSequence.get(0).pop();

        this.conditional = !item.equals("0");
    }

    private void neg() {
        // Method that negates internal conditional

        this.conditional = !this.conditional;
    }

    // Operations for work with auxiliary stacks

    private void print() throws CollectionException {
        // Method that prints stack, or prints empty line if stack is empty

        // Get index of stack to be printed
        String item = this.stackSequence.get(0).pop();
        int index = Integer.parseInt(item);

        // Get that stack as string
        String stack = this.stackSequence.get(index).toString();

        // Replace needed values
        stack = stack.replace(", ", " ");
        stack = stack.replace("[", "");
        stack = stack.replace("]", "");


        // Print stack inline
        System.out.println(stack);
    }

    private void clear() throws CollectionException {
        // Method that clears certain stack

        // Get index of stack to be cleared
        String item = this.stackSequence.get(0).pop();
        int index = Integer.parseInt(item);

        // Pop all elements
        while (!this.stackSequence.get(index).isEmpty()) {
            this.stackSequence.get(index).pop();
        }
    }

    private void fun(String [] commands) throws CollectionException {
        // Method that creates a function - stores a certain amount of operations to new stack

        // Get index of target stack and amount of operations to transfer
        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int index = Integer.parseInt(item1);
        int count = Integer.parseInt(item2);

        // Counter of operations already stored
        int i = 0;

        for (String command : commands) {
            i++;

            if (i == count + 1) {
                break;
            }

            // Store operation to target stack
            this.stackSequence.get(index).push(command);
        }
    }

    private void run() throws CollectionException {
        // Method that runs function created with fun command - run operations in certain stack

        // Get index of stack to run
        String item = this.stackSequence.get(0).pop();

        int index = Integer.parseInt(item);

        // Create array of operations to perform
        String [] tmp = new String[this.stackSequence.get(index).size()];

        int i = 0;

        while (!this.stackSequence.get(index).isEmpty()) {
            tmp[i] = this.stackSequence.get(index).pop();

            i++;
        }

        // Refill stack that needs to be run
        for (int k = tmp.length - 1; k >= 0; k--) {
            this.stackSequence.get(index).push(tmp[k]);
        }

        // Execute all operations stored
        for (int j = tmp.length - 1; j >= 0 ; j--) {
            this.executeCommand(tmp[j], tmp, j);
        }
    }

    private void loop() throws CollectionException {
        // Method that loops through stack and runs all operations a certain amount of times

        // Get index and amount of repetitions
        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();

        int count = Integer.parseInt(item2);

        // Put index back
        this.stackSequence.get(0).push(item1);

        // Run operations & replace stack index
        for (int i = 0; i < count; i++) {
            this.run();
            this.stackSequence.get(0).push(item1);
        }

        // Remove stack index
        this.stackSequence.get(0).pop();
    }

    private void move() throws CollectionException {
        // Method that transfers elements from main stack to some auxiliary stack

        // Get stack index and amount of elements to move
        String item1 = this.stackSequence.get(0).pop();
        String item2 = this.stackSequence.get(0).pop();


        int index = Integer.parseInt(item1);
        int count = Integer.parseInt(item2);

        // Move elements
        for (int i = 0; i < count; i++) {
            this.stackSequence.get(index).push(this.stackSequence.get(0).pop());
        }
    }

    private void reverse() throws CollectionException {
        // Method that reverses some stack

        // Get index of stack to reverse
        String item = this.stackSequence.get(0).pop();
        int index = Integer.parseInt(item);

        // Create temporary stack
        Stack<String> tmp = new Stack<>();

        while (!this.stackSequence.get(index).isEmpty()) {
            tmp.push(this.stackSequence.get(index).pop());
        }

        // Create reversion stack
        Stack<String> reverse = new Stack<>();

        while (!tmp.isEmpty()) {
            reverse.push(tmp.pop());
        }

        // Fill back needed stack
        while (!reverse.isEmpty()) {
            this.stackSequence.get(index).push(reverse.pop());
        }
    }
}
