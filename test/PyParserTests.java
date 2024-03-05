import lab4gen.PySubset.PySubsetParser;
import lab4gen.PySubset.Tree;
import org.junit.Test;

import java.io.*;
import java.text.ParseException;

public class PyParserTests {
    private static final PySubsetParser parser = new PySubsetParser();
    private static final String EXP = "(a and b) or not (c xor (a or not b))";
    private static final String A_PLUS_B = "a or b";

    private static final String XOR = "(a and b) xor (c or not b)";

    private static final String BRACKETS_LATE = "a and b or c and (x or y)";

    private static final String EMPTY = "()";

    private static final String BAD_LETTER = "N";

    private static int cur = 0;

    private static void dfs(Tree tree, int prId, int curId, StringBuilder sb) {
        if (!tree.children.isEmpty()) {
            System.out.print(tree.node);
            System.out.println(tree.children.size());

            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, tree.node));

            if (prId != -1) {
                sb.append(String.format("\t%d -> %d\n", prId, curId));
            }

            tree.children.forEach(child -> dfs(child, curId, ++cur, sb));
        } else {
            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, tree.node))
                    .append(String.format("\t%s -> %d\n", prId, curId));
            ++cur;
        }
    }

    @Test
    public void example() throws ParseException {
        printMe(parser.parse(EXP));
    }

    @Test
    public void aPlusB() throws ParseException {
        printMe(parser.parse(A_PLUS_B));
    }

    @Test
    public void XOR() throws ParseException {
        printMe(parser.parse(XOR));
    }

    @Test
    public void bracket_late() throws ParseException {
        printMe(parser.parse(BRACKETS_LATE));
    }

    @Test(expected = AssertionError.class)
    public void EMPTY() throws ParseException {
        printMe(parser.parse(EMPTY));
    }

    @Test(expected = AssertionError.class)
    public void LETTER() throws ParseException {
        printMe(parser.parse(BAD_LETTER));
    }

    private void printMe(Tree ans) {
        String FILE = "graph2.txt";

        try (final Writer writer = new FileWriter(FILE)) {
            StringBuilder sb = new StringBuilder("digraph {\n");
            dfs(ans, -1, 0, sb);
            sb.append("}");

            writer.write(String.valueOf(sb));
        } catch (final IOException e) {
            System.err.println("Error in writing to graph");
        }
    }
}
