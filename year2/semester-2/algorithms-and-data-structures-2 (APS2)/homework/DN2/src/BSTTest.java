import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BSTTest {
    interface Fn {
        void apply();
    }

    static String captureStdout(Fn fn) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(buffer));
        fn.apply();
        System.setOut(stdout);
        return buffer.toString().replace(System.lineSeparator(), "\n");
    }

    void runOperation(BST bst, char op, String opStr) {
        switch (op) {
            case '+':
                bst.insert(Integer.parseInt(opStr));
                break;
            case '?':
                System.out.println(bst.find(Integer.parseInt(opStr)));
                break;
            case '-':
                bst.delete(Integer.parseInt(opStr));
                break;
            case 'p':
                bst.printPreorder();
                break;
            case 'i':
                bst.printInorder();
                break;
            case 'P':
                bst.printPostorder();
                break;
            case 'c':
                bst.printNodeComparisons();
                break;
            case 'o': // debugging aid, if you override BST::toString().
                System.out.println(bst);
                break;
            case '.':
                System.out.println("--");
                break;
        }
    }

    void runOperations(BST bst, String ops) {
        for (String op : ops.split("(?=[^\\d])")) {
            runOperation(bst, op.charAt(0), op.substring(1));
        }
    }

    void test(String ops, String expectedStdout) {
        BST bst = new BST();
        String capturedStdout = captureStdout(() -> runOperations(bst, ops));
        assertEquals(expectedStdout.trim(), capturedStdout.trim());
    }

    @SuppressWarnings("SameParameterValue")
    String generateRandomOps(Random random,
                             int max,
                             int nInserts,
                             int nFinds,
                             int nDeletes,
                             int nPrints,
                             int nCounts) {
        ArrayList<String> ops = new ArrayList<>();
        for (int i = 0; i < nInserts; i++) ops.add("+" + random.nextInt(max));
        for (int i = 0; i < nFinds; i++) ops.add("?" + random.nextInt(max));
        for (int i = 0; i < nDeletes; i++) ops.add("-" + random.nextInt(max));
        for (int i = 0; i < nPrints; i++) {
            int printType = random.nextInt(3);
            if (printType == 0) ops.add("p");
            if (printType == 1) ops.add("i");
            if (printType == 2) ops.add("P");
        }
        for (int i = 0; i < nCounts; i++) ops.add("c.");
        Collections.shuffle(ops, random);
        while (ops.get(0).charAt(0) != '+') { // ensure first operation is an insert
            String op = ops.get(0);
            ops.remove(0);
            ops.add(random.nextInt(ops.size() + 1), op);
        }
        return String.join("", ops);
    }

    String generateCsvFromOps(String ops) {
        BST bst = new BST();
        String capturedStdout = captureStdout(() -> runOperations(bst, ops));
        return ops + ";" + capturedStdout.replace(System.lineSeparator(), "!");
    }

    @Test
    @Disabled
    void generateTestCases() {
        Random random = new Random();
//        System.out.println("\"" + generateRandomOps(random, 20, 10, 5, 5, 0, 0) + "\",");
//        System.out.println("\"" + generateRandomOps(random, 20, 30, 5, 5, 0, 0) + "\",");
//        System.out.println("\"" + generateRandomOps(random, 20, 30, 5, 15, 0, 0) + "\",");
//        System.out.println("\"" + generateRandomOps(random, 20, 50, 5, 5, 0, 0) + "\"");
        for (int i = 0; i < 5; i++)
            System.out.println("\"" + generateCsvFromOps(generateRandomOps(random, 20, 30, 5, 5, 5, 5)) + "\",");
        for (int i = 0; i < 3; i++)
            System.out.println("\"" + generateCsvFromOps(generateRandomOps(random, 20, 20, 5, 20, 5, 5)) + "\",");
        System.out.println("\"" + generateCsvFromOps(generateRandomOps(random, 50, 30, 5, 10, 5, 5)) + "\",");
        System.out.println("\"" + generateCsvFromOps(generateRandomOps(random, 50, 30, 5, 10, 5, 5)) + "\",");
    }

    @Test
    @DisplayName("Public test 1")
    void testUcilnica1() {
        test("+19+11+23+19+29p." +
             "i." +
             "P",
             "19\n" +
             "11\n" +
             "23\n" +
             "29\n" +
             "--\n" +
             "11\n" +
             "19\n" +
             "23\n" +
             "29\n" +
             "--\n" +
             "11\n" +
             "29\n" +
             "23\n" +
             "19");
    }

    @Test
    @DisplayName("Public test 2")
    void testUcilnica2() {
        test("+19c." +
             "+11+23+31+42+29?29." +
             "+23+29-31?31?23." +
             "i." +
             "c",
             "0\n" +
             "--\n" +
             "true\n" +
             "--\n" +
             "false\n" +
             "true\n" +
             "--\n" +
             "11\n" +
             "19\n" +
             "23\n" +
             "29\n" +
             "42\n" +
             "--\n" +
             "29");
    }

    @ParameterizedTest
    @DisplayName("printInorder() prints sorted and deduplicated elements")
    @ValueSource(strings = {
        "12,0,9,17,8,5,0,20,19,19,4,12,9,10,4,18,2,13,20,5",
        "12,10,14,20,10,11,3,5,9,9,1,4,1,4,10,12,3,0,4,9",
        "4,13,5,9,1,7,0,14,14,3,1,1,9,3,14,7,12,19,11,10",
        "17,9,11,17,6,17,12,1,8,3,6,18,18,14,2,4,12,16,13,11",
        "12,8,2,11,8,7,16,17,20,12,13,11,7,0,13,1,11,9,10,19"
    })
    void testInorderSorting(String numbersStr) {
        List<Integer> numbers = Arrays.stream(numbersStr.split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList()); // converts e.g. "12,0,12,17" to List.of(12, 0, 12, 17)
        String ops = numbers.stream()
                         .map(num -> "+" + num)
                         .collect(Collectors.joining()) + "i"; // converts the list to "+12+0+12+17i"
        String expected = numbers.stream()
            .sorted()
            .distinct()
            .map(Object::toString)
            .collect(Collectors.joining("\n")); // converts the list to "0\n12\n17"
        test(ops, expected);
    }

    @ParameterizedTest
    @DisplayName("Random inserts/finds/deletes work, and all prints output the final set")
    @ValueSource(strings = {
        "+0?8+9?13?10-12-0+0-17-1+13?5?18+11+1+3-7+0+12+18",
        "+13+4+10+16+16+12+7+11-3+1+17-13+7+0+2+4+7?6+4-13+5+6+10+18?7+7?0+11+10?17?0+17-12+8+6+1+17-1+15+0",
        "+3+16+17+6+0-6+5?18+14+8-1?2?4+11+19+3?14+3+19+14+6-19-7+4+11+1+14+3-11+11-9+0-15?18+9-7-7-7+1-8+0-10-14+14+1+17+1-3+17-19",
        "+4+6+7+13+15+12+5+1+2+11-19+14?6+5+1-3+5-9+12+18+3-16+3+15+11-10+0+1+18+2+12+6+10+5+5?17+19+15+1+7+11+8+0+1?8+15+7+5+8+6?18+16+16+11+3+11+4?5+18+19"
    })
    void testBasicPrinting(String ops) {
        HashMap<Integer, Integer> expectedNumbers = new HashMap<>(); // key is number, value is count
        StringBuilder expectedFindResults = new StringBuilder(); // "true\nfalse\ntrue\ntrue...", from find()s

        // simulate inserts, finds and deletes
        for (String op : ops.split("(?=[^\\d])")) {
            char opCode = op.charAt(0); // +, -, ?
            int number = Integer.parseInt(op.substring(1));
            if (opCode == '?') {
                expectedFindResults.append(expectedNumbers.containsKey(number));
                expectedFindResults.append("\n");
            } else {
                expectedNumbers.compute(number, (k, v) -> {
                    int count = v == null ? 0 : v;
                    if (opCode == '+') count++;
                    if (opCode == '-') count--;
                    return count > 0 ? count : null; // delete key if count < 1
                });
            }
        }

        BST bst = new BST();
        assertEquals(expectedFindResults.toString(),
                     captureStdout(() -> runOperations(bst, ops)), // run inserts, finds and deletes
                     "The output of find calls doesn't match the expected output.");

        for (char printType : new char[]{'p', 'i', 'P'}) { // run preorder, inorder and postorder prints
            String printCallName; // friendly name for error messages
            if (printType == 'p') printCallName = "printPreorder()";
            else if (printType == 'i') printCallName = "printInorder()";
            else printCallName = "printPostorder()";

            String capturedStdout = captureStdout(() -> runOperation(bst, printType, ""));

            Set<Integer> printedNumbers;
            try {
                printedNumbers = Arrays.stream(capturedStdout.split("\n"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet()); // convert the output to set of numbers
            } catch (NumberFormatException e) {
                fail("The output of " + printCallName + " contains non-numbers.");
                throw e; // appease javac
            }

            assertEquals(expectedNumbers.keySet(),
                         printedNumbers,
                         "The output of " + printCallName + " doesn't match the expected output.");
        }
    }

    @Test
    @DisplayName("Root node deletion")
    void testRootNodeDeletion() {
        test("+1-1+0p", "0");
        test("+1+2-1p", "2");
        test("+2+1+3-2p." +
             "+0-1p",
             "1\n3\n--\n" +
             "3\n0");
        test("+20+10+30+5+15+17+16+25+35-20p",
             "17\n" +
             "10\n" +
             "5\n" +
             "15\n" +
             "16\n" +
             "30\n" +
             "25\n" +
             "35");
    }

    @Test
    @DisplayName("Basic deletion")
    void testDelete() {
        test("+2+1+3p." +
             "-1p.-3p",
             "2\n1\n3\n--\n" +
             "2\n3\n--\n" +
             "2");
        test("+17+10+5+15+16+30+25+35p." +
             "-30p.-10p",
             "17\n10\n5\n15\n16\n30\n25\n35\n--\n" +
             "17\n10\n5\n15\n16\n25\n35\n--\n" +
             "17\n15\n5\n16\n25\n35");
    }

    @Test
    @DisplayName("Replacement of deleted element is moved")
    void testDeleteMove() {
        test("+20+10+30+5+15+4+7+6+6+7-10p." +
             "-6-7p",
             "20\n7\n5\n4\n6\n15\n30\n--\n" +
             "20\n7\n5\n4\n6\n15\n30");
        test("+20+10+30+5+15+4+6+6-10p." +
             "-6p." +
             "-6p." +
             "-20p",
             "20\n6\n5\n4\n15\n30\n--\n" +
             "20\n6\n5\n4\n15\n30\n--\n" +
             "20\n15\n5\n4\n30\n--\n" +
             "15\n5\n4\n30");
    }

    @Test
    @DisplayName("Deletion only changes direction when deleted element has two subtrees")
    void testDeleteAlternating() {
        test("+20+10+30+5+15p." + // 20 (10 (5 15) 30)
             "-10p.+4p." + // replace 10 with left, left = 4
             "-5p.+16p." + // replace 5 with right, right = 16
             "-15p.+2p." + // replace 15 with left, left = 2
             "-2p.+2p." + // delete leaf 2, add it again
             "-4p.+17p." + // replace 4 with right, right = 17
             "+3-2p." + // add 3 to the right of 2, delete 2
             "-16p", // replace 16 with left
             "20\n10\n5\n15\n30\n--\n" +
             "20\n5\n15\n30\n--\n20\n5\n4\n15\n30\n--\n" + // replace with left
             "20\n15\n4\n30\n--\n20\n15\n4\n16\n30\n--\n" + // replace with right
             "20\n4\n16\n30\n--\n20\n4\n2\n16\n30\n--\n" + // replace with left
             "20\n4\n16\n30\n--\n20\n4\n2\n16\n30\n--\n" + // delete leaf
             "20\n16\n2\n30\n--\n20\n16\n2\n17\n30\n--\n" + // replace with right
             "20\n16\n3\n17\n30\n--\n" + // make node with 1 subtree, delete it
             "20\n3\n17\n30"); // replace with left
    }

    @Test
    @DisplayName("Find works")
    void testBasicFind() {
        test("+20+10+30+5+15+4+7+6+17" +
             "?20?10?30?5?15?4?7?6?17" +
             "?16?1?3" +
             "-10?10?4?6?7",
             "true\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\ntrue\n" +
             "false\nfalse\nfalse\n" +
             "false\ntrue\ntrue\ntrue");
    }

    @Test
    @DisplayName("Counter works")
    void testBasicCounter() {
        test("+2c+1+3c+4c+5c+6c." +
             "?1c?4c." +
             "-2+0c+2c-3c",
             "0\n2\n4\n7\n11\n--\n" +
             "true\n13\ntrue\n16\n--\n" +
             "18\n20\n22");
    }

    // Generated from my own implementation, might be buggy.
    // If you get 10/10 and this test doesn't pass, please let me know.
    @ParameterizedTest
    @DisplayName("Randomly generated tests")
    @CsvSource(delimiter = ';', value = {
        "+8+17+0+4c.?10c.?5P+12+9p+7P+13+15p+4c.+9+14+18-1?11+0-14+7+14+15-19c.c.+14+19+12+18+12+14+8+15?14+0+5+17-1-7+16?7p+12;4!--!false!6!--!false!4!0!17!8!8!0!4!17!12!9!7!4!0!9!12!17!8!8!0!4!7!17!12!9!13!15!27!--!false!70!--!70!--!true!true!8!0!4!7!5!17!12!9!13!15!14!16!18!19!",
        "+6?4+15+2-11+1i+19+17-6+0+19-15+0c.ic.P+14+12+14+14+4+18+18c.?3+19?6+12?8+2-19+17+14pc.+5+16c.+19+6+11+5-5+19+3p+18?17;false!1!2!6!15!23!--!0!1!2!17!19!23!--!0!1!17!19!2!50!--!false!false!false!2!1!0!19!17!14!12!4!18!85!--!95!--!2!1!0!19!17!14!12!4!3!5!6!11!16!18!true!",
        "+19+14c.P?10+4+6+17P+19-7?12?9+15?5+1+6+10-6+9+7+2+16-2+0+11c.+5c.+11+5+13+12?16i+4+6c.+6p+13-3+13+9+9c.+16-7i+13;1!--!14!19!false!6!4!17!14!19!false!false!false!78!--!82!--!true!0!1!4!5!6!7!9!10!11!12!13!14!15!16!17!19!118!--!19!14!4!1!0!6!5!10!9!7!11!13!12!17!15!16!152!--!0!1!4!5!6!9!10!11!12!13!14!15!16!17!19!",
        "+2-8-13+17P+18+18+16i+10+7?14+18+17+3?3?14+14+12+7+15-2+13+4+10+9-6+10i?18+6c.+11+19-5+9+5c.+19c.Pp+4+2+3?5+19+1c.c.;17!2!2!16!17!18!false!true!false!3!4!7!9!10!12!13!14!15!16!17!18!true!95!--!121!--!124!--!5!6!4!3!9!7!11!13!12!15!14!10!16!19!18!17!17!16!10!7!3!4!6!5!9!14!12!11!13!15!18!19!true!157!--!157!--!",
        "+2+11?8p-18+8+16+10-13+2+13+1P+2c.?17+4+7?1+6+15?4+4+7+17+19+2-7+15P+1+4?4c.+8Pc.c.+7-14+1c.+5+19-17i+18+11+18+17;false!2!11!1!10!8!13!16!11!2!21!--!false!true!true!1!6!7!4!10!8!15!13!19!17!16!11!2!true!83!--!1!6!7!4!10!8!15!13!19!17!16!11!2!86!--!86!--!98!--!1!2!4!5!6!7!8!10!11!13!15!16!19!",
        "+17c.+6-12+7+2-6-6-18-16-4-5+5-11+2c.c.c.+19p-13+12-14+13?1+18i+12-17+15-3+11-18+19?8i-9+3?15-19-0?18-1+12+5-5+18+9+10-6?3-17ic.i;0!--!30!--!30!--!30!--!17!2!7!5!19!false!2!5!7!12!13!17!18!19!false!2!5!7!11!12!13!15!19!true!false!true!2!3!5!7!9!10!11!12!13!15!18!19!130!--!2!3!5!7!9!10!11!12!13!15!18!19!",
        "+15-17+9-7+5+7-5+0+19?5-15-14?2-14-18+14P+7-4?2-6-8-18-5i+17-0+14-15+18+16+5-18c.+19+2+2?6P-18-2c.c.+0?0+17-17p-7+2p-4c.+17c.;false!false!0!7!14!19!9!false!0!7!9!14!19!79!--!false!2!5!7!16!17!14!19!9!99!--!99!--!true!9!7!5!2!0!19!14!17!16!9!7!5!2!0!19!14!17!16!126!--!130!--!",
        "+19+3+19+10c.-12-12-15+5+19+18-7i+4+11-6-1+12p?17c.+9-3?17ic.+15+12-7+19+8-1?16c.c.-3+13-2p-4-1+9-0i+15+15?9-15-5+19-2-1-13-15?18;4!--!3!5!10!18!19!19!3!10!5!4!18!11!12!false!49!--!false!4!5!9!10!11!12!18!19!60!--!false!89!--!89!--!19!10!5!4!9!8!18!11!12!15!13!5!8!9!10!11!12!13!15!18!19!true!true!",
        "+45+11-34+19+13c.i+13+11+25+14-26+28c.i-25?48-12+3+14+44+7+0+0+29P?24-28+37+0-12-27+37+1c.+4+32?18c.-4-18p+2c.?42+49+26+24+42?12i+47+23+5-40;8!--!11!13!19!45!29!--!11!13!14!19!25!28!45!false!0!7!3!14!13!29!44!28!19!11!45!false!100!--!false!115!--!45!11!3!0!1!7!19!13!14!44!29!37!32!130!--!false!false!0!1!2!3!7!11!13!14!19!24!26!29!32!37!42!44!45!49!",
        "+20?36c.-32-18+36+1?26c.+37+8+36-43-19+18+36P-19Pc.+25+39+39?9+33+46+42-40?44+38+5i+17-13+37+37c.-41+13P+17?8-39+5i+15+31+2+3-41+12+6+18+7c.;false!1!--!false!7!--!18!8!1!37!36!20!18!8!1!37!36!20!28!--!false!false!1!5!8!18!20!25!33!36!37!38!39!42!46!87!--!5!13!17!18!8!1!33!25!38!42!46!39!37!36!20!true!1!5!8!13!17!18!20!25!33!36!37!38!39!42!46!158!--!",
    })
    void testRandom(String ops, String expectedStdout) {
        test(ops, expectedStdout.replace('!', '\n'));
    }
}
